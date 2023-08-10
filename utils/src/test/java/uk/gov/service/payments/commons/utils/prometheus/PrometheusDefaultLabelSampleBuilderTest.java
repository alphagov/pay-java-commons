package uk.gov.service.payments.commons.utils.prometheus;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<String> additionalLabelNames = Arrays.asList("name1", "name2", "name3");
        List<String> additionalLabelValues = Arrays.asList("value1", "value2", "value3");

        var prometheusDefaultLabelSampleBuilder = new PrometheusDefaultLabelSampleBuilder(new URI("http://localhost:8080/v4/b322172c022b4a8c9411f923350cb623-1121955312"));
        var sample = prometheusDefaultLabelSampleBuilder.createSample("a-metric", null, additionalLabelNames, additionalLabelValues, 1.0);
        assertThat(sample.labelNames, contains("name1", "name2", "name3", "containerImageTag", "ecsClusterName", "ecsServiceName", "ecsTaskID", "awsAccountName", "instance"));
        var values = new EcsContainerMetadataVersion4(containerMetadata);
        assertThat(sample.labelValues, contains("value1", "value2", "value3", values.getContainerImageTag(), values.getClusterName(),
                values.getServiceName(), values.getEcsTaskId(), values.getAccountName(), values.getInstanceIP().get()));

    }
}
