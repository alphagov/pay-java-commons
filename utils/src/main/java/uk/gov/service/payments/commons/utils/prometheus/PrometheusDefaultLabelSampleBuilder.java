package uk.gov.service.payments.commons.utils.prometheus;

import io.prometheus.client.Collector;
import io.prometheus.client.dropwizard.samplebuilder.SampleBuilder;

import java.util.ArrayList;
import java.util.List;

public class PrometheusDefaultLabelSampleBuilder implements SampleBuilder {
    private final List<String> defaultLabelNames;
    private final List<String> defaultLabelValues;

    public PrometheusDefaultLabelSampleBuilder() {
        DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();
        this.defaultLabelNames = defaultLabelsSingleton.getLabelNames();
        this.defaultLabelValues = defaultLabelsSingleton.getLabelValues();
    }

    @Override
    public Collector.MetricFamilySamples.Sample createSample(String dropwizardName, String nameSuffix,
                                                             List<String> additionalLabelNames,
                                                             List<String> additionalLabelValues,
                                                             double value) {
        final String suffix = nameSuffix == null ? "" : nameSuffix;

        List<String> finalLabelNames = new ArrayList<>(additionalLabelNames);
        List<String> finalLabelValues = new ArrayList<>(additionalLabelValues);

        finalLabelNames.addAll(defaultLabelNames);
        finalLabelValues.addAll(defaultLabelValues);

        return new Collector.MetricFamilySamples.Sample(
                Collector.sanitizeMetricName(dropwizardName + suffix),
                finalLabelNames,
                finalLabelValues,
                value);
    }
}
