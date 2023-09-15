package uk.gov.service.payments.commons.utils.prometheus;

import io.prometheus.client.Collector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.prometheus.client.CollectorRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;


import java.util.Enumeration;
import java.util.Optional;

class CounterTest {
    final CollectorRegistry defaultRegistry = CollectorRegistry.defaultRegistry;
    final DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();

    @AfterEach
    void resetCollectorRegistryAndLabels() {
        defaultRegistry.clear();
        defaultLabelsSingleton.clearLabels();
    }

    @Test
    void CreatingACounter_ShouldCreateACounterInTheDefaultRegistry() {
        Counter counter = new Counter("test_counter", "Help Text for test_counter");

        Optional<Collector.MetricFamilySamples> metricOption = getMetricFromDefaultRegistry("test_counter");

        assertFalse(metricOption.isEmpty());

        metricOption.ifPresent(
                metric -> {
                    assertEquals("Help Text for test_counter", metric.help);
                }
        );
    }

    @Test
    void Counter_ShouldReturnChildCounterOnLabelsCall() {
        Counter counter = new Counter("test_counter", "Help Text for test_counter");

        assertInstanceOf(io.prometheus.client.Counter.Child.class, counter.labels());
    }

    @Test
    void Counter_shouldBeIncrementableWithoutLabels() {
        Counter counter = new Counter("test_counter_total", "Help text");
        counter.inc();

        double counterValue = this.defaultRegistry.getSampleValue("test_counter_total");
        assertEquals(1.0, counterValue);

        counter.inc(2.0);

        counterValue = this.defaultRegistry.getSampleValue("test_counter_total");
        assertEquals(3.0, counterValue);
    }

    @Test
    void Counter_ShouldHaveDefaultLabels() {
        defaultLabelsSingleton.addLabel("label1", "value1");
        defaultLabelsSingleton.addLabel("label2", "value2");

        Counter counter = new Counter("test_counter", "Help Text for test_counter");
        counter.inc();

        double sampleValue = this.defaultRegistry.getSampleValue("test_counter_total", new String[] {"label1", "label2"}, new String[] {"value1", "value2"});
        assertEquals(1.0, sampleValue);
    }

    @Test
    void Counter_ShouldHaveNamedLabelsWithoutDefaultWhenNoDefault() {
        Counter counter = new Counter("test_counter", "Help Text for test_counter", "label1", "label2");
        counter.labels("value1", "value2").inc();

        double sampleValue = this.defaultRegistry.getSampleValue("test_counter_total", new String[] {"label1", "label2"}, new String[] {"value1", "value2"});
        assertEquals(1.0, sampleValue);
    }

    @Test
    void Counter_ShouldHaveDefaultAndNamedLabels() {
        defaultLabelsSingleton.addLabel("defaultLabel1", "defaultValue1");
        defaultLabelsSingleton.addLabel("defaultLabel2", "defaultValue2");

        Counter counter = new Counter("test_counter", "Help Text for test_counter", "customLabel1", "customLabel2");
        counter.labels("customValue1", "customValue2").inc();

        double sampleValue = this.defaultRegistry.getSampleValue("test_counter_total", new String[] {"defaultLabel1", "defaultLabel2", "customLabel1", "customLabel2"}, new String[] {"defaultValue1", "defaultValue2", "customValue1", "customValue2"});
        assertEquals(1.0, sampleValue);


        counter.labels("customValue1", "customValue2").inc(2.0);

        sampleValue = this.defaultRegistry.getSampleValue("test_counter_total", new String[] {"defaultLabel1", "defaultLabel2", "customLabel1", "customLabel2"}, new String[] {"defaultValue1", "defaultValue2", "customValue1", "customValue2"});
        assertEquals(3.0, sampleValue);
    }


    private Optional<Collector.MetricFamilySamples> getMetricFromDefaultRegistry(String name) {
        for (Enumeration<Collector.MetricFamilySamples> enumerator = this.defaultRegistry.metricFamilySamples(); enumerator.hasMoreElements();) {
            Collector.MetricFamilySamples familySamples = enumerator.nextElement();
            if (familySamples.name.equals(name)) {
                return Optional.of(familySamples);
            }
        }

        return Optional.empty();
    }
}