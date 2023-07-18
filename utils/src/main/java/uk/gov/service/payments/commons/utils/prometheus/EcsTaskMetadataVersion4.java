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
    public EcsTaskMetadataVersion4(String ecsTaskMetadataJson) {
        this.ecsTaskMetadataJson = new JSONObject(ecsTaskMetadataJson);
    }

    public Optional<String> getContainerImageTag() {
        return getJSONArray(ecsTaskMetadataJson, "Containers").map(array -> {
            if (array.isEmpty()) {
                return null;
            }
            String image = array.getJSONObject(0).getString("Image");
            return image.substring(image.lastIndexOf(":") + 1);
        });
    }

    public Optional<String> getClusterName() {
        return getString(ecsTaskMetadataJson, "Cluster").map(cluster ->
            cluster.substring(cluster.lastIndexOf("/") + 1));
    }
    
    private static Optional<String> getString(JSONObject jsonObject, String key) {
        try {
            return Optional.of(jsonObject.getString(key));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }
    
    private static Optional<JSONArray> getJSONArray(JSONObject jsonObject, String key) {
        try {
            return Optional.of(jsonObject.getJSONArray(key));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    public Optional<String> getServiceName() {
        return getJSONArray(ecsTaskMetadataJson, "Containers").map(array -> {
            if (array.isEmpty()) {
                return null;
            }
            return array.getJSONObject(0).getString("Name");
        });
    }

    public Optional<String> getEcsTaskId() {
        return getString(ecsTaskMetadataJson, "TaskARN").map(cluster ->
                cluster.substring(cluster.lastIndexOf("/") + 1));
    }

    public Optional<String> getAccountName() {
        return getClusterName().map(clusterName -> clusterName.substring(0, clusterName.indexOf("-")));
    }
}
