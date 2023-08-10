package uk.gov.service.payments.commons.utils.prometheus;

import io.prometheus.client.Collector;
import io.prometheus.client.dropwizard.samplebuilder.SampleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrometheusDefaultLabelSampleBuilder implements SampleBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PrometheusDefaultLabelSampleBuilder.class);

    private final Map<String, String> ecsLabels = new LinkedHashMap<>();

    public PrometheusDefaultLabelSampleBuilder(URI ecsContainerMetadataUriV4) {
        try {
            logger.info("Getting container metadata from " + ecsContainerMetadataUriV4);
            HttpRequest request = HttpRequest.newBuilder(ecsContainerMetadataUriV4).GET().build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Status code from ECS: " + response.statusCode());
            if (response.statusCode() == 200) {
                var containerMetadata = new EcsContainerMetadataVersion4(response.body());
                ecsLabels.put("containerImageTag", containerMetadata.getContainerImageTag());
                ecsLabels.put("ecsClusterName", containerMetadata.getClusterName());
                ecsLabels.put("ecsServiceName", containerMetadata.getServiceName());
                ecsLabels.put("ecsTaskID", containerMetadata.getEcsTaskId());
                ecsLabels.put("awsAccountName", containerMetadata.getAccountName());
                Optional<String> instanceIP = containerMetadata.getInstanceIP();
                instanceIP.ifPresentOrElse(ip -> ecsLabels.put("instance", ip),
                        () -> logger.info("There was no IP for the container with ecsTaskID {} and ecsServiceName {}.",
                                containerMetadata.getEcsTaskId(), containerMetadata.getServiceName()));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collector.MetricFamilySamples.Sample createSample(String dropwizardName, String nameSuffix,
                                                             List<String> additionalLabelNames,
                                                             List<String> additionalLabelValues,
                                                             double value) {
        final String suffix = nameSuffix == null ? "" : nameSuffix;

        List<String> finalLabelNames = new ArrayList<>(additionalLabelNames);
        List<String> finalLabelValues = new ArrayList<>(additionalLabelValues);

        finalLabelNames.addAll(ecsLabels.keySet());
        finalLabelValues.addAll(ecsLabels.values());

        return new Collector.MetricFamilySamples.Sample(
                Collector.sanitizeMetricName(dropwizardName + suffix),
                finalLabelNames,
                finalLabelValues,
                value);
    }
}
