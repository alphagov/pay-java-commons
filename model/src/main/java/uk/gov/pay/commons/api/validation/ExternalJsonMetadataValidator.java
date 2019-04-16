package uk.gov.pay.commons.api.validation;

import com.fasterxml.jackson.databind.JsonNode;
import fj.data.List;
import fj.data.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.BiFunction;

import static fj.data.List.arrayList;
import static java.lang.String.format;

public class ExternalJsonMetadataValidator implements ConstraintValidator<ValidExternalJsonMetadata, JsonNode> {

    private static final int MAXIMUM_KEYS = 10;
    private static final int MAXIMUM_VALUE_LENGTH = 50;
    private static final int MAXIMUM_KEY_LENGTH = 30;

    private static final BiFunction<JsonNode, List<String>, List<String>> VALIDATE_KEY_VALUE_PAIRS = (jsonNode, errors) -> {
        if (jsonNode.isArray()) return arrayList("must be an object of JSON key-value pairs").append(errors);
        return errors;
    };

    private static final BiFunction<JsonNode, List<String>, List<String>> VALIDATE_MAX_NUMBER_OF_KEY_PAIRS = (jsonNode, errors) -> {
        if (jsonNode.size() > MAXIMUM_KEYS)
            return arrayList(format("cannot have more than %d key-value pairs", MAXIMUM_KEYS));
        return errors;
    };

    private static final BiFunction<JsonNode, List<String>, List<String>> VALIDATE_KEYS_ARE_NOT_NULL = (jsonNode, errors) -> {
        var blankOrNullKeys = Stream.iteratorStream(jsonNode.fields())
                .filter(entry -> entry.getKey() == null)
                .toList();
        if (blankOrNullKeys.length() > 0) return arrayList("keys must not be null").append(errors);
        return errors;
    };

    private static final BiFunction<JsonNode, List<String>, List<String>> VALIDATE_KEY_LENGTHS = (jsonNode, errors) ->
            Stream.iteratorStream(jsonNode.fields())
                    .filter(entry -> entry.getKey() != null && keyLengthIsInvalid(entry.getKey()))
                    .foldLeft((accumulatedErrors, entry) ->
                                    arrayList(format("keys must be between 1 and %d characters long", MAXIMUM_KEY_LENGTH)).append(accumulatedErrors),
                            errors);

    private static final BiFunction<JsonNode, List<String>, List<String>> VALIDATE_VALUE_TYPES = (jsonNode, errors) ->
            Stream.iteratorStream(jsonNode.fields())
                    .filter(entry -> valueTypeIsNotAllowed(entry.getValue()))
                    .foldLeft((accumulatedErrors, entry) ->
                                    arrayList(format("value for '%s' must be of type string, boolean or number", entry.getKey())).append(accumulatedErrors),
                            errors);

    private static final BiFunction<JsonNode, List<String>, List<String>> validateValueLengths = (jsonNode, errors) ->
            Stream.iteratorStream(jsonNode.fields())
                    .filter(entry -> entry.getValue().asText().length() > MAXIMUM_VALUE_LENGTH)
                    .foldLeft((accumulatedErrors, entry) ->
                                    arrayList(format("value for '%s' must be a maximum of %d characters", entry.getKey(), MAXIMUM_VALUE_LENGTH)).append(accumulatedErrors),
                            errors);

    @Override
    public boolean isValid(JsonNode jsonNode, ConstraintValidatorContext constraintValidatorContext) {

        if (jsonNode == null) return true;

        constraintValidatorContext.disableDefaultConstraintViolation();

        List<String> errors =
                validateValueLengths.apply(jsonNode,
                        VALIDATE_VALUE_TYPES.apply(jsonNode,
                                VALIDATE_KEY_LENGTHS.apply(jsonNode,
                                        VALIDATE_KEYS_ARE_NOT_NULL.apply(jsonNode,
                                                VALIDATE_MAX_NUMBER_OF_KEY_PAIRS.apply(jsonNode,
                                                        VALIDATE_KEY_VALUE_PAIRS.apply(jsonNode, arrayList()))))));

        if (errors.length() > 0) {
            errors.toStream().forEach(e -> constraintValidatorContext.buildConstraintViolationWithTemplate(e).addConstraintViolation());
            return false;
        }

        return true;
    }

    private static boolean keyLengthIsInvalid(String key) {
        return key.length() < 1 || key.length() > MAXIMUM_KEY_LENGTH;
    }

    private static boolean valueTypeIsNotAllowed(JsonNode value) {
        return !(value.isBoolean() || value.isNumber() || value.isTextual());
    }
}
