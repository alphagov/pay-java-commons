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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApiResponseDateTimeDeserializerTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new ApiResponseDateTimeDeserializer());
        objectMapper.registerModule(simpleModule);
    }

    @Test
    public void shouldDeserializeValidString() throws IOException {
        String testValue = "{\"created_date\":\"2019-01-29T11:34:53.849012345Z\"}";

        RequestDateTimeJsonTest actual = objectMapper.readValue(testValue, RequestDateTimeJsonTest.class);
        ZonedDateTime expected = ZonedDateTime.parse("2019-01-29T11:34:53.849012345Z");
        assertEquals(expected, actual.getCreatedDate());
    }

    @Test
    public void shouldDeserializeToNull() throws IOException {
        String testValue = "{\"created_date\":null}";

        RequestDateTimeJsonTest response = objectMapper.readValue(testValue, RequestDateTimeJsonTest.class);
        assertNull(response.getCreatedDate());
    }

    @Test
    public void shouldThrowExceptionWhenNullValue() {
        String testValue = "{\"created_date\":\"blah\"}";
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(testValue, RequestDateTimeJsonTest.class));
    }
}

class RequestDateTimeJsonTest {
    @JsonProperty(value = "created_date")
    @JsonDeserialize(using = ApiResponseDateTimeDeserializer.class)
    private ZonedDateTime createdDate;

    ZonedDateTime getCreatedDate() {
        return createdDate;
    }
}
