package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiResponseDateTimeSerializerTest {
    private final ApiResponseDateTimeSerializer serializer = new ApiResponseDateTimeSerializer();

    @Test
    public void shouldSerializeWithMillisecondPrecision() throws IOException {
        ZonedDateTime testValue = ZonedDateTime.parse("2019-01-29T11:34:53.849012345Z");
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        final SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(testValue, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        final String actual = jsonWriter.toString();
        assertEquals("\"2019-01-29T11:34:53.849Z\"", actual);
    }
}
