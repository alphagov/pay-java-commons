package uk.gov.service.payments.commons.utils.prometheus;

import java.util.ArrayList;
import java.util.List;

public final class DefaultLabelsSingleton {
    private static DefaultLabelsSingleton INSTANCE;
    private final List<String> labelNames = new ArrayList<>();
    private final List<String> labelValues = new ArrayList<>();

    public static DefaultLabelsSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultLabelsSingleton();
        }

        return INSTANCE;
    }

    private DefaultLabelsSingleton() {
    }

    public List<String> getLabelNames() {
        return this.labelNames;
    }

    public List<String> getLabelValues() {
        return this.labelValues;
    }

    public void addLabel(String labelName, String labelValue) {
        this.labelNames.add(labelName);
        this.labelValues.add(labelValue);
    }

    public void clearLabels() {
        this.labelNames.clear();
        this.labelValues.clear();
    }
}
