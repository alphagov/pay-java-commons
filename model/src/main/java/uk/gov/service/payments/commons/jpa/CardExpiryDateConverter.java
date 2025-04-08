package uk.gov.service.payments.commons.jpa;

import uk.gov.service.payments.commons.model.CardExpiryDate;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CardExpiryDateConverter implements AttributeConverter<CardExpiryDate, String> {

    @Override
    public String convertToDatabaseColumn(CardExpiryDate cardExpiryDate) {
        if (cardExpiryDate == null) {
            return null;
        }

        return cardExpiryDate.toString();
    }

    @Override
    public CardExpiryDate convertToEntityAttribute(String cardExpiryDate) {
        if (cardExpiryDate == null) {
            return null;
        }

        return CardExpiryDate.valueOf(cardExpiryDate);
    }

}
