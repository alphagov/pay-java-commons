package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/**
 * Custom JSON Deserializer that will try to parse a String into a ZonedDateTime
 */
public class ApiResponseDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    public ApiResponseDateTimeDeserializer() {
        this(null);
    }

    public ApiResponseDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        final String dateString = jsonparser.getText();
        return ZonedDateTime.parse(dateString, ISO_ZONED_DATE_TIME).withZoneSameInstant(UTC);
    }
}
