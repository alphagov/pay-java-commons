package uk.gov.pay.commons.utils.xray;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Namespace;
import com.amazonaws.xray.entities.Subsegment;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionProfiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class XRaySessionProfiler implements SessionProfiler {
    private static final Logger logger = LoggerFactory.getLogger(XRaySessionProfiler.class);
    private int profileWeight = SessionProfiler.ALL;
    private final AWSXRayRecorder recorder;

    // Required by EclipseLink
    public XRaySessionProfiler() {
        this.recorder = AWSXRay.getGlobalRecorder();
    }

    // Used for testing
    XRaySessionProfiler(AWSXRayRecorder recorder) {
        this.recorder = recorder;
    }

    @Override
    public void endOperationProfile(String operationName) {
    }

    @Override
    public void endOperationProfile(String operationName, DatabaseQuery databaseQuery, int weight) {
    }

    @Override
    public Object profileExecutionOfQuery(DatabaseQuery databaseQuery, Record record, AbstractSession abstractSession) {
        if (recorder.getCurrentSegmentOptional().isPresent()) {
            Optional<DatabaseLogin> databaseLogin = getDatabaseLogin(abstractSession);
            String hostname = databaseLogin.map(this::getDatabaseHostName).orElse("database");

            Subsegment subsegment = recorder.beginSubsegment(hostname);
            subsegment.putMetadata("monitor_name", databaseQuery.getMonitorName());
            subsegment.putMetadata("calling_class", databaseQuery.getClass().getSimpleName());
            subsegment.setNamespace(Namespace.REMOTE.toString());

            Map<String, Object> databaseMetadata = databaseLogin.map(login -> getQueryMetadataWithLogin(login, databaseQuery))
                    .orElse(getQueryMetadata(databaseQuery));
            subsegment.putAllSql(databaseMetadata);

            try {
                return abstractSession.internalExecuteQuery(databaseQuery, (AbstractRecord) record);
            } finally {
                subsegment.end();
            }
        } else {
            logger.info("No segment found, executing query without recording subsegment data.");
            return abstractSession.internalExecuteQuery(databaseQuery, (AbstractRecord) record);
        }
    }

    private Optional<DatabaseLogin> getDatabaseLogin(AbstractSession abstractSession) {
        try {
            DatabaseLogin datasourceLogin = (DatabaseLogin) abstractSession.getDatasourceLogin();
            return Optional.ofNullable(datasourceLogin);
        } catch (ClassCastException e) {
            logger.warn("Cannot cast to DatabaseLogin [{}]", e.getMessage());
            return Optional.empty();
        }
    }

    private Map<String, Object> getQueryMetadataWithLogin(DatabaseLogin databaseLogin, DatabaseQuery databaseQuery) {
        Map<String, Object> databaseMetadata = getQueryMetadata(databaseQuery);
        databaseMetadata.putAll(getLoginDetails(databaseLogin));
        return databaseMetadata;
    }

    private Map<String, Object> getQueryMetadata(DatabaseQuery databaseQuery) {
        Map<String, Object> databaseMetadata = new HashMap<>();
        databaseMetadata.put("preparation", databaseQuery.isCallQuery() ? "call" : "statement");
        databaseMetadata.put("sanitized_query", Optional.ofNullable(databaseQuery.getSQLString()).orElse(""));
        return databaseMetadata;
    }

    private Map<String, Object> getLoginDetails(DatabaseLogin databaseLogin) {
        Map<String, Object> databaseMetadata = new HashMap<>();
        databaseMetadata.put("url", databaseLogin.getURL());
        databaseMetadata.put("user", databaseLogin.getUserName());
        return databaseMetadata;
    }

    private String getDatabaseHostName(DatabaseLogin databaseLogin) {
        try {
            final URI dbURI = new URI((new URI(databaseLogin.getURL())).getSchemeSpecificPart());
            String hostname = dbURI.getHost();
            final String rawPath = dbURI.getPath();
            String catalogue = rawPath.substring(1);

            return catalogue + "@" + hostname;
        } catch (URISyntaxException exception) {
            logger.warn("Error parsing database host name.");
        }
        return "database";
    }

    @Override
    public void setSession(Session session) {
    }

    @Override
    public void startOperationProfile(String operationName) {
    }

    @Override
    public void startOperationProfile(String operationName, DatabaseQuery databaseQuery, int weight) {
    }

    @Override
    public void update(String operationName, Object value) {
    }

    @Override
    public void occurred(String operationName, AbstractSession abstractSession) {
    }

    @Override
    public void occurred(String operationName, DatabaseQuery databaseQuery, AbstractSession abstractSession) {
    }

    @Override
    public void setProfileWeight(int profileWeight) {
        this.profileWeight = profileWeight;
    }

    @Override
    public int getProfileWeight() {
        return this.profileWeight;
    }

    @Override
    public void initialize() {
    }
}
