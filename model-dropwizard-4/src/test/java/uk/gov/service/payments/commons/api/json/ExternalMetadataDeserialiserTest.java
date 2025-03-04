package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.service.payments.commons.model.charge.ExternalMetadata;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ExternalMetadataDeserialiserTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ExternalMetadata.class, new ExternalMetadataDeserialiser());
        objectMapper.registerModule(simpleModule);
    }
    
    @Test
    public void shouldReturnNullIfValueIsAnEmptyJson() throws Exception {
        ExternalMetadata externalMetadata = objectMapper.readValue("{}", ExternalMetadata.class);
        assertNull(externalMetadata);
    }
    
    @Test
    public void shouldDeserialise() throws Exception {
        String metadata = "{\"ledger_code\":\"123\", \"fund_code\":\"1234\"}";
        ExternalMetadata externalMetadata = objectMapper.readValue(metadata, ExternalMetadata.class);
        assertNotNull(externalMetadata.getMetadata());
        assertEquals(externalMetadata.getMetadata().size(), 2);
        assertEquals(externalMetadata.getMetadata().get("ledger_code"), "123");
        assertEquals(externalMetadata.getMetadata().get("fund_code"), "1234");
    }

    @Test
    public void shouldReturnErrorIfValueIsNotAnObject() {
        String metadata = "some text";
        try {
            objectMapper.convertValue(metadata, ExternalMetadata.class);
            fail();
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("Field [metadata] must be an object of JSON key-value pairs"));
        }
    }
}
