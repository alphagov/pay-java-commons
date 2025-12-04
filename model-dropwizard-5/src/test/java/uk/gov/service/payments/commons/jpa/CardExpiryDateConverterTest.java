package uk.gov.service.payments.commons.jpa;

import org.junit.jupiter.api.Test;
import uk.gov.service.payments.commons.model.CardExpiryDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class CardExpiryDateConverterTest {

    private final CardExpiryDateConverter cardExpiryDateConverter = new CardExpiryDateConverter();

    @Test
    void convertsCardExpiryDateToString() {
        var cardExpiryDate = CardExpiryDate.valueOf("09/22");
        String result = cardExpiryDateConverter.convertToDatabaseColumn(cardExpiryDate);
        assertThat(result, is("09/22"));
    }

    @Test
    void convertsNullCardExpiryDateToNull() {
        String result = cardExpiryDateConverter.convertToDatabaseColumn(null);
        assertThat(result, is(nullValue()));
    }

    @Test
    void convertsStringToCardExpiryDate() {
        CardExpiryDate result = cardExpiryDateConverter.convertToEntityAttribute("09/22");
        assertThat(result, is(CardExpiryDate.valueOf("09/22")));
    }

    @Test
    void convertsNullStringToNull() {
        CardExpiryDate result = cardExpiryDateConverter.convertToEntityAttribute(null);
        assertThat(result, is(nullValue()));
    }

}
