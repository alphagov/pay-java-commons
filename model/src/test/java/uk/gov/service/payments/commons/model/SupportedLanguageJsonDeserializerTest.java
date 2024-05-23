package uk.gov.service.payments.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SupportedLanguageJsonDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void deserialize_shouldDeserializeToEnglish() throws IOException {
        String json = "{\"language\" : \"en\"}";
        SupportedLanguageJson fromJson = mapper.readValue(json, SupportedLanguageJson.class);
        assertNotNull(fromJson);
        assertThat(fromJson.getLanguage(), CoreMatchers.is(SupportedLanguage.ENGLISH));
    }

    @Test
    public void deserialize_shouldDeserializeToWelsh() throws IOException {
        String json = "{\"language\" : \"cy\"}";
        SupportedLanguageJson fromJson = mapper.readValue(json, SupportedLanguageJson.class);
        assertNotNull(fromJson);
        assertThat(fromJson.getLanguage(), CoreMatchers.is(SupportedLanguage.WELSH));
    }

    @Test
    public void deserialize_shouldThrowExceptionForUnsupportedLanguage() {
        String json = "{\"language\" : \"ab\"}";
        try {
            mapper.readValue(json, SupportedLanguageJson.class);
        } catch (IOException e) {
            assertThat(e, instanceOf(JsonMappingException.class));
            assertThat(e.getCause(), instanceOf(IllegalArgumentException.class));
            assertThat(e.getMessage(), containsString("ab is not a supported ISO 639-1 code"));
        }
    }

    @Test
    public void deserialize_shouldThrowExceptionForEmptyString() {
        String json = "{\"language\" : \"\"}";
        try {
            mapper.readValue(json, SupportedLanguageJson.class);
        } catch (IOException e) {
            assertThat(e, instanceOf(JsonMappingException.class));
            assertThat(e.getCause(), instanceOf(IllegalArgumentException.class));
            assertThat(e.getMessage(), containsString(" is not a supported ISO 639-1 code"));
        }
    }
}

class SupportedLanguageJson {
    @JsonDeserialize(using = SupportedLanguageJsonDeserializer.class)
    @JsonProperty("language")
    private SupportedLanguage language;

    SupportedLanguage getLanguage() {
        return this.language;
    }
}
