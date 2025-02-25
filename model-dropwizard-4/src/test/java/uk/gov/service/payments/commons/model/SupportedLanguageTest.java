package uk.gov.service.payments.commons.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SupportedLanguageTest {

    @Test
    public void enMapsToEnglish() {
        SupportedLanguage result = SupportedLanguage.fromIso639AlphaTwoCode("en");
        assertThat(result, is(SupportedLanguage.ENGLISH));
    }

    @Test
    public void cyMapsToWelsh() {
        SupportedLanguage result = SupportedLanguage.fromIso639AlphaTwoCode("cy");
        assertThat(result, is(SupportedLanguage.WELSH));
    }

    @Test
    public void frThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SupportedLanguage.fromIso639AlphaTwoCode("fr"));
    }

    @Test
    public void enUpperCaseThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SupportedLanguage.fromIso639AlphaTwoCode("EN"));
    }

    @Test
    public void englishUpperCaseThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SupportedLanguage.fromIso639AlphaTwoCode("ENGLISH"));
    }

}
