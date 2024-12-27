package uk.gov.service.payments.commons.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static uk.gov.service.payments.commons.model.CommonDateTimeFormatters.ISO_INSTANT_MICROSECOND_PRECISION;
import static uk.gov.service.payments.commons.model.CommonDateTimeFormatters.ISO_INSTANT_MILLISECOND_PRECISION;
import static uk.gov.service.payments.commons.model.CommonDateTimeFormatters.ISO_LOCAL_DATE_IN_UTC;

class CommonDateTimeFormattersTest {

    @Test
    void isoInstantMillisecondPrecisionConvertsInstantToIsoStringWithMillisecondPrecision() {
        var instant = Instant.parse("2015-10-21T07:28:00.12356789Z");

        String result = ISO_INSTANT_MILLISECOND_PRECISION.format(instant);

        assertThat(result, is("2015-10-21T07:28:00.123Z"));
    }

    @Test
    void isoInstantMillisecondPrecisionConvertsInstantExactlyOnTheSecondToIsoStringWithMillisecondPrecision() {
        var instant = Instant.parse("2015-10-21T07:28:00Z");

        String result = ISO_INSTANT_MILLISECOND_PRECISION.format(instant);

        assertThat(result, is("2015-10-21T07:28:00.000Z"));
    }

    @Test
    void isoInstantMillisecondPrecisionConvertsUtcZonedDateTimeToIsoStringWithMillisecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00.123456789"), ZoneOffset.UTC);

        String result = ISO_INSTANT_MILLISECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T07:28:00.123Z"));
    }

    @Test
    void isoInstantMillisecondPrecisionConvertsNonUtcZonedDateTimeToIsoStringWithMillisecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00.123456789"),
                ZoneId.of("America/Los_Angeles"));

        String result = ISO_INSTANT_MILLISECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T14:28:00.123Z"));
    }

    @Test
    void isoInstantMillisecondPrecisionConvertsZonedDateTimeExactlyOnTheSecondToIsoStringWithMillisecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00"), ZoneOffset.UTC);

        String result = ISO_INSTANT_MILLISECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T07:28:00.000Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsInstantToIsoStringWithMicrosecondPrecision() {
        var instant = Instant.parse("2015-10-21T07:28:00.12356789Z");

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(instant);

        assertThat(result, is("2015-10-21T07:28:00.123567Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsInstantExactlyOnTheSecondToIsoStringWithMicrosecondPrecision() {
        var instant = Instant.parse("2015-10-21T07:28:00Z");

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(instant);

        assertThat(result, is("2015-10-21T07:28:00.000000Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsInstantExactlyOnTheMillisecondToIsoStringWithMicrosecondPrecision() {
        var instant = Instant.parse("2015-10-21T07:28:00.123Z");

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(instant);

        assertThat(result, is("2015-10-21T07:28:00.123000Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsUtcZonedDateTimeToIsoStringWithMicrosecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00.123456789"), ZoneOffset.UTC);

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T07:28:00.123456Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsNonUtcZonedDateTimeToIsoStringWithMicrosecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00.123456789"),
                ZoneId.of("America/Los_Angeles"));

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T14:28:00.123456Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsZonedDateTimeExactlyOnTheSecondToIsoStringWithMicrosecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00"), ZoneOffset.UTC);

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T07:28:00.000000Z"));
    }

    @Test
    void isoInstantMicrosecondPrecisionConvertsZonedDateTimeExactlyOnTheMillisecondToIsoStringWithMicrosecondPrecision() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2015-10-21T07:28:00.123"), ZoneOffset.UTC);

        String result = ISO_INSTANT_MICROSECOND_PRECISION.format(zonedDateTime);

        assertThat(result, is("2015-10-21T07:28:00.123000Z"));
    }

    @Test
    void isoLocalDateInUtcConvertsInstantToIsoLocalDateInUtc() {
        var instant = Instant.parse("2022-10-04T12:34:56.789Z");

        String result = ISO_LOCAL_DATE_IN_UTC.format(instant);

        assertThat(result, is("2022-10-04"));
    }

    @Test
    void isoLocalDateInUtcConvertsZonedDateTimeInUtcToIsoLocalDateInUtc() {
        var zonedDateTime = ZonedDateTime.parse("2022-10-04T12:34:56.789Z");

        String result = ISO_LOCAL_DATE_IN_UTC.format(zonedDateTime);

        assertThat(result, is("2022-10-04"));
    }

    @Test
    void isoLocalDateInUtcConvertsZonedDateTimeInAnotherTimeZoneWhereItIsAlreadyTomorrowToIsoLocalDateInUtc() {
        var zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2022-10-05T06:07:08.123"), ZoneId.of("Pacific/Auckland"));

        String result = ISO_LOCAL_DATE_IN_UTC.format(zonedDateTime);

        assertThat(result, is("2022-10-04"));
    }

}
