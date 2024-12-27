package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class IsoInstantMillisecondSerializerTest {

    private final IsoInstantMillisecondSerializer isoInstantMillisecondSerializer = new IsoInstantMillisecondSerializer();

    @Test
    void shouldSerializeWithMillisecondPrecision() throws IOException {
        var instant = Instant.parse("2020-12-25T15:00:00.123456789Z");

        var stringWriter = new StringWriter();
        var jsonGenerator = new JsonFactory().createGenerator(stringWriter);

        isoInstantMillisecondSerializer.serialize(instant, jsonGenerator, new ObjectMapper().getSerializerProvider());
        jsonGenerator.flush();

        var expected = "\"2020-12-25T15:00:00.123Z\"";
        var actual = stringWriter.toString();

        assertThat(actual, is(expected));
    }

}
