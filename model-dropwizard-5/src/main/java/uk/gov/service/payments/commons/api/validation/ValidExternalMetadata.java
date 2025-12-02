package uk.gov.service.payments.commons.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MAX_KEY_LENGTH;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MAX_KEY_VALUE_PAIRS;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MAX_VALUE_LENGTH;
import static uk.gov.service.payments.commons.model.charge.ExternalMetadata.MIN_KEY_LENGTH;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "Field [metadata] must not be null")
@Size(max = MAX_KEY_VALUE_PAIRS, message = "Field [metadata] cannot have more than {max} key-value pairs")
@MapKeyLength(max = MAX_KEY_LENGTH, min = MIN_KEY_LENGTH, message = "Field [metadata] keys must be between {min} and {max} characters long")
@MapValueTypes(types = {String.class, Number.class, Boolean.class}, message = "Field [metadata] values must be of type String, Boolean or Number")
@MapValueLength(max = MAX_VALUE_LENGTH, message = "Field [metadata] values must be no greater than {max} characters long")
@MapValueNotNull(message = "Field [metadata] must not have null values")
@MapKeyInsensitiveUnique(message = "Field [metadata] must have case insensitive unique keys")
public @interface ValidExternalMetadata {
    String message() default "Invalid metadata";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD, PARAMETER})
    @Retention(RUNTIME)
    @interface List {
        ValidExternalMetadata[] value();
    }
}
