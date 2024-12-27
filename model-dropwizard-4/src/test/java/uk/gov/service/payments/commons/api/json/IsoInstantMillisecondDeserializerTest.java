package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IsoInstantMillisecondDeserializerTest {

    private record JsonObjectWithInstant(
            @JsonDeserialize(using = IsoInstantMillisecondDeserializer.class) Instant instant) { }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeIsoInstantStringWithSecondsWith3DecimalPlacesToInstant() throws JsonProcessingException {
        var jsonString = """
                {
                  "instant": "2024-10-02T09:30:00.123Z"
                }
                """;

        JsonObjectWithInstant deserializedJsonObject = objectMapper.readValue(jsonString, JsonObjectWithInstant.class);

        assertThat(deserializedJsonObject.instant(), is(Instant.parse("2024-10-02T09:30:00.123Z")));
    }

    @Test
    void shouldDeserializeIsoInstantStringWithSecondsWith3DecimalPlacesOfZeroToInstant() throws JsonProcessingException {
        var jsonString = """
                {
                  "instant": "2024-10-02T09:30:00.000Z"
                }
                """;

        JsonObjectWithInstant deserializedJsonObject = objectMapper.readValue(jsonString, JsonObjectWithInstant.class);

        assertThat(deserializedJsonObject.instant(), is(Instant.parse("2024-10-02T09:30:00.000Z")));
    }

    @Test
    void shouldDeserializeNullToNull() throws JsonProcessingException {
        var jsonString = """
                {
                  "instant": null
                }
                """;

        JsonObjectWithInstant deserializedJsonObject = objectMapper.readValue(jsonString, JsonObjectWithInstant.class);

        assertThat(deserializedJsonObject.instant(), is(nullValue()));
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeserializeIsoStringWith2DecimalPlaces() {
        var jsonString = """
                {
                  "instant": "2024-10-02T09:30:00.12Z"
                }
                """;

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonString, JsonObjectWithInstant.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeserializeIsoStringWith4DecimalPlaces() {
        var jsonString = """
                {
                  "instant": "2024-10-02T09:30:00.1234Z"
                }
                """;

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonString, JsonObjectWithInstant.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeserializeIsoStringWith6DecimalPlaces() {
        var jsonString = """
                {
                  "instant": "2024-10-02T09:30:00.123456Z"
                }
                """;

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonString, JsonObjectWithInstant.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeserializeIsoStringWith6DecimalPlacesButNoTrailingZ() {
        var jsonString = """
                {
                  "instant": "2024-10-02T09:30:00.123"
                }
                """;

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonString, JsonObjectWithInstant.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeserializeNonsenseString() {
        var jsonString = """
                {
                  "instant": "This is clearly not an ISO instant"
                }
                """;

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonString, JsonObjectWithInstant.class));
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeserializeNonString() {
        var jsonString = """
                {
                  "instant": 1727861400123
                }
                """;

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonString, JsonObjectWithInstant.class));
    }

}
