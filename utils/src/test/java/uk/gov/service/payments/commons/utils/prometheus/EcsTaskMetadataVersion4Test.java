package uk.gov.service.payments.commons.utils.prometheus;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EcsTaskMetadataVersion4Test {
    
    private static EcsTaskMetadataVersion4 ecsTaskMetadata;
    
    @BeforeAll
    static void setup() throws IOException {
        ecsTaskMetadata = new EcsTaskMetadataVersion4(
                new String(EcsTaskMetadataVersion4Test.class.getClassLoader().getResourceAsStream("ecs-container-metadata.json").readAllBytes()));
    }
    
    @Test
    void extractInstanceLabel() {
        assertEquals("172.18.64.83", ecsTaskMetadata.getInstanceLabel().get());
    }
    
    @Test
    void extractEmptyInstanceLabel() throws Exception {
        EcsTaskMetadataVersion4 ecsTaskMetadata = new EcsTaskMetadataVersion4(
                new String(EcsTaskMetadataVersion4Test.class.getClassLoader().getResourceAsStream("ecs-container-metadata-no-network.json").readAllBytes()));
        assertTrue(ecsTaskMetadata.getInstanceLabel().isEmpty());
    }
    
    @Test
    void extractContainerImageTag() {
        assertEquals("PP-11223-4", ecsTaskMetadata.getContainerImageTag());
    }
    
    @Test
    void shouldThrowExceptionIfInvalidJsonProvided() {
        assertThrows(JSONException.class, () -> new EcsTaskMetadataVersion4("{\"Cluster\":\"arn\""));
    }
    
    @Test
    void extractEcsClusterName() {
        assertEquals("test-12-fargate", ecsTaskMetadata.getClusterName());
    }
    
    @Test
    void extractEcsServiceName() {
        assertEquals("connector", ecsTaskMetadata.getServiceName());
    }

    @Test
    void extractEcsTaskId() {
        assertEquals("b322172c022b4a8c9411f923350cb623", ecsTaskMetadata.getEcsTaskId()); // pragma: allowlist secret
    }
    
    @Test
    void extractAccountName() {
        assertEquals("test", ecsTaskMetadata.getAccountName());
    }
}
