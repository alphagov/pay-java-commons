package uk.gov.service.payments.commons.testing.db;

import com.google.common.base.Stopwatch;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.LogConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

public class PostgresContainer {

    private static final Logger logger = LoggerFactory.getLogger(PostgresContainer.class);

    private final String containerId;
    private final int port;
    private DockerClient docker;
    private volatile boolean stopped = false;
    private String dbPassword;
    private String dbUsername;

    private static final int DB_TIMEOUT_SEC = 15;
    private static final String INTERNAL_PORT = "5432";

    public PostgresContainer(DockerClient docker, String imageName, String dbUsername, String dbPassword) throws InterruptedException, IOException, ClassNotFoundException, DockerException {
        this.docker = docker;
        this.dbPassword = dbPassword;
        this.dbUsername = dbUsername;

        failsafeDockerPull(docker, imageName);
        docker.listImages(DockerClient.ListImagesParam.create("name", imageName));

        final HostConfig hostConfig = HostConfig.builder().logConfig(LogConfig.create("json-file")).publishAllPorts(true).build();
        ContainerConfig containerConfig = ContainerConfig.builder()
                .image(imageName)
                .hostConfig(hostConfig)
                .env("POSTGRES_USER=" + dbUsername, "POSTGRES_PASSWORD=" + dbPassword)
                .build();
        containerId = docker.createContainer(containerConfig).id();
        docker.startContainer(containerId);
        port = hostPortNumber(docker.inspectContainer(containerId));
        registerShutdownHook();
        waitForPostgresToStart();
    }

    public PostgresContainer() throws InterruptedException, IOException, ClassNotFoundException, DockerCertificateException, DockerException {
        this(DefaultDockerClient.fromEnv().build(), "govukpay/postgres:11.1", "postgres", "mysecretpassword");
    }

    public PostgresContainer(String imageName) throws InterruptedException, IOException, ClassNotFoundException, DockerCertificateException, DockerException {
        this(DefaultDockerClient.fromEnv().build(), imageName, "postgres", "mysecretpassword");
    }

    public String getUsername() {
        return dbUsername;
    }

    public String getPassword() {
        return dbPassword;
    }

    public String getConnectionUrl() {
        return "jdbc:postgresql://" + docker.getHost() + ":" + port + "/";
    }

    private void failsafeDockerPull(DockerClient docker, String image) {
        try {
            docker.pull(image);
            docker.pull(image);
        } catch (Exception e) {
            logger.error("Docker image " + image + " could not be pulled from DockerHub", e);
        }
    }

    private static int hostPortNumber(ContainerInfo containerInfo) {
        List<PortBinding> portBindings = containerInfo.networkSettings().ports().get(INTERNAL_PORT + "/tcp");
        logger.info("Postgres host port: {}", portBindings.stream().map(PortBinding::hostPort).collect(joining(", ")));
        return parseInt(portBindings.get(0).hostPort());
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void waitForPostgresToStart() throws DockerException, InterruptedException, IOException {
        Stopwatch timer = Stopwatch.createStarted();
        boolean succeeded = false;
        while (!succeeded && timer.elapsed(TimeUnit.SECONDS) < DB_TIMEOUT_SEC) {
            Thread.sleep(500);
            succeeded = checkPostgresConnection();
        }
        if (!succeeded) {
            throw new RuntimeException("Postgres did not start in " + DB_TIMEOUT_SEC + " seconds.");
        }
        logger.info("Postgres docker container started in {}.", timer.elapsed(TimeUnit.MILLISECONDS));
    }

    private boolean checkPostgresConnection() throws IOException {

        Properties props = new Properties();
        props.setProperty("user", this.dbUsername);
        props.setProperty("password", this.dbPassword);

        try (Connection connection = DriverManager.getConnection(getConnectionUrl(), props)) {
            return true;
        } catch (Exception except) {
            return false;
        }
    }

    public void stop() {
        if (stopped) {
            return;
        }
        try {
            stopped = true;
            System.out.println("Killing postgres container with ID: " + containerId);
            LogStream logs = docker.logs(containerId, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr());
            System.out.println("Killed container logs:\n");
            logs.attach(System.out, System.err);
            docker.stopContainer(containerId, 5);
            docker.removeContainer(containerId);
        } catch (DockerException | InterruptedException | IOException e) {
            System.err.println("Could not shutdown " + containerId);
            e.printStackTrace();
        }
    }
}
