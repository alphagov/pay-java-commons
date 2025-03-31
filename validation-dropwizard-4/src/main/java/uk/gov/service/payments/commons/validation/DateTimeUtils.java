package uk.gov.service.payments.commons.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateTimeUtils {
    private static final ZoneId UTC = ZoneOffset.UTC;
    private static DateTimeFormatter dateTimeFormatterAny = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static DateTimeFormatter localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Converts any valid ZonedDateTime String (ISO_8601) representation to a UTC ZonedDateTime
     *
     * e.g. 
     * 1. 2010-01-01T12:00:00+01:00[Europe/Paris] to ZonedDateTime("2010-12-31T22:59:59.132Z")
     * 2. 2010-12-31T22:59:59.132Z to ZonedDateTime("2010-12-31T22:59:59.132Z")
     *
     * @param dateString e.g.
     * @return ZonedDateTime instance represented by dateString in UTC ("Z") or
     * Optional.empty() if the dateString does not represent a valid ISO_8601 zone string
     * @see "https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html"
     */
    public static Optional<ZonedDateTime> toUTCZonedDateTime(String dateString) {
        try {
            ZonedDateTime utcDateTime = ZonedDateTime
                    .parse(dateString, dateTimeFormatterAny)
                    .withZoneSameInstant(UTC);
            return Optional.of(utcDateTime);
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }

    /**
     * Converts a LocalDateTime to a UTC ISO_8601 string representation
     *
     * e.g.
     * 1. LocalDateTime("2010-01-01") to "2010-12-01"
     * 2. LocalDateTime("2010-12-31") to "2010-12-31"
     *
     *
     * @param zonedDateTime
     * @return UTC ISO_8601 date string
     */
    public static String toLocalDateString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDate().format(localDateFormatter);
    }

    public static Optional<ZonedDateTime> fromLocalDateOnlyString(String localDateString) {
        try {
            ZonedDateTime utcTime = LocalDate.parse(localDateString).atStartOfDay(ZoneOffset.UTC);
            return Optional.of(utcTime);
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }
}
