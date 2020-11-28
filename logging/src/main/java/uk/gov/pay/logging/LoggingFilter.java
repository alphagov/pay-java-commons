package uk.gov.pay.logging;

import com.codahale.metrics.MetricRegistry;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.function.LongSupplier;

import static uk.gov.pay.logging.LoggingKeys.MDC_REQUEST_ID_KEY;

public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    private final MetricRegistry metricRegistry;

    private final LongSupplier timeSource;

    public LoggingFilter() {
        this(null);
    }

    public LoggingFilter(MetricRegistry metricRegistry) {
        this(metricRegistry, System::nanoTime);
    }

    protected LoggingFilter(MetricRegistry metricRegistry, LongSupplier timeSource) {
        this.metricRegistry = metricRegistry;
        this.timeSource = timeSource;
    }

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        long startTimeNs = timeSource.getAsLong();
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
            long elapsedMs = (timeSource.getAsLong() - startTimeNs) / 1_000L;
            logger.info("{} to {} ended - total time {}ms", requestMethod, requestURL, elapsedMs);
            if (metricRegistry != null)
                metricRegistry.histogram("response-times").update(elapsedMs);
        }
    }

    @Override
    public void destroy() {}
}
