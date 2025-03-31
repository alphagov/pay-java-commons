package uk.gov.service.payments.commons.model.agreement;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentInstrumentType {
    CARD;

    public static Optional<PaymentInstrumentType> from(String paymentInstrumentTypeName) {
        return Arrays.stream(PaymentInstrumentType.values())
                .filter(v -> v.name().equals(paymentInstrumentTypeName))
                .findFirst();
    }
}
