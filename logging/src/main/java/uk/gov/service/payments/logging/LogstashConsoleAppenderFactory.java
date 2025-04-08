package uk.gov.service.payments.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gson.Gson;
import io.dropwizard.logging.common.ConsoleAppenderFactory;
import io.dropwizard.logging.common.async.AsyncAppenderFactory;
import io.dropwizard.logging.common.filter.LevelFilterFactory;
import io.dropwizard.logging.common.layout.LayoutFactory;
import net.logstash.logback.encoder.LogstashEncoder;

import java.util.Map;

@JsonTypeName("logstash-console")
public class LogstashConsoleAppenderFactory extends ConsoleAppenderFactory<ILoggingEvent> {

    private Map<String, String> customFields;

    @Override
    public Appender<ILoggingEvent> build(LoggerContext context,
                                         String applicationName,
                                         LayoutFactory<ILoggingEvent> layout,
                                         LevelFilterFactory<ILoggingEvent> levelFilterFactory,
                                         AsyncAppenderFactory<ILoggingEvent> asyncAppenderFactory) {

        LogstashEncoder encoder = new LogstashEncoder();
        encoder.setCustomFields(new Gson().toJson(customFields));
        encoder.setContext(context);
        encoder.start();

        final ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setName("logstash-console-appender");
        appender.setContext(context);
        appender.setTarget(getTarget().get());
        appender.setEncoder(encoder);
        final ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(threshold.toString());
        filter.start();
        appender.addFilter(filter);
        appender.start();

        return wrapAsync(appender, asyncAppenderFactory);
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }
}
