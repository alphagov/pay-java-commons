package uk.gov.service.payments.commons.testing.db;

import org.junit.jupiter.api.extension.Extension;

public class PostgresDockerExtension extends PostgresTestHelper implements Extension {
    
    public PostgresDockerExtension() {
        startPostgresIfNecessary();
    }
   
    public PostgresDockerExtension(String imageTage) {
        startPostgresIfNecessary(imageTage);
    }
}
