package uk.gov.service.payments.commons.utils.prometheus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EcsTaskMetadataVersion4Test {
    
    private static EcsTaskMetadataVersion4 ecsTaskMetadata;
    private static EcsTaskMetadataVersion4 ecsTaskMetadataWithoutContainers;
    
    private static EcsTaskMetadataVersion4 emptyEcsTaskMetadata;
    
    @BeforeAll
    static void setup() throws IOException {
        ecsTaskMetadata = new EcsTaskMetadataVersion4(new String(EcsTaskMetadataVersion4Test.class.getClassLoader().getResourceAsStream("ecs-container-task-metadata.json").readAllBytes()));
        ecsTaskMetadataWithoutContainers = new EcsTaskMetadataVersion4(new String(EcsTaskMetadataVersion4Test.class.getClassLoader().getResourceAsStream("ecs-metadata-no-containers.json").readAllBytes()));
        emptyEcsTaskMetadata = new EcsTaskMetadataVersion4("{}");
    }
    
    @Test
    void extractContainerImageTag() throws Exception {
        assertEquals("PP-11223-4", ecsTaskMetadata.getContainerImageTag().get());
    }
    
    @Test
    void shouldReturnEmptyContainerImageTag() {
        assertTrue(ecsTaskMetadataWithoutContainers.getContainerImageTag().isEmpty());
        assertTrue(emptyEcsTaskMetadata.getContainerImageTag().isEmpty());
    }
    
    @Test
    void shouldThrowExceptionIfInvalidJsonProvided() {
        assertThrows(RuntimeException.class, () -> new EcsTaskMetadataVersion4("{\"Cluster\":\"arn\""));
    }
    
    @Test
    void extractEcsClusterName() {
        assertEquals("test-12-fargate", ecsTaskMetadata.getClusterName().get());
    }
    
    @Test
    void shouldReturnEmptyClusterName() {
        assertTrue(emptyEcsTaskMetadata.getClusterName().isEmpty());
    }
    
    @Test
    void extractEcsServiceName() {
        assertEquals("connector", ecsTaskMetadata.getServiceName().get());
    }

    @Test
    void shouldReturnEmptyEcsServiceName() {
        assertTrue(ecsTaskMetadataWithoutContainers.getServiceName().isEmpty());
        assertTrue(emptyEcsTaskMetadata.getServiceName().isEmpty());
    }

    @Test
    void extractEcsTaskId() {
        assertEquals("b322172c022b4a8c9411f923350cb623", ecsTaskMetadata.getEcsTaskId().get()); // pragma: allowlist secret
    }

    @Test
    void shouldReturnEmptyEcsTaskId() {
        var ecsTaskMetadata = new EcsTaskMetadataVersion4("{}");
        assertTrue(ecsTaskMetadata.getEcsTaskId().isEmpty());
    }

    @Test
    void extractAccountName() {
        assertEquals("test", ecsTaskMetadata.getAccountName().get());
    }

    @Test
    void shouldReturnEmptyAccountName() {
        assertTrue(emptyEcsTaskMetadata.getAccountName().isEmpty());
    }
}
