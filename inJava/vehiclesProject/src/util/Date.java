package util;
import java.util.StringTokenizer;
/**
 * A Date class that represents a calendar date in mm/dd/yyyy format.
 * Provides validation, string conversion, equality, and comparison methods.
 *
 * @author Alec Cotler
 */
public class Date implements Comparable<Date> {
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUARTERCENTENNIAL = 400;
    public static final int MONTHS_IN_YEAR = 12;
    public static final int MINIMUM_MONTH = 1;
    public static final int MINIMUM_DAY = 1;
    public static final int DAYS_IN_JANUARY = 31;
    public static final int DAYS_IN_FEBRUARY_NON_LEAP = 28;
    public static final int DAYS_IN_FEBRUARY_LEAP = 29;
    public static final int DAYS_IN_MARCH = 31;
    public static final int DAYS_IN_APRIL = 30;
    public static final int DAYS_IN_MAY = 31;
    public static final int DAYS_IN_JUNE = 30;
    public static final int DAYS_IN_JULY = 31;
    public static final int DAYS_IN_AUGUST = 31;
    public static final int DAYS_IN_SEPTEMBER = 30;
    public static final int DAYS_IN_OCTOBER = 31;
    public static final int DAYS_IN_NOVEMBER = 30;
    public static final int DAYS_IN_DECEMBER = 31;

    private int year;
    private int month;
    private int day;


    /**
     * Constructor: parses a date string in "mm/dd/yyyy" format.
     *
     * @param dateStr the date string being processed
     */
    public Date(String dateStr) {
        StringTokenizer st = new StringTokenizer(dateStr, "/");
        this.month = Integer.parseInt(st.nextToken());
        this.day = Integer.parseInt(st.nextToken());
        this.year = Integer.parseInt(st.nextToken());
    }

    /**
     * Checks if the date is a valid calendar date, including leap year logic.
     *
     * @return true if it is a valid calendar date, false if it is not
     */
    public boolean isValid() {
        if (year <= 1000) {
            return false;
        }

        // Months: must be 1–12
        if (month < MINIMUM_MONTH || month > MONTHS_IN_YEAR) {
            return false;
        }

        // Validate day
        if (day < MINIMUM_DAY) {
            return false;
        }
        int daysInThisFebruary = DAYS_IN_FEBRUARY_NON_LEAP;
        if (isLeap()) {
            daysInThisFebruary = DAYS_IN_FEBRUARY_LEAP;
        }
        int maxDays;
        switch (month) {
            case 1:
                maxDays = DAYS_IN_JANUARY;
                break;
            case 2:
                maxDays = daysInThisFebruary;
                break;
            case 3:
                maxDays = DAYS_IN_MARCH;
                break;
            case 4:
                maxDays = DAYS_IN_APRIL;
                break;
            case 5:
                maxDays = DAYS_IN_MAY;
                break;
            case 6:
                maxDays = DAYS_IN_JUNE;
                break;
            case 7:
                maxDays = DAYS_IN_JULY;
                break;
            case 8:
                maxDays = DAYS_IN_AUGUST;
                break;
            case 9:
                maxDays = DAYS_IN_SEPTEMBER;
                break;
            case 10:
                maxDays = DAYS_IN_OCTOBER;
                break;
            case 11:
                maxDays = DAYS_IN_NOVEMBER;
                break;
            case 12:
                maxDays = DAYS_IN_DECEMBER;
                break;
            default:
                return false;
        }

        return day <= maxDays;
    }

    /**
     * Checks if a year is a leap year.
     *
     * @return true if the year is a leap year, false if it is not a leap year
     */
    public boolean isLeap() {
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0) {
                if (this.year % QUARTERCENTENNIAL == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if two dates are equal.
     *
     * @param obj the reference object with which to compare.
     * @return true if the objects are equal, false if they are not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Date other = (Date) obj;
        return month == other.month && day == other.day && year == other.year;
    }

    /**
     * Returns the date in mm/dd/yyyy format.
     *
     * @return the string representation of the date (mm/dd/yyyy)
     */
    @Override
    public String toString() {
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * Returns -1 if this date comes before the other date, 0 if they are the same date,
     * or 1 if this date come after the other date.
     *
     * @param other , the date being compared to
     * @return -1 if the date you are comparing comes before other, 0 if they are the same date, and 1 if the date you are comparing comes after other
     */
    @Override
    public int compareTo(Date other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to null Date");
        }
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    /**
     * Returns this date's year.
     *
     * @return year this date's year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns this date's month.
     *
     * @return month this date's month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns this date's day.
     *
     * @return day this month's day
     */
    public int getDay() {
        return day;
    }
}




