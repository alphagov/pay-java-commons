package uk.gov.pay.commons.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValid(value);
    }

    public static boolean isValid(String value) {
        return Optional.ofNullable(value).orElse("").isBlank()
                || DateTimeUtils.toUTCZonedDateTime(value).isPresent();
    }
}
