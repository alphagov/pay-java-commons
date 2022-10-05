package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;

import static uk.gov.service.payments.commons.model.ApiResponseDateTimeFormatter.ISO_INSTANT_MICROSECOND_PRECISION;

public class ApiResponseInstantWithMicrosecondPrecisionSerializer extends StdSerializer<Instant> {

    public ApiResponseInstantWithMicrosecondPrecisionSerializer() {
        this(null);
    }

    private ApiResponseInstantWithMicrosecondPrecisionSerializer(Class<Instant> t) {
        super(t);
    }

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider sp) throws IOException {
        gen.writeString(ISO_INSTANT_MICROSECOND_PRECISION.format(value));
    }

}
