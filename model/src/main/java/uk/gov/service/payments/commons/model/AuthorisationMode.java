package uk.gov.service.payments.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

public enum AuthorisationMode {
    @JsonProperty("web")
    WEB("web"),
    @JsonProperty("moto_api")
    MOTO_API("moto_api"),
    @JsonProperty("agreement")
    AGREEMENT("agreement"),
    @JsonProperty("external")
    EXTERNAL("external");

    private final String name;

    AuthorisationMode(String name) {
        this.name = name;
    }

    public static AuthorisationMode of(String name) {
        return Stream.of(AuthorisationMode.values())
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getName() {
        return this.name;
    }
}
