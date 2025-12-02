package uk.gov.service.payments.commons.testing.db;

public abstract class PostgresTestHelper {

    static void startPostgresIfNecessary(String imageTag) {
        PostgresContainer.getOrCreate(imageTag);
    }
    
    static void startPostgresIfNecessary() {
        PostgresContainer.getOrCreate();
    }
    
    public String getConnectionUrl() {
        return PostgresContainer.getConnectionUrl();
    }

    public String getUsername() {
        return PostgresContainer.getUsername();
    }

    public String getPassword() {
        return PostgresContainer.getPassword();
    }

    public static void stop() {
        PostgresContainer.stop();
    }

    public String getDriverClass() {
        return "org.postgresql.Driver";
    }
}
