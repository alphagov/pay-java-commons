package uk.gov.service.payments.commons.testing.pact.consumers;

import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.model.FileSource;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static au.com.dius.pact.model.PactReader.loadPact;
import static com.google.common.io.Resources.getResource;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static uk.gov.service.payments.commons.testing.port.PortFactory.findFreePort;

public class PactProviderRule extends PactProviderRuleMk2 {

    private String methodName;
    private List<String> pactsToDelete = new ArrayList<>();
    
    public PactProviderRule(String provider, Object target) {
        super(provider, "localhost", findFreePort(), target);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        this.methodName = description.getMethodName();
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    PactProviderRule.super.apply(base, description).evaluate();
                } finally {
                    after();
                }
            }
        };   
    }
    
    @Override
    protected Map<String, RequestResponsePact> getPacts(String fragment) {
        HashMap<String, RequestResponsePact> pacts = new HashMap<>();
        for (Method m : target.getClass().getMethods()) {
            if (m.getName().equals(methodName)) {
                Optional.ofNullable(m.getAnnotationsByType(Pacts.class)).ifPresent(pactsAnnotation ->
                        stream(pactsAnnotation).forEach(p -> stream(p.pacts()).forEach(
                                fileName -> {
                                    if (fileName.contains(provider)) {
                                        RequestResponsePact pact = (RequestResponsePact) loadPact(new FileSource<>(new File(getResource(format("pacts/%s.json", fileName)).getFile())));
                                        pacts.put(provider, pact);
                                    }
                                    if (!p.publish()) pactsToDelete.add(format("%s.json", fileName));
                                }
                        ))
                );
            }
        }
        return pacts;
    }

    @Override
    protected void after() {
        //by deleting from project_dir/target/pacts it won't be there for the pact:publish maven plugin to publish to the pact broker. Issue has been raised here https://github.com/DiUS/pact-jvm/issues/711
        pactsToDelete.stream().forEach(s -> {
            File file = new File(Paths.get("", "target", "pacts", s).toAbsolutePath().toString());
            file.delete();
        });
    }
}
