package uk.gov.pay.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import java.util.function.Supplier;

import static java.lang.String.format;
import static net.logstash.logback.argument.StructuredArguments.kv;
import static uk.gov.pay.logging.LoggingKeys.HTTP_STATUS;
import static uk.gov.pay.logging.LoggingKeys.METHOD;
import static uk.gov.pay.logging.LoggingKeys.RESPONSE_TIME;
import static uk.gov.pay.logging.LoggingKeys.URL;

public class RestClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(RestClientLoggingFilter.class);
    private static final String HEADER_REQUEST_ID = "X-Request-Id";
    private static final ThreadLocal<String> requestId = new ThreadLocal<>();
    private static final ThreadLocal<Long> startTimeNs = new ThreadLocal<>();

    private final Supplier<Long> timeSource;

    public RestClientLoggingFilter() {
        this.timeSource = System::nanoTime;
    }

    protected RestClientLoggingFilter(Supplier<Long> timeSource) {
        this.timeSource = timeSource;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        startTimeNs.set(timeSource.get());
        requestId.set(StringUtils.defaultString(MDC.get(LoggingKeys.MDC_REQUEST_ID_KEY)));

        requestContext.getHeaders().add(HEADER_REQUEST_ID, requestId.get());
        logger.info(format("[%s] - %s to %s began",
                requestId.get(),
                requestContext.getMethod(),
                requestContext.getUri(),
                kv(METHOD, requestContext.getMethod()),
                kv(URL, requestContext.getUri())));

    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        long elapsedMs = (timeSource.get() - startTimeNs.get()) / 1_000L;
        responseContext.getHeaders().add(HEADER_REQUEST_ID, requestId.get());
        logger.info(format("[%s] - %s to %s ended - total time %dms",
                requestId.get(),
                requestContext.getMethod(),
                requestContext.getUri(),
                elapsedMs),
                kv(METHOD, requestContext.getMethod()),
                kv(URL, requestContext.getUri()),
                kv(HTTP_STATUS, responseContext.getStatus()),
                kv(RESPONSE_TIME, elapsedMs));

        requestId.remove();
        startTimeNs.remove();
    }
}
