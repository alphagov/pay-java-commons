package uk.gov.service.payments.commons.testing.db;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import java.io.IOException;

public abstract class PostgresTestHelper {
    
    private static PostgresContainer container;
    
    static void startPostgresIfNecessary(String imageTag) {
        try {
            if (container == null) {
                container = new PostgresContainer(imageTag);
            }
        } catch (DockerCertificateException | InterruptedException | DockerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    static void startPostgresIfNecessary() {
        try {
            if (container == null) {
                container = new PostgresContainer();
            }
        } catch (DockerCertificateException | InterruptedException | DockerException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String getConnectionUrl() {
        return container.getConnectionUrl();
    }

    public String getUsername() {
        return container.getUsername();
    }

    public String getPassword() {
        return container.getPassword();
    }

    public static void stop() {
        container.stop();
        container = null;
    }

    public String getDriverClass() {
        return "org.postgresql.Driver";
    }
}
