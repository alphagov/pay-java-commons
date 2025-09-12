package uk.gov.service.payments.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

public enum AgreementPaymentType {
    @JsonProperty("instalment")
    INSTALMENT("instalment"),
    @JsonProperty("recurring")
    RECURRING("recurring"),
    @JsonProperty("unscheduled")
    UNSCHEDULED("unscheduled");

    private final String name;

    AgreementPaymentType(String name) {
        this.name = name;
    }
    
    public static AgreementPaymentType of(String name) {
        return Stream.of(AgreementPaymentType.values())
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getName() {
        return this.name;
    }
}
