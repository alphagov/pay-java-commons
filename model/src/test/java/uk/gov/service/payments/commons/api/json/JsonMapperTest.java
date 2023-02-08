package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
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
    
    @Test
    public void shouldListOfString() throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree("[\"foo\",\"bar\"]");
        List<String> fromJson = jsonObjectMapper.getAsListOfString(jsonNode);
        assertThat(fromJson, contains("foo", "bar"));
    }

    @Test
    public void shouldMapToObjectMap() throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree("{\"foo\":{\"other\":\"faa\"}}");
        Map<String, Object> fromJson = jsonObjectMapper.getAsObjectMap(jsonNode);
        assertThat(fromJson, hasEntry("foo", Map.of("other", "faa")));
    }
}
