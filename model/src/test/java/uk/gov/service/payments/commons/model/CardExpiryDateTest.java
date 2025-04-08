package uk.gov.service.payments.commons.model;

import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardExpiryDateTest {

    @Test
    void month01Parses() {
        var input = "01/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("01"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, JANUARY)));
        assertThat(cardExpiryDate.toString(), is("01/22"));
    }

    @Test
    void month02Parses() {
        var input = "02/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("02"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, FEBRUARY)));
        assertThat(cardExpiryDate.toString(), is("02/22"));
    }

    @Test
    void month03Parses() {
        var input = "03/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("03"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, MARCH)));
        assertThat(cardExpiryDate.toString(), is("03/22"));
    }

    @Test
    void month04Parses() {
        var input = "04/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("04"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, APRIL)));
        assertThat(cardExpiryDate.toString(), is("04/22"));
    }

    @Test
    void month05Parses() {
        var input = "05/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("05"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, MAY)));
        assertThat(cardExpiryDate.toString(), is("05/22"));
    }

    @Test
    void month06Parses() {
        var input = "06/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("06"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, JUNE)));
        assertThat(cardExpiryDate.toString(), is("06/22"));
    }

    @Test
    void month07Parses() {
        var input = "07/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("07"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, JULY)));
        assertThat(cardExpiryDate.toString(), is("07/22"));
    }

    @Test
    void month08Parses() {
        var input = "08/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("08"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, AUGUST)));
        assertThat(cardExpiryDate.toString(), is("08/22"));
    }

    @Test
    void month09Parses() {
        var input = "09/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("09"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, SEPTEMBER)));
        assertThat(cardExpiryDate.toString(), is("09/22"));
    }

    @Test
    void month10Parses() {
        var input = "10/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("10"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/22"));
    }

    @Test
    void month11Parses() {
        var input = "11/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("11"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, NOVEMBER)));
        assertThat(cardExpiryDate.toString(), is("11/22"));
    }

    @Test
    void month12Parses() {
        var input = "12/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("12"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, DECEMBER)));
        assertThat(cardExpiryDate.toString(), is("12/22"));
    }

    @Test
    void year22Parses() {
        var input = "10/22";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("22"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2022"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2022, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/22"));
    }

    @Test
    void year00Parses() {
        var input = "10/00";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("00"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2000"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2000, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/00"));
    }

    @Test
    void year01Parses() {
        var input = "10/01";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("01"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2001"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2001, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/01"));
    }

    @Test
    void year09Parses() {
        var input = "10/09";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("09"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2009"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2009, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/09"));
    }

    @Test
    void year30Parses() {
        var input = "10/30";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("30"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2030"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2030, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/30"));
    }

    @Test
    void year99Parses() {
        var input = "10/99";
        var cardExpiryDate = CardExpiryDate.valueOf(input);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("99"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2099"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2099, OCTOBER)));
        assertThat(cardExpiryDate.toString(), is("10/99"));
    }
    
    @Test
    void createSuccessfullyFromYearMonthWithOneDigitMonth() {
        int expiryDateYear = 2023;
        int expiryDateMonth = 8;
        var yearMonth = YearMonth.of(expiryDateYear, expiryDateMonth);
        var cardExpiryDate = CardExpiryDate.valueOf(yearMonth);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("23"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2023"));
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("08"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2023, AUGUST)));
        assertThat(cardExpiryDate.toString(), is("08/23"));
    }

    @Test
    void createSuccessfullyFromYearMonthWithTwoDigitMonth() {
        int expiryDateYear = 2023;
        int expiryDateMonth = 11;
        var yearMonth = YearMonth.of(expiryDateYear, expiryDateMonth);
        var cardExpiryDate = CardExpiryDate.valueOf(yearMonth);
        assertThat(cardExpiryDate.getTwoDigitYear(), is("23"));
        assertThat(cardExpiryDate.getFourDigitYear(), is("2023"));
        assertThat(cardExpiryDate.getTwoDigitMonth(), is("11"));
        assertThat(cardExpiryDate.toYearMonth(), is(YearMonth.of(2023, NOVEMBER)));
        assertThat(cardExpiryDate.toString(), is("11/23"));
    }
    
    @Test
    void dateAfter2100ThrowsException() {
        int expiryDateYear = 2100;
        int expiryDateMonth = 1;
        var yearMonth = YearMonth.of(expiryDateYear, expiryDateMonth);
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(yearMonth));
    }

    @Test
    void dateBefore2000ThrowsException() {
        int expiryDateYear = 1999;
        int expiryDateMonth = 12;
        var yearMonth = YearMonth.of(expiryDateYear, expiryDateMonth);
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(yearMonth));
    }
    
    @Test
    void month1WithNoLeadingZeroThrowsException() {
        var input = "1/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month2WithNoLeadingZeroThrowsException() {
        var input = "2/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month3WithNoLeadingZeroThrowsException() {
        var input = "3/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month4WithNoLeadingZeroThrowsException() {
        var input = "4/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month5WithNoLeadingZeroThrowsException() {
        var input = "5/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month6WithNoLeadingZeroThrowsException() {
        var input = "6/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month7WithNoLeadingZeroThrowsException() {
        var input = "7/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month8WithNoLeadingZeroThrowsException() {
        var input = "8/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month9WithNoLeadingZeroThrowsException() {
        var input = "9/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month13ThrowsException() {
        var input = "13/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void month00ThrowsException() {
        var input = "00/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void yearOpenParenthesis0ThrowsException() {
        var input = "10/(2";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void threeDigitMonthThrowsException() {
        var input = "100/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void fourDigitYearThrowsException() {
        var input = "10/2022";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void singleDigitYearThrowsException() {
        var input = "10/2";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void threeDigitYearThrowsException() {
        var input = "10/222";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void missingMonthThrowsException() {
        var input = "/22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void missingYearThrowsException() {
        var input = "10/";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void missingSlashThrowsException() {
        var input = "1022";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void dashInsteadOfSlashThrowsException() {
        var input = "10-22";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void isoFormatThrowsException() {
        var input = "2022-10";
        assertThrows(IllegalArgumentException.class, () -> CardExpiryDate.valueOf(input));
    }

    @Test
    void cardExpiryDatesWithSameMonthAndYearAreEqual() {
        var cardExpiryDate1 = CardExpiryDate.valueOf("10/22");
        var cardExpiryDate2 = CardExpiryDate.valueOf("10/22");
        assertThat(cardExpiryDate1.equals(cardExpiryDate2), is(true));
        assertThat(cardExpiryDate2.equals(cardExpiryDate1), is(true));
    }

    @Test
    void cardExpiryDatesWithSameMonthAndDifferentYearAreNotEqual() {
        var cardExpiryDate1 = CardExpiryDate.valueOf("10/22");
        var cardExpiryDate2 = CardExpiryDate.valueOf("10/23");
        assertThat(cardExpiryDate1.equals(cardExpiryDate2), is(false));
        assertThat(cardExpiryDate2.equals(cardExpiryDate1), is(false));
    }

    @Test
    void cardExpiryDatesWithDifferentMonthAndSameYearAreNotEqual() {
        var cardExpiryDate1 = CardExpiryDate.valueOf("10/22");
        var cardExpiryDate2 = CardExpiryDate.valueOf("11/22");
        assertThat(cardExpiryDate1.equals(cardExpiryDate2), is(false));
        assertThat(cardExpiryDate2.equals(cardExpiryDate1), is(false));
    }

    @Test
    void cardExpiryDatesWithDifferentMonthAndDifferentYearAreNotEqual() {
        var cardExpiryDate1 = CardExpiryDate.valueOf("10/22");
        var cardExpiryDate2 = CardExpiryDate.valueOf("11/23");
        assertThat(cardExpiryDate1.equals(cardExpiryDate2), is(false));
        assertThat(cardExpiryDate2.equals(cardExpiryDate1), is(false));
    }

    @Test
    void cardExpiryDateNotEqualToEquivalentString() {
        var cardExpiryDate = CardExpiryDate.valueOf("10/22");
        assertThat(cardExpiryDate.equals("10/22"), is(false));
    }

    @Test
    void cardExpiryDateNotEqualToEquivalentYearMonth() {
        var cardExpiryDate = CardExpiryDate.valueOf("10/22");
        var yearMonth = YearMonth.of(2022, 10);
        assertThat(cardExpiryDate.equals(yearMonth), is(false));
    }

    @Test
    void cardExpiryDateNotEqualToNull() {
        var cardExpiryDate = CardExpiryDate.valueOf("10/22");
        assertThat(cardExpiryDate.equals(null), is(false));
    }

    @Test
    void cardExpiryDatesWithSameMonthAndYearHaveSameHashCode() {
        var cardExpiryDate1 = CardExpiryDate.valueOf("10/22");
        var cardExpiryDate2 = CardExpiryDate.valueOf("10/22");
        assertThat(cardExpiryDate1.hashCode(), is(cardExpiryDate2.hashCode()));
    }

}
