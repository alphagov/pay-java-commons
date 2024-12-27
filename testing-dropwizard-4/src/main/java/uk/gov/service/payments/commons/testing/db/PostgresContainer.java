package uk.gov.service.payments.commons.testing.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class PostgresContainer {

    private static final Logger logger = LoggerFactory.getLogger(PostgresContainer.class);


    private static PostgreSQLContainer<?> POSTGRES_CONTAINER;
    private static String DB_NAME = "pay_test";
    private static String DB_USERNAME = "test";
    private static String DB_PASSWORD = "test";
    private static String IMAGE_TAG = "15.2";

    public PostgresContainer(String imageTag) {
        new PostgresContainer(DB_USERNAME, DB_PASSWORD, DB_NAME, imageTag);
    }

    public PostgresContainer(String dbUsername, String dbPassword, String dbName, String imageTag) {
        this.DB_PASSWORD = dbPassword;
        this.DB_USERNAME = dbUsername;
        this.DB_NAME = dbName;
        this.IMAGE_TAG = imageTag;
    }

    public static void getOrCreate() {
        try {
            if (POSTGRES_CONTAINER == null) {
                logger.info("Creating Postgres Container");

                POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:" + IMAGE_TAG)
                        .withUsername(DB_USERNAME)
                        .withPassword(DB_PASSWORD);

                POSTGRES_CONTAINER.start();
                createDatabase();
                registerShutdownHook();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getOrCreate(String imageTag) {
        IMAGE_TAG = imageTag;
        getOrCreate();
    }

    private static void createDatabase() {
        try (Connection connection = getConnection(getConnectionUrl(), DB_USERNAME, DB_PASSWORD)) {
            connection.createStatement().execute("CREATE DATABASE " + DB_NAME + " WITH owner=" + DB_USERNAME + " TEMPLATE postgres");
            connection.createStatement().execute("GRANT ALL PRIVILEGES ON DATABASE " + DB_NAME + " TO " + DB_USERNAME);
            connection.createStatement().execute("CREATE EXTENSION IF NOT EXISTS pg_trgm");
            connection.createStatement().execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUsername() {
        return DB_USERNAME;
    }

    public static String getPassword() {
        return DB_PASSWORD;
    }

    public static String getConnectionUrl() {
        return POSTGRES_CONTAINER.getJdbcUrl();
    }

    public static PostgreSQLContainer getPostgresContainer() {
        return POSTGRES_CONTAINER;
    }

    public static void stop() {
        if (POSTGRES_CONTAINER == null) {
            return;
        }
        try {
            POSTGRES_CONTAINER.stop();
            POSTGRES_CONTAINER = null;
        } catch (Exception e) {
            System.err.println("Could not shutdown Postgres Container");
            e.printStackTrace();
        }
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(PostgresContainer::stop));
    }

    private boolean checkPostgresConnection() {

        Properties props = new Properties();
        props.setProperty("user", this.DB_USERNAME);
        props.setProperty("password", this.DB_PASSWORD);

        try (Connection connection = DriverManager.getConnection(getConnectionUrl(), props)) {
            return true;
        } catch (Exception except) {
            return false;
        }
    }
}
