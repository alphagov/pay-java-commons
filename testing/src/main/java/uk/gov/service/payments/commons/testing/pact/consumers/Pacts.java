package uk.gov.service.payments.commons.testing.pact.consumers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(MultiPacts.class)
public @interface Pacts {
    String[] pacts();

    boolean publish() default true;
}
