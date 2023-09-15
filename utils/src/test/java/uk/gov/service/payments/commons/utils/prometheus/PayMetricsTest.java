package uk.gov.service.payments.commons.utils.prometheus;

import com.codahale.metrics.MetricRegistry;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

@WireMockTest(httpPort = 8080)
class PayMetricsTest {
    final DefaultLabelsSingleton defaultLabelsSingleton = DefaultLabelsSingleton.getInstance();
    final CollectorRegistry collectorRegistry = CollectorRegistry.defaultRegistry;
    final MetricRegistry dropWizardMetricsRegistry = new MetricRegistry();

    @AfterEach
    void resetCollectorRegistryAndDefaultLabels() {
        defaultLabelsSingleton.clearLabels();
        collectorRegistry.clear();
    }

    @Test
    void initialisePrometheusMetricsWithoutMetadataUri_ShouldCreateNoDefaultLabels() {
        PayMetrics.initialisePrometheusMetrics(dropWizardMetricsRegistry);

        assertThat(defaultLabelsSingleton.getLabelNames(), empty());
        assertThat(defaultLabelsSingleton.getLabelValues(), empty());
    }

    @Test
    void initialisePrometheusMetricsWithMetadataUri_ShouldCreateECSDefaultLabels() throws Exception {
        String containerMetadata = new String(getClass().getClassLoader().getResourceAsStream("ecs-container-metadata.json").readAllBytes());
        stubFor(get("/v4/b322172c022b4a8c9411f923350cb623-1121955312").willReturn(okJson(containerMetadata)));

        PayMetrics.initialisePrometheusMetrics(dropWizardMetricsRegistry, new URI("http://localhost:8080/v4/b322172c022b4a8c9411f923350cb623-1121955312"));

        var values = new EcsContainerMetadataVersion4(containerMetadata);

        assertThat(defaultLabelsSingleton.getLabelNames(), contains("containerImageTag", "ecsClusterName", "ecsServiceName", "ecsTaskID", "awsAccountName", "instance"));
        assertThat(defaultLabelsSingleton.getLabelValues(), contains(
                values.getContainerImageTag(), values.getClusterName(), values.getServiceName(), values.getEcsTaskId(), values.getAccountName(), values.getInstanceIP().get())
        );
    }
}