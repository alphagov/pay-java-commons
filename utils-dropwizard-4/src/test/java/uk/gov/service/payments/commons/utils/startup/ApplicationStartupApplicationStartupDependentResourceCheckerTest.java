package uk.gov.service.payments.commons.utils.startup;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationStartupApplicationStartupDependentResourceCheckerTest {
    private ApplicationStartupDependentResourceChecker applicationStartupDependentResourceChecker;

    @Mock
    DatabaseStartupResource mockApplicationStartupDependentResource;
    @Mock
    Consumer<Duration> mockWaiter;

    private Appender mockAppender;

    @Captor
    ArgumentCaptor<LoggingEvent> loggingEventArgumentCaptor;

    @BeforeEach
    public void setup() {
        mockAppender = mock(Appender.class);
        ((Logger) LoggerFactory.getLogger(ApplicationStartupDependentResourceChecker.class)).addAppender(mockAppender);

        applicationStartupDependentResourceChecker = new ApplicationStartupDependentResourceChecker(mockApplicationStartupDependentResource, mockWaiter);
    }

    @Test
    public void start_ShouldWaitAndLogUntilDatabaseIsAccessible() {

        when(mockApplicationStartupDependentResource.toString()).thenReturn("DatabaseStartupResource[url=mock, user=mock]");
        when(mockApplicationStartupDependentResource.isAvailable())
                .thenReturn(false)
                .thenReturn(true);

        applicationStartupDependentResourceChecker.checkAndWaitForResource();

        verify(mockApplicationStartupDependentResource, times(2)).isAvailable();
        verify(mockWaiter).accept(Duration.ofSeconds(5));

        verify(mockAppender, times(2)).doAppend(loggingEventArgumentCaptor.capture());
        List<LoggingEvent> allValues = loggingEventArgumentCaptor.getAllValues();

        assertThat(allValues.get(0).getFormattedMessage(), is("Waiting for 5 seconds until DatabaseStartupResource[url=mock, user=mock] is available ..."));
        assertThat(allValues.get(1).getFormattedMessage(), is("DatabaseStartupResource[url=mock, user=mock] available."));
    }

    @Test
    public void start_ShouldProgressivelyIncrementSleepingTimeBetweenChecksForDBAccessibility() {
        when(mockApplicationStartupDependentResource.toString()).thenReturn("DatabaseStartupResource[url=mock, user=mock]");
        when(mockApplicationStartupDependentResource.isAvailable())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        applicationStartupDependentResourceChecker.checkAndWaitForResource();

        verify(mockApplicationStartupDependentResource, times(4)).isAvailable();
        verify(mockWaiter).accept(Duration.ofSeconds(5));
        verify(mockWaiter).accept(Duration.ofSeconds(10));
        verify(mockWaiter).accept(Duration.ofSeconds(15));
        verify(mockAppender, times(4)).doAppend(loggingEventArgumentCaptor.capture());

        List<LoggingEvent> logStatement = loggingEventArgumentCaptor.getAllValues();
        assertThat(logStatement.get(0).getFormattedMessage(), is("Waiting for 5 seconds until DatabaseStartupResource[url=mock, user=mock] is available ..."));
        assertThat(logStatement.get(1).getFormattedMessage(), is("Waiting for 10 seconds until DatabaseStartupResource[url=mock, user=mock] is available ..."));
        assertThat(logStatement.get(2).getFormattedMessage(), is("Waiting for 15 seconds until DatabaseStartupResource[url=mock, user=mock] is available ..."));
        assertThat(logStatement.get(3).getFormattedMessage(), is("DatabaseStartupResource[url=mock, user=mock] available."));
    }
}
