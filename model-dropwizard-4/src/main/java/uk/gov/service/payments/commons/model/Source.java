package uk.gov.service.payments.commons.model;

import java.util.Arrays;
import java.util.Optional;

public enum Source {
    CARD_API,
    CARD_PAYMENT_LINK,
    CARD_AGENT_INITIATED_MOTO,
    CARD_EXTERNAL_TELEPHONE;

    public static Optional<Source> from(String sourceName) {
        return Arrays.stream(Source.values())
                .filter(v -> v.name().equals(sourceName))
                .findFirst();
    }
}
