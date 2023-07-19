package uk.gov.service.payments.commons.utils.prometheus;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

@WireMockTest(httpPort = 8080)
class PrometheusDefaultLabelSampleBuilderTest {
    
    @Test
    void testSampleLabels() throws Exception {
        String containerMetadata = new String(getClass().getClassLoader().getResourceAsStream("ecs-container-metadata.json").readAllBytes());
        stubFor(get("/v4/b322172c022b4a8c9411f923350cb623-1121955312").willReturn(okJson(containerMetadata)));

        var prometheusDefaultLabelSampleBuilder = new PrometheusDefaultLabelSampleBuilder(new URI("http://localhost:8080/v4/b322172c022b4a8c9411f923350cb623-1121955312"));
        var sample = prometheusDefaultLabelSampleBuilder.createSample("a-metric", null, null, null, 1.0);
        assertThat(sample.labelNames, contains("containerImageTag", "ecsClusterName", "ecsServiceName", "ecsTaskID", "awsAccountName", "instance"));
        var values = new EcsContainerMetadataVersion4(containerMetadata);
        assertThat(sample.labelValues, contains(values.getContainerImageTag(), values.getClusterName(), 
                values.getServiceName(), values.getEcsTaskId(), values.getAccountName(), values.getInstanceIP().get()));

    }
}
