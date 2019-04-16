package uk.gov.pay.commons.api.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptySet;

public class ExternalJsonMetadataValidator implements ConstraintValidator<ValidExternalJsonMetadata, JsonNode> {

    private static final int MAXIMUM_KEYS = 10;
    private static final int MAXIMUM_VALUE_LENGTH = 50;
    private static final int MAXIMUM_KEY_LENGTH = 30;

    @Override
    public boolean isValid(JsonNode jsonNode, ConstraintValidatorContext constraintValidatorContext) {

        if (jsonNode == null) return true;

        constraintValidatorContext.disableDefaultConstraintViolation();

        Set<String> errors = new HashSet<>();
        errors.addAll(validateIsObject(jsonNode));
        errors.addAll(validateMaxNumberOfKeyPairs(jsonNode));
        errors.addAll(validateKeyLengths(jsonNode));
        errors.addAll(validateValueTypes(jsonNode));
        errors.addAll(validateValueLengths(jsonNode));

        if (errors.size() > 0) {
            errors.stream().forEach(e -> constraintValidatorContext.buildConstraintViolationWithTemplate(e).addConstraintViolation());
            return false;
        }

        return true;
    }
    
    private static Set<String> validateIsObject(JsonNode jsonNode) {
        if (!jsonNode.isObject()) return Set.of("metadata must be an object of JSON key-value pairs");
        return emptySet();
    };

    private static Set<String> validateMaxNumberOfKeyPairs(JsonNode jsonNode) {
        if (jsonNode.size() > MAXIMUM_KEYS) return Set.of(format("metadata cannot have more than %d key-value pairs", MAXIMUM_KEYS));
        return emptySet();
    };
    
    private static Set<String> validateKeyLengths(JsonNode jsonNode) {
        return Streams.stream(jsonNode.fields())
                .filter(entry -> entry.getKey() != null && keyLengthIsInvalid(entry.getKey()))
                .map(e -> format("metadata keys must be between 1 and %d characters long", MAXIMUM_KEY_LENGTH))
                .collect(Collectors.toSet());
    }

    private static Set<String> validateValueTypes(JsonNode jsonNode) {
        return Streams.stream(jsonNode.fields())
                .filter(entry -> valueTypeIsNotAllowed(entry.getValue()))
                .map(e -> format("metadata value for '%s' must be of type string, boolean or number", e.getKey()))
                .collect(Collectors.toSet());
    }

    private static Set<String> validateValueLengths(JsonNode jsonNode) {
        return Streams.stream(jsonNode.fields())
                .filter(entry -> entry.getValue().asText().length() > MAXIMUM_VALUE_LENGTH)
                .map(e -> format("metadata value for '%s' must be a maximum of %d characters", e.getKey(), MAXIMUM_VALUE_LENGTH))
                .collect(Collectors.toSet());
    }

    private static boolean keyLengthIsInvalid(String key) {
        return key.length() < 1 || key.length() > MAXIMUM_KEY_LENGTH;
    }

    private static boolean valueTypeIsNotAllowed(JsonNode value) {
        return !(value.isBoolean() || value.isNumber() || value.isTextual());
    }
}
