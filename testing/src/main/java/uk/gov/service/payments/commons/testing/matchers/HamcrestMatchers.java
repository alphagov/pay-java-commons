package uk.gov.service.payments.commons.testing.matchers;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class HamcrestMatchers {

    public static Matcher<Object> optionalMatcher(String value) {
        return value == null ? is(nullValue()) : is(value);
    }
}
