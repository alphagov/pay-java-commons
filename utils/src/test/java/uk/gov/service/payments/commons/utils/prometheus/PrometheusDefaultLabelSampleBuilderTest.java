package uk.gov.service.payments.commons.utils.prometheus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

class PrometheusDefaultLabelSampleBuilderTest {

    DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();

    @AfterEach
    void resetLabels() {
        defaultLabelsSingleton.clearLabels();
    }
    
    @Test
    void testSampleLabels() throws Exception {
        defaultLabelsSingleton.addLabel("defaultLabel1", "defaultValue1");
        defaultLabelsSingleton.addLabel("defaultLabel2", "defaultValue2");

        List<String> additionalLabelNames = Arrays.asList("name1", "name2", "name3");
        List<String> additionalLabelValues = Arrays.asList("value1", "value2", "value3");

        var prometheusDefaultLabelSampleBuilder = new PrometheusDefaultLabelSampleBuilder();

        var sample = prometheusDefaultLabelSampleBuilder.createSample("a-metric", null, additionalLabelNames, additionalLabelValues, 1.0);
        assertThat(sample.labelNames, contains("name1", "name2", "name3", "defaultLabel1", "defaultLabel2"));
        assertThat(sample.labelValues, contains("value1", "value2", "value3", "defaultValue1", "defaultValue2"));
    }
}
