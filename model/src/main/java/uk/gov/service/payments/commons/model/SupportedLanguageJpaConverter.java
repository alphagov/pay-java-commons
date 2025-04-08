package uk.gov.service.payments.commons.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SupportedLanguageJpaConverter implements AttributeConverter<SupportedLanguage, String> {

    @Override
    public String convertToDatabaseColumn(SupportedLanguage supportedLanguage) {
        return supportedLanguage.toString();
    }

    @Override
    public SupportedLanguage convertToEntityAttribute(String supportedLanguage) {
        return SupportedLanguage.fromIso639AlphaTwoCode(supportedLanguage);
    }

}
