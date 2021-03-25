package uk.gov.service.payments.commons.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import static uk.gov.service.payments.commons.model.ApiResponseDateTimeFormatter.ISO_INSTANT_MILLISECOND_PRECISION;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApiResponseDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    public ApiResponseDateTimeSerializer() {
        this(null);
    }

    private ApiResponseDateTimeSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider sp) throws IOException {
        gen.writeString(ISO_INSTANT_MILLISECOND_PRECISION.format(value));
    }
}
