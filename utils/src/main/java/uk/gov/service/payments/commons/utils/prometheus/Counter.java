package uk.gov.service.payments.commons.utils.prometheus;

import org.apache.commons.lang3.ArrayUtils;

public class Counter {
    private final io.prometheus.client.Counter counter;
    private final String[] defaultLabelValues;

    public Counter(String name, String help, String ...labelNames) {
        DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();

        String[] allLabelNames = ArrayUtils.addAll(defaultLabelsSingleton.getLabelNames().toArray(new String[0]), labelNames);
        this.defaultLabelValues = defaultLabelsSingleton.getLabelValues().toArray(new String[0]);

        this.counter = io.prometheus.client.Counter.build()
                .name(name)
                .help(help)
                .labelNames(allLabelNames)
                .register();
    }

    public io.prometheus.client.Counter.Child labels(String ...labelValues) {
        String[] allLabelValues = ArrayUtils.addAll(this.defaultLabelValues, labelValues);
        return this.counter.labels(allLabelValues);
    }

    public void inc() {
        if (this.defaultLabelValues.length == 0) {
            this.counter.inc();
        } else {
            this.counter.labels(this.defaultLabelValues).inc();
        }
    }

    public void inc(double amount) {
        if (this.defaultLabelValues.length == 0) {
            this.counter.inc(amount);
        } else {
            this.counter.labels(this.defaultLabelValues).inc(amount);
        }
    }
}