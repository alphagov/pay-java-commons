package uk.gov.service.payments.commons.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public final class SupportedLanguageJsonDeserializer extends JsonDeserializer<SupportedLanguage> {
    @Override
    public SupportedLanguage deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final String valueAsString = parser.getValueAsString();
        return SupportedLanguage.fromIso639AlphaTwoCode(valueAsString);
    }
}
