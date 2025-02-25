package uk.gov.service.payments.commons.model;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TokenPaymentTypeTest {

    @Test
    public void cardFromStringMapsToCard() {
        TokenPaymentType result = TokenPaymentType.fromString("CARD");
        assertThat(result, is(TokenPaymentType.CARD));
    }

    @Test
    public void direct_debitFromStringMapsToDirectDebit() {
        TokenPaymentType result = TokenPaymentType.fromString("DIRECT_DEBIT");
        assertThat(result, is(TokenPaymentType.DIRECT_DEBIT));
    }

    @Test
    public void cashMapsToCard() {
        TokenPaymentType result = TokenPaymentType.fromString("CASH");
        assertThat(result, is(TokenPaymentType.CARD));
    }

    @Test
    public void cardGetFriendlyNameMapsToCardPayment() {
        String result = TokenPaymentType.fromString("CARD").getFriendlyName();
        assertThat(result, is("Card Payment"));
    }

    @Test
    public void direct_debitGetFriendlyNameMapsToDirectDebitPayment() {
        String result = TokenPaymentType.fromString("DIRECT_DEBIT").getFriendlyName();
        assertThat(result, is("Direct Debit Payment"));
    }
}
