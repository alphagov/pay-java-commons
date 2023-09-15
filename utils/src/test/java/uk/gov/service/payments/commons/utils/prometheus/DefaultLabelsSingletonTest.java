package uk.gov.service.payments.commons.utils.prometheus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class DefaultLabelsSingletonTest {
    @Test
    void CreatingEmptyLabelSet() {
        DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();

        assertTrue(defaultLabelsSingleton.getLabelNames().isEmpty());
        assertTrue(defaultLabelsSingleton.getLabelValues().isEmpty());
    }

    @Test
    void AddingLabels() {
        DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();

        defaultLabelsSingleton.addLabel("Name", "Value");
        defaultLabelsSingleton.addLabel("Foo", "Bar");

        String[] expectedLabelNames = new String[] {"Name", "Foo"};
        String[] actualLabelNames = defaultLabelsSingleton.getLabelNames().toArray(new String[0]);
        assertArrayEquals(actualLabelNames, expectedLabelNames);

        String[] expectedLabelValues = new String[] {"Value", "Bar"};
        String[] actualLabelValues = defaultLabelsSingleton.getLabelValues().toArray(new String[0]);
        assertArrayEquals(actualLabelValues, expectedLabelValues);
    }

    @Test
    void ClearingLabels() {
        DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();

        defaultLabelsSingleton.addLabel("Name", "Value");
        defaultLabelsSingleton.addLabel("Foo", "Bar");

        defaultLabelsSingleton.clearLabels();

        assertTrue(defaultLabelsSingleton.getLabelNames().isEmpty());
        assertTrue(defaultLabelsSingleton.getLabelValues().isEmpty());
    }
}