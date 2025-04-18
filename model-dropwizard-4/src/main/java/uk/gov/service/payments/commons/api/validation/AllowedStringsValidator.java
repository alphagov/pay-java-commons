package uk.gov.service.payments.commons.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class AllowedStringsValidator implements ConstraintValidator<AllowedStrings, String>  {

    private Set<String> allowedStrings;
    
    @Override
    public void initialize(AllowedStrings parameters) {
        allowedStrings = Set.of(parameters.allowed());
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return allowedStrings.contains(value);
    }
}
