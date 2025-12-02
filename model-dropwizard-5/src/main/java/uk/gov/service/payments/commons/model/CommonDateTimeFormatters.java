package uk.gov.service.payments.commons.model;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class CommonDateTimeFormatters {

    /**
     * DateTimeFormatter that produces a standard ISO-8601 format date and time
     * in UTC with millisecond precision (three fractional second digits, zero
     * right-padded if necessary), for example 2015-10-21T07:28:00.000Z
     */
    public static final DateTimeFormatter ISO_INSTANT_MILLISECOND_PRECISION =
            new DateTimeFormatterBuilder().appendInstant(3).toFormatter(Locale.ENGLISH);

    /**
     * DateTimeFormatter that produces a standard ISO-8601 format date and time
     * in UTC with microsecond precision (six fractional second digits, zero
     * right-padded if necessary), for example 2015-10-21T07:28:00.000000Z
     */
    public static final DateTimeFormatter ISO_INSTANT_MICROSECOND_PRECISION =
            new DateTimeFormatterBuilder().appendInstant(6).toFormatter(Locale.ENGLISH);

    /**
     * DateTimeFormatter that produces a standard ISO-8601 local date in UTC
     * without an offset, for example 2022-10-04
     */
    public static final DateTimeFormatter ISO_LOCAL_DATE_IN_UTC =
            DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC);

}
