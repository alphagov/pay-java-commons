package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;

import static uk.gov.service.payments.commons.model.CommonDateTimeFormatters.ISO_INSTANT_MILLISECOND_PRECISION;

public class IsoInstantMillisecondDeserializer extends StdDeserializer<Instant> {

    public IsoInstantMillisecondDeserializer() {
        this(null);
    }

    private IsoInstantMillisecondDeserializer(Class<Instant> t) {
        super(t);
    }

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
       return ISO_INSTANT_MILLISECOND_PRECISION.parse(jsonParser.getText(), Instant::from);
    }

}
