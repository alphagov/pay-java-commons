package uk.gov.service.payments.logging;

import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GovUkPayDropwizardRequestJsonLogLayoutFactoryTest {
    
    @Test
    public void throw_runtime_exception_if_no_additional_field_named_container() {
        var jsonLogLayoutFactory = new GovUkPayDropwizardRequestJsonLogLayoutFactory();
        assertThrows(RuntimeException.class, () -> jsonLogLayoutFactory.build(null, TimeZone.getDefault()));
    }
}
