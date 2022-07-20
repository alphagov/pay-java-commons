package uk.gov.service.payments.commons.model.agreement;

import java.util.Arrays;
import java.util.Optional;

public enum AgreementStatus {
    CREATED,
    ACTIVE,
    CANCELLED,
    EXPIRED;

    public static Optional<AgreementStatus> from(String agreementStatusName) {
        return Arrays.stream(AgreementStatus.values())
                .filter(v -> v.name().equals(agreementStatusName))
                .findFirst();
    }
}
