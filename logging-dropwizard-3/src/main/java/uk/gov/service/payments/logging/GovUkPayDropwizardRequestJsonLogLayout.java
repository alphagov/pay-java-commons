package uk.gov.service.payments.logging;

import ch.qos.logback.access.spi.IAccessEvent;
import io.dropwizard.logging.json.layout.AbstractJsonLayout;
import io.dropwizard.logging.json.layout.JsonFormatter;
import io.dropwizard.logging.json.layout.TimestampFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static uk.gov.service.payments.logging.LoggingKeys.HTTP_STATUS;
import static uk.gov.service.payments.logging.LoggingKeys.MDC_REQUEST_ID_KEY;
import static uk.gov.service.payments.logging.LoggingKeys.METHOD;
import static uk.gov.service.payments.logging.LoggingKeys.REMOTE_ADDRESS;
import static uk.gov.service.payments.logging.LoggingKeys.RESPONSE_TIME;
import static uk.gov.service.payments.logging.LoggingKeys.URL;

public class GovUkPayDropwizardRequestJsonLogLayout extends AbstractJsonLayout<IAccessEvent> {

    private static final String HEADER_REQUEST_ID = "X-Request-Id";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";

    private final int logVersion;
    private final Map<String, Object> additionalFields;
    private final TimestampFormatter timestampFormatter;

    public GovUkPayDropwizardRequestJsonLogLayout(JsonFormatter jsonFormatter,
                                                  TimestampFormatter timestampFormatter,
                                                  int logVersion,
                                                  Map<String, Object> additionalFields) {
        super(jsonFormatter);
        this.timestampFormatter = timestampFormatter;
        this.logVersion = logVersion;
        this.additionalFields = additionalFields;
    }

    @Override
    protected Map<String, Object> toJsonMap(IAccessEvent event) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("@version", logVersion);
        jsonMap.put(RESPONSE_TIME, event.getElapsedTime());
        jsonMap.put(HTTP_STATUS, event.getStatusCode());
        jsonMap.put("content_length", event.getContentLength());
        putIfNotNull(jsonMap, "@timestamp", formatTimeStamp(event.getTimeStamp()));
        putIfNotNull(jsonMap, URL, event.getRequestURI());
        putIfNotNull(jsonMap, METHOD, event.getMethod());
        putIfNotNull(jsonMap,"http_version", event.getProtocol());
        putIfNotNull(jsonMap, "user_agent", event.getRequestHeader(HEADER_USER_AGENT));
        putIfNotNull(jsonMap, MDC_REQUEST_ID_KEY, event.getRequestHeader(HEADER_REQUEST_ID));

        Optional.ofNullable(event.getRequestHeader(HEADER_FORWARDED_FOR))
                .map(forwarded -> forwarded.split(",")[0])
                .ifPresent(clientAddress -> jsonMap.put(REMOTE_ADDRESS, clientAddress));
        
        jsonMap.putAll(additionalFields);
        
        return jsonMap;
    }

    private Object formatTimeStamp(long timeStamp) {
        if (timeStamp > 0) {
            return timestampFormatter.format(timeStamp);
        }

        return null;
    }

    private void putIfNotNull(Map<String, Object> jsonMap, String key, Object value) {
        if (value != null) {
            jsonMap.put(key, value);
        }
    }
}
