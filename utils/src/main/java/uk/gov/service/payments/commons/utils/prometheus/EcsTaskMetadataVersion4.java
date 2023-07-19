package uk.gov.service.payments.commons.utils.prometheus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Extracts relevant information from the <a href="https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint-v4.html">AWS ECS task metadata endpoint version 4</a>.
 */
public class EcsTaskMetadataVersion4 {
    
    private final JSONObject ecsTaskMetadataJson;
    private final String clusterName;
    private final String accountName;
    private final String ecsTaskId;
    private final String containerImageTag;
    private final String serviceName;
    private final String instanceLabel;

    /**
     * Will throw a runtime exception if JSON is not valid.
     * @param ecsTaskMetadataJson A valid <a href="https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint-v4.html#task-metadata-endpoint-v4-response">Task metadata JSON</a>
     */
    public EcsTaskMetadataVersion4(String ecsTaskMetadataJson) {
        this.ecsTaskMetadataJson = new JSONObject(ecsTaskMetadataJson);
        
        String cluster = this.ecsTaskMetadataJson.getString("Cluster");
        this.clusterName = cluster.substring(cluster.lastIndexOf("/") + 1);

        this.accountName = clusterName.substring(0, clusterName.indexOf("-"));
        
        String taskARN = this.ecsTaskMetadataJson.getString("TaskARN");
        this.ecsTaskId = taskARN.substring(taskARN.lastIndexOf("/") + 1);

        JSONObject container = this.ecsTaskMetadataJson.getJSONArray("Containers").getJSONObject(0);
        String image = container.getString("Image");
        this.containerImageTag = image.substring(image.lastIndexOf(":") + 1);
        
        this.serviceName = container.getString("Name");

        String instanceLabel;
        try {
            JSONArray networks = container.getJSONArray("Networks");
            instanceLabel = (String) networks.getJSONObject(0).getJSONArray("IPv4Addresses").get(0);
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
