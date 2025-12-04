package uk.gov.service.payments.commons.model;

public enum TokenPaymentType {
    CARD("Card Payment"), DIRECT_DEBIT("Direct Debit Payment");

    private String friendlyName;

    TokenPaymentType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public static TokenPaymentType fromString(final String type) {
        try {
            return TokenPaymentType.valueOf(type);
        } catch (Exception e) {
            return CARD;
        }
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }
}
