package uk.gov.service.payments.commons.utils.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class PayMetrics {
    private static final Logger logger = LoggerFactory.getLogger(PayMetrics.class);

    public static io.prometheus.client.CollectorRegistry initialisePrometheusMetrics(com.codahale.metrics.MetricRegistry dropwizardMetricRegistry, URI ecsContainerMetadataUriV4) {
        prometheusDefaultLabels(ecsContainerMetadataUriV4);
        CollectorRegistry collectorRegistry = CollectorRegistry.defaultRegistry;
        collectorRegistry.register(new DropwizardExports(dropwizardMetricRegistry, new PrometheusDefaultLabelSampleBuilder()));
        return collectorRegistry;
    }

    public static io.prometheus.client.CollectorRegistry initialisePrometheusMetrics(com.codahale.metrics.MetricRegistry dropwizardMetricRegistry) {
        CollectorRegistry collectorRegistry = CollectorRegistry.defaultRegistry;
        collectorRegistry.register(new DropwizardExports(dropwizardMetricRegistry));
        return collectorRegistry;
    }

    private static void prometheusDefaultLabels(URI ecsContainerMetadataUriV4) {
        DefaultLabelsSingleton defaultLabels = DefaultLabelsSingleton.getInstance();

        try {
            logger.info("Getting container metadata from " + ecsContainerMetadataUriV4);

            HttpRequest request = HttpRequest.newBuilder(ecsContainerMetadataUriV4).GET().build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Status code from ECS: " + response.statusCode());

            if (response.statusCode() == 200) {
                var containerMetadata = new EcsContainerMetadataVersion4(response.body());

                defaultLabels.addLabel("containerImageTag", containerMetadata.getContainerImageTag());
                defaultLabels.addLabel("ecsClusterName", containerMetadata.getClusterName());
                defaultLabels.addLabel("ecsServiceName", containerMetadata.getServiceName());
                defaultLabels.addLabel("ecsTaskID", containerMetadata.getEcsTaskId());
                defaultLabels.addLabel("awsAccountName", containerMetadata.getAccountName());

                Optional<String> instanceIP = containerMetadata.getInstanceIP();
                instanceIP.ifPresentOrElse(ip -> defaultLabels.addLabel("instance", ip),
                        () -> logger.info("There was no IP for the container with ecsTaskID {} and ecsServiceName {}.",
                                containerMetadata.getEcsTaskId(), containerMetadata.getServiceName()));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}