package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MicrosecondPrecisionDateTimeDeserializerTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new MicrosecondPrecisionDateTimeDeserializer());
        objectMapper.registerModule(simpleModule);
    }

    @Test
    public void shouldDeserializeValidString() throws IOException {
        String testValue = "{\"created_date\":\"2019-01-29T11:34:53.849012Z\"}";

        TestMicrosecondDeserializerObject deserialized = objectMapper.readValue(testValue, TestMicrosecondDeserializerObject.class);
        ZonedDateTime expected = ZonedDateTime.parse("2019-01-29T11:34:53.849012Z");
        assertThat(deserialized.getCreatedDate(), is(expected));
    }

    @Test
    public void shouldDeserializeToNull() throws IOException {
        String testValue = "{\"created_date\":null}";

        TestMicrosecondDeserializerObject deserialized = objectMapper.readValue(testValue, TestMicrosecondDeserializerObject.class);
        assertThat(deserialized.getCreatedDate(), is(nullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenNullValue() {
        String testValue = "{\"created_date\":\"blah\"}";
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(testValue, TestMicrosecondDeserializerObject.class));
    }
}

class TestMicrosecondDeserializerObject {
    @JsonProperty(value = "created_date")
    @JsonDeserialize(using = MicrosecondPrecisionDateTimeDeserializer.class)
    private ZonedDateTime createdDate;

    ZonedDateTime getCreatedDate() {
        return createdDate;
    }
}


