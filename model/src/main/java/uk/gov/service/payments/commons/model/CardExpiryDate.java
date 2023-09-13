package uk.gov.service.payments.commons.model;

import java.time.YearMonth;
import java.util.Objects;
import java.util.regex.Pattern;

public class CardExpiryDate {

    public static final Pattern CARD_EXPIRY_DATE_PATTERN = Pattern.compile("(0[1-9]|1[0-2])/([0-9]{2})");
    private static final String PREFIX_TO_MAKE_2_DIGIT_YEAR_INTO_4_DIGIT_YEAR = "20";
    private static final YearMonth MIN_EXPIRY_DATE = YearMonth.of(2000, 1);
    private static final YearMonth MAX_EXPIRY_DATE = YearMonth.of(2099, 12);

    private final String month2Digits;
    private final String year2Digits;

    private CardExpiryDate(String expiryDate) {
        Objects.requireNonNull(expiryDate, "expiryDate");

        var matcher = CARD_EXPIRY_DATE_PATTERN.matcher(expiryDate);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not in MM/yy format: " + expiryDate);
        }

        this.month2Digits = matcher.group(1);
        this.year2Digits = matcher.group(2);
    }
    
    private CardExpiryDate(YearMonth expiryDate) {
        if (expiryDate.isBefore(MIN_EXPIRY_DATE) || expiryDate.isAfter(MAX_EXPIRY_DATE)) {
            throw new IllegalArgumentException("Expiry date must be in the range 01/2000 - 12/2099");
        }
        int month = expiryDate.getMonthValue();
        this.month2Digits = String.format("%02d", month);
        this.year2Digits = String.valueOf(expiryDate.getYear()).substring(2,4);
    }
    
    /**
     * Parses a string in the MM/yy format used for expiry dates on most
     * payment cards into a CardExpiryDate. For example, "09/22".
     *
     * Both the month and the year must be 2 digits, zero-padded if necessary
     * (so "09" rather than "9"), thus the string must be exactly 5 characters
     * in total.
     **/
    public static CardExpiryDate valueOf(String expiryDate) {
        return new CardExpiryDate(expiryDate);
    }
    
    public static CardExpiryDate valueOf(YearMonth expiryDate) {
        return new CardExpiryDate(expiryDate);
    }
    
    public String getTwoDigitMonth() {
        return month2Digits;
    }

    public String getTwoDigitYear() {
        return year2Digits;
    }

    public String getFourDigitYear() {
        return PREFIX_TO_MAKE_2_DIGIT_YEAR_INTO_4_DIGIT_YEAR + year2Digits;
    }

    public YearMonth toYearMonth() {
        var month = Integer.parseInt(month2Digits);
        var year = Integer.parseInt(getFourDigitYear());
        return YearMonth.of(year, month);
    }

    /**
     * Returns the card expiry date in the MM/yy format used for expiry dates
     * on most payment cards. For example, "09/22".
     */
    @Override
    public String toString() {
        return month2Digits + '/' + year2Digits;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || other.getClass() != CardExpiryDate.class) {
            return false;
        }

        var that = (CardExpiryDate) other;
        return this.month2Digits.equals(that.month2Digits) && this.year2Digits.equals(that.year2Digits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month2Digits, year2Digits);
    }

}
