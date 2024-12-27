package uk.gov.service.payments.logging;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Stopwatch;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.TimeUnit;

import jakarta.servlet.Filter;

import static uk.gov.service.payments.logging.LoggingKeys.MDC_REQUEST_ID_KEY;

public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    private final MetricRegistry metricRegistry;

    public LoggingFilter() {
        this.metricRegistry = null;
    }

    public LoggingFilter(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void init(FilterConfig filterConfig) { /* empty */ }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String requestURL = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();
        String requestId = httpRequest.getHeader("X-Request-Id");

        if (requestId == null) {
            MDC.remove(MDC_REQUEST_ID_KEY);
        } else {
            MDC.put(MDC_REQUEST_ID_KEY, requestId);
        }

        logger.info("{} to {} began", requestMethod, requestURL);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Throwable throwable) {
            logger.error("Exception - {}", throwable.getMessage(), throwable);
        } finally {
            long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            logger.info("{} to {} ended - total time {}ms", requestMethod, requestURL, elapsed);
            if (metricRegistry != null)
                metricRegistry.histogram("response-times").update(elapsed);
        }
    }

    @Override
    public void destroy() { /* empty */ }
}
