package uk.gov.pay.commons.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.stream.Collectors;

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
