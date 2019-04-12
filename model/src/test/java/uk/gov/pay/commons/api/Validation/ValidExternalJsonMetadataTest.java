package uk.gov.pay.commons.api.Validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.gov.pay.commons.api.validation.ValidExternalJsonMetadata;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidExternalJsonMetadataTest {

    private static Validator validator;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void shouldPassValidation() {
        ObjectNode validMetadata = objectMapper.createObjectNode();
        validMetadata.put("key1", "value1");
        validMetadata.put("key2", 123);
        validMetadata.put("key3", true);
        validMetadata.put("key4", "value4");
        TestClass aTestClass = new TestClass(validMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(0));
    }

    @Test
    public void shouldPassForNullValue() {
        TestClass aTestClass = new TestClass(null);
        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(0));
    }

    @Test
    public void shouldFailValidationMultipleInvalidValueTypes() {
        JsonNode nestedJsonObject = objectMapper.createObjectNode();
        JsonNode nestedJsonArray = objectMapper.createArrayNode();
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        invalidMetadata.set("key1", nestedJsonObject);
        invalidMetadata.set("key2", nestedJsonArray);
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<String> expectedErrors = Set.of(
                "value for 'key1' must be of type string, boolean or number",
                "value for 'key2' must be of type string, boolean or number");

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(2));
        assertThat(expectedErrors.contains(violationSet.iterator().next().getMessage()), is(true));
        assertThat(expectedErrors.contains(violationSet.iterator().next().getMessage()), is(true));
    }

    @Test
    public void shouldFailValidationForTooManyKeys() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        int maximumKeysAllowed = 10;
        for (int i = 0; i < maximumKeysAllowed + 5; i++) {
            invalidMetadata.put("key" + i, "value" + i);
        }
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("cannot have more than " + maximumKeysAllowed + " key-value pairs"));
    }

    @Test
    public void shouldFailValidationKeyTooLong() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        String tooLongKeyName = "this key is over thirty characters long and is invalid";
        invalidMetadata.put(tooLongKeyName, "value1");
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("keys must be between 1 and 30 characters long"));
    }

    @Test
    public void shouldFailValidationEmptyKey() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        invalidMetadata.put("", "value1");
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("keys must be between 1 and 30 characters long"));
    }

    @Test
    public void shouldFailValidationValueTooLong() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        String tooLongValue = "this string is over fifty characters long by the time I've finished typing";
        invalidMetadata.put("key1", tooLongValue);
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("value for 'key1' must be a maximum of 50 characters"));
    }

    @Test
    public void shouldFailValidationForArrayNode() {
        JsonNode invalidMetadata = objectMapper.createArrayNode();
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("must be an object of JSON key-value pairs"));
    }

    @Test
    public void shouldFailValidationForNullKey() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        invalidMetadata.put(null, "someValue");
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("keys must not be null"));
    }

    @Test
    public void shouldFailValidationForNullKeyAndInvalidValue() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        invalidMetadata.set(null, objectMapper.createArrayNode());
        Set<String> expectedErrorMessages = Set.of(
                "keys must not be null",
                "value for 'null' must be of type string, boolean or number");

        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(2));
        assertThat(expectedErrorMessages.contains(violationSet.iterator().next().getMessage()), is(true));
        assertThat(expectedErrorMessages.contains(violationSet.iterator().next().getMessage()), is(true));
    }

    @Test
    public void shouldFailValidationForNullValue() {
        ObjectNode invalidMetadata = objectMapper.createObjectNode();
        invalidMetadata.set("key1", (JsonNode) null);
        TestClass aTestClass = new TestClass(invalidMetadata);

        Set<ConstraintViolation<TestClass>> violationSet = validator.validate(aTestClass);
        assertThat(violationSet.size(), is(1));
        assertThat(violationSet.iterator().next().getMessage(), is("value for 'key1' must be of type string, boolean or number"));
    }

    public static class TestClass {

        @ValidExternalJsonMetadata()
        private final JsonNode metadata;

        TestClass(JsonNode metadata) {
            this.metadata = metadata;
        }
    }
}
