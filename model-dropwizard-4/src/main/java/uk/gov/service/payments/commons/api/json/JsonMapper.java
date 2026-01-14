package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class JsonMapper {
    private ObjectMapper objectMapper;
    
    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, String> getAsMap(JsonNode jsonNode) {
        if (jsonNode != null) {
            if ((jsonNode.isTextual() && !isEmpty(jsonNode.asText())) || (!jsonNode.isNull() && jsonNode.isObject())) {
                try {
                    return objectMapper.readValue(jsonNode.traverse(), new TypeReference<Map<String, String>>() {});
                } catch (IOException e) {
                    throw new RuntimeException("Malformed JSON object in value", e);
                }
            }
        }
        return null;
    }    
    
    public List<String> getAsListOfString(JsonNode jsonNode) {
        if (jsonNode != null) {
            if (jsonNode.isArray()) {
                try {
                    return objectMapper.readValue(jsonNode.traverse(), new TypeReference<List<String>>() {});
                } catch (IOException e) {
                    throw new RuntimeException("Malformed JSON object in value", e);
                }
            }
        }
        return null;
    }
}
