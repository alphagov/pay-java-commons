package uk.gov.service.payments.commons.model;

import java.util.Objects;

public abstract class WrappedStringValue {

    private final String value;

    public WrappedStringValue(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        if (that != null && this.getClass() == that.getClass()) {
            var wrappedStringValueWithSameRuntimeTypeAsThisObject = (WrappedStringValue) that;
            return this.value.equals(wrappedStringValueWithSameRuntimeTypeAsThisObject.value);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
