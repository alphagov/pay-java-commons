package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class JsonMapperTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonMapper jsonObjectMapper = new JsonMapper(objectMapper);
    
    @Test
    public void shouldMapToMap() throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree("{\"foo\":\"bar\"}");
        Map<String, String> fromJson = jsonObjectMapper.getAsMap(jsonNode);
        assertThat(fromJson, hasEntry("foo", "bar"));
    }
}
