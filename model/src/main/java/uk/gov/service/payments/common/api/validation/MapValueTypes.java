package uk.gov.service.payments.common.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MapValueTypesValidator.class)
public @interface MapValueTypes {
    String message() default "value must be of type {types}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class[] types();

    @Target({ FIELD, PARAMETER})
    @Retention(RUNTIME)
    @interface List {
        MapValueTypes[] value();
    }
}
