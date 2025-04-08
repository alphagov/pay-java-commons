package uk.gov.service.payments.commons.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = AllowedStringsValidator.class)
@Documented
public @interface AllowedStrings {

    String message();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    
    String[] allowed();

    @Target({ FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        AllowedStrings[] value();
    }
}
