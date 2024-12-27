package uk.gov.service.payments.commons.testing.db;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class PostgresDockerRule extends PostgresTestHelper implements TestRule {
    
    public PostgresDockerRule(String imageTag) {
        startPostgresIfNecessary(imageTag);
    }
    
    public PostgresDockerRule() {
        startPostgresIfNecessary();
    }
    
    @Override
    public Statement apply(Statement statement, Description description) {
        return statement;
    }
}
