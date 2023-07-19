package uk.gov.service.payments.commons.utils.prometheus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Extracts relevant information from the <a href="https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint-v4.html">AWS ECS task metadata endpoint version 4</a>.
 */
public class EcsContainerMetadataVersion4 {

    private final String clusterName;
    private final String accountName;
    private final String ecsTaskId;
    private final String containerImageTag;
    private final String serviceName;
    private final String instanceLabel;

    /**
     * Will throw a runtime exception if JSON is not valid.
     * @param ecsContainerMetadataJson A valid <a href="https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint-v4.html">Container metadata JSON</a>
     */
    public EcsContainerMetadataVersion4(String ecsContainerMetadataJson) {
        JSONObject ecsContainerMetadata = new JSONObject(ecsContainerMetadataJson);

        JSONObject labels = ecsContainerMetadata.getJSONObject("Labels");
        String cluster = labels.getString("com.amazonaws.ecs.cluster");
        this.clusterName = cluster.substring(cluster.lastIndexOf("/") + 1);
        this.accountName = clusterName.substring(0, clusterName.indexOf("-"));
        
        String taskARN = labels.getString("com.amazonaws.ecs.task-arn");
        this.ecsTaskId = taskARN.substring(taskARN.lastIndexOf("/") + 1);

        String image = ecsContainerMetadata.getString("Image");
        this.containerImageTag = image.substring(image.lastIndexOf(":") + 1);

        this.serviceName = ecsContainerMetadata.getString("Name");
        
        String instanceLabel;
        try {
            instanceLabel = (String) ecsContainerMetadata.getJSONArray("Networks")
                    .getJSONObject(0).getJSONArray("IPv4Addresses").get(0);
        } catch (JSONException e) {
            instanceLabel = null;
        }
        this.instanceLabel = instanceLabel;
            
    }
    
    public String getContainerImageTag() {
        return containerImageTag;
    }

    public String getClusterName() {
        return clusterName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getEcsTaskId() {
        return ecsTaskId;
    }

    public String getAccountName() {
        return accountName;
    }

    public Optional<String> getInstanceLabel() {
        return Optional.ofNullable(instanceLabel);
    }
}
