package uk.gov.service.payments.commons.testing.pact.provider;

import com.google.common.collect.ImmutableSetMultimap;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class CreateTestSuite {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTestSuite.class);

    public static TestSuite create(ImmutableSetMultimap<String, JUnit4TestAdapter> consumerToJUnitTest) {
        String consumer = System.getProperty("CONSUMER");
        TestSuite suite = new TestSuite();

        if (consumer == null || consumer.isBlank()) {
            LOGGER.info("Running all contract tests.");
            consumerToJUnitTest.keys().elementSet().forEach(key -> consumerToJUnitTest.get(key).forEach(suite::addTest));
        } else if (consumerToJUnitTest.containsKey(consumer)) {
            LOGGER.info("Running {}-connector contract tests only.", consumer);
            consumerToJUnitTest.get(consumer).forEach(suite::addTest);
        } else {
            throw new RuntimeException(format("Error running provider contract tests. ${CONSUMER} system property was %s.", consumer));
        }
        return suite;
    }
}
