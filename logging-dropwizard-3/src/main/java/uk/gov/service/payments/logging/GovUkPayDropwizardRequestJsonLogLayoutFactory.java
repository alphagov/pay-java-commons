package uk.gov.service.payments.logging;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.json.AccessJsonLayoutBaseFactory;
import io.dropwizard.logging.json.layout.TimestampFormatter;

import java.util.TimeZone;

/**
 * Our custom implementation of the factory used to format dropwizard request logs as json.
 * This is configured in Java apps by adding the following to the config.yaml:
 * -
 * requestLog:
 *  appenders:
 *    - type: console
 *      layout:
 *        type: govuk-pay-access-json
 *        additionalFields:
 *          container: "ledger"
 *          environment: ${ENVIRONMENT}
 * -
 * More additional fields can be added as required; however it is mandatory to add a "container" key as this is 
 * standard across all our dropwizard apps.
 */
@JsonTypeName("govuk-pay-access-json")
public class GovUkPayDropwizardRequestJsonLogLayoutFactory extends AccessJsonLayoutBaseFactory {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @Override
    public LayoutBase<IAccessEvent> build(LoggerContext context, TimeZone timeZone) {
        if (!this.getAdditionalFields().containsKey("container")) {
            throw new IllegalArgumentException("When using govuk-pay-access-json, an additional field with the key of " +
                    "\"container\" must be present");
        }
        var jsonLayout = new GovUkPayDropwizardRequestJsonLogLayout(this.createDropwizardJsonFormatter(),
                this.createTimestampFormatter(timeZone), 1, this.getAdditionalFields());
        jsonLayout.setContext(context);
        return jsonLayout;
    }

    @Override
    protected TimestampFormatter createTimestampFormatter(TimeZone timeZone) {
        return new TimestampFormatter(TIMESTAMP_FORMAT, timeZone.toZoneId());
    }
}

