package uk.gov.service.payments.commons.model.charge;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MAX_KEY_LENGTH;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MAX_KEY_VALUE_PAIRS;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MAX_VALUE_LENGTH;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MIN_KEY_LENGTH;

public class ExternalMetadataTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String TOO_LONG_VALUE = IntStream.rangeClosed(1, MAX_VALUE_LENGTH + 1).mapToObj(i -> "a").collect(joining());
    private final String TOO_LONG_KEY = IntStream.rangeClosed(1, MAX_KEY_LENGTH + 1).mapToObj(i -> "a").collect(joining());

    @Test
    public void shouldPassValidation() {
        Map<String, Object> metadata = Map.of(
                "key1", "string",
                "key2", true,
                "key3", 123,
                "key4", 1.234,
                "key5", ""
        );
        ExternalMetadata validExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(validExternalMetadata);

        assertThat(violations.size(), is(0));
    }

    @Test
    public void shouldFailValidationWithMoreThan10Keys() {
        Map<String, Object> metadata = IntStream.rangeClosed(1, 11).boxed()
                .collect(Collectors.toUnmodifiableMap(i -> "key" + i, i-> "value" + i));
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is("Field [metadata] cannot have more than " + MAX_KEY_VALUE_PAIRS + " key-value pairs"));
    }

    @Test
    public void shouldFailValidationForKeysToLong() {
        Map<String, Object> metadata = Map.of(TOO_LONG_KEY, "string");
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("Field [metadata] keys must be between " + MIN_KEY_LENGTH + " and " + MAX_KEY_LENGTH + " characters long"));
    }

    @Test
    public void shouldFailValidationForEmptyKey() {
        Map<String, Object> metadata = Map.of("", "string");
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("Field [metadata] keys must be between " + MIN_KEY_LENGTH + " and " + MAX_KEY_LENGTH + " characters long"));
    }

    @Test
    public void shouldFailValidationForValueToLong() {
        Map<String, Object> metadata = Map.of("key1", TOO_LONG_VALUE);
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(),
                is("Field [metadata] values must be no greater than " + MAX_VALUE_LENGTH + " characters long"));
    }

    @Test
    public void shouldFailValidationWhenValueIsObject() {
        Map<String, Object> metadata = Map.of("key1", mapper.createObjectNode());
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is("Field [metadata] values must be of type String, Boolean or Number"));
    }

    @Test
    public void shouldFailValidationWhenValueIsArray() {
        Map<String, Object> metadata = Map.of("key1", mapper.createArrayNode());
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is("Field [metadata] values must be of type String, Boolean or Number"));
    }

    @Test
    public void shouldFailValidationWhenAValueIsNull() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", null);
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is("Field [metadata] must not have null values"));
    }

    @Test
    public void shouldFailWithMultipleViolations() {
        Map<String, Object> metadata = Map.of(
                TOO_LONG_KEY, "string",
                "key2", mapper.createArrayNode(),
                "key3", TOO_LONG_VALUE
        );
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);
        Set<String> expectedErrorMessages = Set.of(
                "Field [metadata] values must be of type String, Boolean or Number",
                "Field [metadata] values must be no greater than " + MAX_VALUE_LENGTH + " characters long",
                "Field [metadata] keys must be between " + MIN_KEY_LENGTH + " and " + MAX_KEY_LENGTH + " characters long");

        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);

        assertThat(violations.size(), is(3));
        assertThat(expectedErrorMessages.contains(violations.iterator().next().getMessage()), is(true));
        assertThat(expectedErrorMessages.contains(violations.iterator().next().getMessage()), is(true));
        assertThat(expectedErrorMessages.contains(violations.iterator().next().getMessage()), is(true));
    }

    @Test
    public void shouldFailWithDuplicateCaseInsensitiveKeysViolations() {
        Map<String, Object> metadata = Map.of(
                "key", "string",
                "Key", 1L
        );
        ExternalMetadata invalidExternalMetadata = new ExternalMetadata(metadata);
        Set<ConstraintViolation<ExternalMetadata>> violations = validator.validate(invalidExternalMetadata);
        assertThat(violations.size(), is(1));
        assertThat(violations.iterator().next().getMessage(), is("Field [metadata] must have case insensitive unique keys"));
    }
}
