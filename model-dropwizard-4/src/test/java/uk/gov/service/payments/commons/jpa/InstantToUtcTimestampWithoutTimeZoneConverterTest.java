package uk.gov.service.payments.commons.jpa;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

class InstantToUtcTimestampWithoutTimeZoneConverterTest {

    private final InstantToUtcTimestampWithoutTimeZoneConverter converter = new InstantToUtcTimestampWithoutTimeZoneConverter();

    @Test
    void convertsInstantToLocalDateTimeInUtc() {
        var instant = Instant.parse("2020-12-25T15:00:00.123456789Z");
        LocalDateTime result = converter.convertToDatabaseColumn(instant);
        assertThat(result, is(LocalDateTime.of(2020, 12, 25, 15, 0, 0, 123456789)));
    }

    @Test
    void convertsNullInstantToNull() {
        LocalDateTime result = converter.convertToDatabaseColumn(null);
        assertThat(result, is(nullValue()));
    }

    @Test
    void convertsLocalDateTimeToInstant() {
        var localDateTime = LocalDateTime.of(2020, 12, 25, 15, 0, 0, 123456789);
        Instant result = converter.convertToEntityAttribute(localDateTime);
        assertThat(result, is(Instant.parse("2020-12-25T15:00:00.123456789Z")));
    }

    @Test
    void convertsNullLocalDateTimeToNull() {
        Instant result = converter.convertToEntityAttribute(null);
        assertThat(result, is(nullValue()));
    }

}
