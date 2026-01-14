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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MicrosecondPrecisionDateTimeSerializerTest {

    private final MicrosecondPrecisionDateTimeSerializer serializer = new MicrosecondPrecisionDateTimeSerializer();
    
    @Test
    public void shouldSerializeWithMicrosecondPrecision() throws IOException {
        ZonedDateTime testValue = ZonedDateTime.parse("2019-01-29T11:34:53.849012345Z");
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        final SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(testValue, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        final String serialized = jsonWriter.toString();
        assertThat(serialized, is("\"2019-01-29T11:34:53.849012Z\""));
    }
}
