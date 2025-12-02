package uk.gov.service.payments.commons.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;

public class MapKeyInsensitiveUniqueValidator implements ConstraintValidator<MapKeyInsensitiveUnique, Map<String, Object>> {

    @Override
    public boolean isValid(Map<String, Object> theMap, ConstraintValidatorContext context) {
        if (theMap == null) {
            return true;
        }
        return theMap.keySet()
                .stream()
                .map(String::toLowerCase)
                .distinct()
                .count() == theMap.keySet().size();
    }
}
