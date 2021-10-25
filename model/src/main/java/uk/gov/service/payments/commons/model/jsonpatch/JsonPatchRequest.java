package uk.gov.service.payments.commons.model.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.service.payments.commons.api.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static uk.gov.service.payments.commons.model.jsonpatch.JsonPatchKeys.FIELD_OPERATION;
import static uk.gov.service.payments.commons.model.jsonpatch.JsonPatchKeys.FIELD_OPERATION_PATH;
import static uk.gov.service.payments.commons.model.jsonpatch.JsonPatchKeys.FIELD_VALUE;

public class JsonPatchRequest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonMapper jsonObjectMapper = new JsonMapper(objectMapper);
    
    private final JsonPatchOp op;
    private final String path;
    private final JsonNode value;

    public JsonPatchOp getOp() {
        return op;
    }

    public String getPath() {
        return path;
    }
    
    public boolean valueIsString() { 
        return value.isTextual();
    }

    public boolean valueIsArray() {
        return value.isArray();
    }

    public boolean valueIsBoolean() {
        return value.isBoolean();
    }

    public String valueAsString() {
        return value.asText();
    }

    public List valueAsList() {
        return objectMapper.convertValue(value, List.class);
    }
    
    public long valueAsLong() {
        if (value != null && value.isNumber()) {
            return Long.parseLong(value.asText());
        }
        throw new JsonNodeNotCorrectTypeException("JSON node " + value + " is not of type number");
    }
    
    public int valueAsInt() {
        if(value != null && value.isNumber()) {
            return Integer.parseInt(value.asText());
        }
        throw new JsonNodeNotCorrectTypeException("JSON node " + value + " is not of type number");
    }

    public boolean valueAsBoolean() {
        if (value != null && value.isBoolean()) {
            return Boolean.parseBoolean(value.asText());
        }
        throw new JsonNodeNotCorrectTypeException("JSON node " + value + " is not of type boolean");
    }

    public Map<String, String> valueAsObject() {
        return jsonObjectMapper.getAsMap(value);
    }


    private JsonPatchRequest(JsonPatchOp op, String path, JsonNode value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public static JsonPatchRequest from(JsonNode payload) {
        return new JsonPatchRequest(
                JsonPatchOp.valueOf(payload.get(FIELD_OPERATION).asText().toUpperCase()),
                payload.get(FIELD_OPERATION_PATH).asText(),
                payload.get(FIELD_VALUE));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonPatchRequest that = (JsonPatchRequest) o;
        return op == that.op && Objects.equals(path, that.path) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, path, value);
    }

    public class JsonNodeNotCorrectTypeException extends RuntimeException {
        public JsonNodeNotCorrectTypeException(String message) {
            super(message);
        }
    }
}
