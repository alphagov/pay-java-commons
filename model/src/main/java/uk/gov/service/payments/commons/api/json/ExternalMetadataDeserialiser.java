package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import uk.gov.service.payments.commons.model.charge.ExternalMetadata;

import java.io.IOException;
import java.util.Map;

public class ExternalMetadataDeserialiser extends JsonDeserializer<ExternalMetadata> {

    @Override
    public ExternalMetadata deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        if (!jsonParser.isExpectedStartObjectToken()){
            throw new JsonMappingException(jsonParser, "Field [metadata] must be an object of JSON key-value pairs");
        }

        Map<String, Object> metadata = jsonParser.getCodec().readValue(jsonParser, new TypeReference<Map<String, Object>>() {});
        if (metadata != null) {
            return metadata.isEmpty() ? null : new ExternalMetadata(metadata);
        }

        assert false : "This should never be invoked since we currently do no deserialize null values.";
        throw new JsonMappingException(jsonParser, "metadata cannot be null");
    }
}
