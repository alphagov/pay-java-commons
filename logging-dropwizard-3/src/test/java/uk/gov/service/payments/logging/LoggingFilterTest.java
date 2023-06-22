package uk.gov.service.payments.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoggingFilterTest {

    private LoggingFilter loggingFilter;

    @Mock
    HttpServletRequest mockRequest;

    @Mock
    HttpServletResponse mockResponse;

    @Mock
    FilterChain mockFilterChain;

    private Appender<ILoggingEvent> mockAppender;

    @Captor
    ArgumentCaptor<LoggingEvent> loggingEventArgumentCaptor;

    @BeforeEach
    public void setup() {
        loggingFilter = new LoggingFilter();
        Logger root = (Logger) LoggerFactory.getLogger(LoggingFilter.class);
        mockAppender = mockAppender();
        root.addAppender(mockAppender);
    }

    @Test
    public void shouldLogEntryAndExitPointsOfEndPoints() throws Exception {
        when(mockRequest.getRequestURI()).thenReturn("/publicauth-request");
        when(mockRequest.getMethod()).thenReturn("GET");

        loggingFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockAppender, times(2)).doAppend(loggingEventArgumentCaptor.capture());
        List<LoggingEvent> loggingEvents = loggingEventArgumentCaptor.getAllValues();

        assertThat(loggingEvents.get(0).getFormattedMessage(), is("GET to /publicauth-request began"));
        String endLogMessage = loggingEvents.get(1).getFormattedMessage();
        assertThat(endLogMessage, containsString("GET to /publicauth-request ended - total time "));
        assertThat(endLogMessage, matchesRegex(".*total time \\d+ms"));
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldLogEntryAndExitPointsEvenIfRequestIdDoesNotExist() throws Exception {
        when(mockRequest.getRequestURI()).thenReturn("/publicauth-request");
        when(mockRequest.getMethod()).thenReturn("GET");

        loggingFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockAppender, times(2)).doAppend(loggingEventArgumentCaptor.capture());
        List<LoggingEvent> loggingEvents = loggingEventArgumentCaptor.getAllValues();

        assertThat(loggingEvents.get(0).getFormattedMessage(), is("GET to /publicauth-request began"));
        String endLogMessage = loggingEvents.get(1).getFormattedMessage();
        assertThat(endLogMessage, containsString("GET to /publicauth-request ended - total time "));
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldLogEntryAndExitPointsEvenWhenFilterChainingThrowsException() throws Exception {
        when(mockRequest.getRequestURI()).thenReturn("/publicauth-url-with-exception");
        when(mockRequest.getMethod()).thenReturn("GET");

        IOException exception = new IOException("Failed request");
        doThrow(exception).when(mockFilterChain).doFilter(mockRequest, mockResponse);

        loggingFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockAppender, times(3)).doAppend(loggingEventArgumentCaptor.capture());
        List<LoggingEvent> loggingEvents = loggingEventArgumentCaptor.getAllValues();

        assertThat(loggingEvents.get(0).getFormattedMessage(), is("GET to /publicauth-url-with-exception began"));
        assertThat(loggingEvents.get(1).getFormattedMessage(), is(format("Exception - %s", exception.getMessage())));
        assertThat(loggingEvents.get(1).getLevel(), is(Level.ERROR));
        assertThat(loggingEvents.get(1).getThrowableProxy().getMessage(), is("Failed request"));
        String endLogMessage = loggingEvents.get(2).getFormattedMessage();
        assertThat(endLogMessage, containsString("GET to /publicauth-url-with-exception ended - total time "));
        assertThat(endLogMessage, matchesRegex(".*total time \\d+ms"));
    }

    @Test
    public void shouldSetDiagnosticContextPerRequest() {
        when(mockRequest.getHeader("X-Request-Id")).thenReturn("some-id");
        loggingFilter.doFilter(mockRequest, mockResponse, mockFilterChain);
        assertThat(MDC.get("x_request_id"), is("some-id"));
    }

    @SuppressWarnings("unchecked")
    private <T> Appender<T> mockAppender() {
        return mock(Appender.class);
    }

}
