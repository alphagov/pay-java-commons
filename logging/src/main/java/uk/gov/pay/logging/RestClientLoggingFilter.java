package uk.gov.pay.logging;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.util.concurrent.TimeUnit;
import java.util.Optional;

import static java.lang.String.format;
import static net.logstash.logback.argument.StructuredArguments.kv;
import static uk.gov.pay.logging.LoggingKeys.HTTP_STATUS;
import static uk.gov.pay.logging.LoggingKeys.METHOD;
import static uk.gov.pay.logging.LoggingKeys.RESPONSE_TIME;
import static uk.gov.pay.logging.LoggingKeys.URL;

public class RestClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(RestClientLoggingFilter.class);
    private static final String HEADER_REQUEST_ID = "X-Request-Id";
    private static ThreadLocal<String> requestId = new ThreadLocal<>();
    private static ThreadLocal<Stopwatch> timer = new ThreadLocal<>();

    @Override
    public void filter(ClientRequestContext requestContext) {
        timer.set(Stopwatch.createStarted());
        requestId.set(Optional.ofNullable(MDC.get(LoggingKeys.MDC_REQUEST_ID_KEY)).orElse(""));

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
        long elapsed = timer.get().elapsed(TimeUnit.MILLISECONDS);
        responseContext.getHeaders().add(HEADER_REQUEST_ID, requestId.get());
        logger.info(format("[%s] - %s to %s ended - total time %dms",
                requestId.get(),
                requestContext.getMethod(),
                requestContext.getUri(),
                elapsed),
                kv(METHOD, requestContext.getMethod()),
                kv(URL, requestContext.getUri()),
                kv(HTTP_STATUS, responseContext.getStatus()),
                kv(RESPONSE_TIME, elapsed));

        requestId.remove();
        timer.get().stop();
        timer.remove();
    }
}
