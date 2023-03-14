package uk.gov.service.payments.commons.model;

public enum ErrorIdentifier {
    GENERIC,
    REFUND_NOT_AVAILABLE,
    REFUND_AMOUNT_AVAILABLE_MISMATCH,
    REFUND_NOT_AVAILABLE_DUE_TO_DISPUTE,
    ZERO_AMOUNT_NOT_ALLOWED,
    MOTO_NOT_ALLOWED,
    CANCEL_CHARGE_FAILURE_DUE_TO_CONFLICTING_TERMINAL_STATE_AT_GATEWAY_CHARGE_STATE_FORCIBLY_TRANSITIONED,
    CANCEL_CHARGE_FAILURE_DUE_TO_CONFLICTING_TERMINAL_STATE_AT_GATEWAY_INVALID_STATE_TRANSITION,
    AUTH_TOKEN_INVALID,
    AUTH_TOKEN_REVOKED,
    ACCOUNT_NOT_LINKED_WITH_PSP,
    TELEPHONE_PAYMENT_NOTIFICATIONS_NOT_ALLOWED,
    AGREEMENT_NOT_FOUND,
    ONE_TIME_TOKEN_INVALID,
    ONE_TIME_TOKEN_ALREADY_USED,
    INVALID_ATTRIBUTE_VALUE,
    CARD_NUMBER_REJECTED,
    AUTHORISATION_API_NOT_ALLOWED,
    AUTHORISATION_REJECTED,
    AUTHORISATION_ERROR,
    AUTHORISATION_TIMEOUT,
    AGREEMENT_NOT_ACTIVE,
    INCORRECT_AUTHORISATION_MODE_FOR_SAVE_PAYMENT_INSTRUMENT_TO_AGREEMENT,
    MISSING_MANDATORY_ATTRIBUTE,
    UNEXPECTED_ATTRIBUTE,
    ACCOUNT_DISABLED,
    RECURRING_CARD_PAYMENTS_NOT_ALLOWED,
    IDEMPOTENCY_KEY_USED
}
