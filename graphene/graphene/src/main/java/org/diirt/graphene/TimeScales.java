/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.diirt.util.array.ArrayInt;
import org.diirt.util.time.TimeInterval;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


/**
 *
 * @author carcassi
 */
public class TimeScales {

    final public static int HOUR_FIELD_ID = GregorianCalendar.HOUR_OF_DAY;
    final public static int FIRST_HOUR = 0;
    final public static int DAY_FIELD_ID = GregorianCalendar.DAY_OF_WEEK;
    final public static int FIRST_DAY = 1;
    final public static int WEEK_FIELD_ID = GregorianCalendar.WEEK_OF_MONTH;
    final public static int FIRST_WEEK = 1;

    public static TimeScale linearAbsoluteScale() {
        return new LinearAbsoluteTimeScale();
    }

    static class TimePeriod {
        public final int fieldId;
        public final double amount;

        public TimePeriod(int fieldId, double amount) {
            this.fieldId = fieldId;
            this.amount = amount;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + this.fieldId;
            hash = 59 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TimePeriod other = (TimePeriod) obj;
            if (this.fieldId != other.fieldId) {
                return false;
            }
            if (Double.doubleToLongBits(this.amount) != Double.doubleToLongBits(other.amount)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "TimePeriod{" + "fieldId=" + fieldId + ", amount=" + amount + '}';
        }

    }

    static TimePeriod nextUp(TimePeriod period) {
        //TODO nanoseconds and year rounding up
        switch(period.fieldId) {
            case GregorianCalendar.MILLISECOND:
                if (period.amount < 2) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 2);
                }
                if (period.amount < 5) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 5);
                }
                if (period.amount < 10) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 10);
                }
                if (period.amount < 20) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 20);
                }
                if (period.amount < 50) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 50);
                }
                if (period.amount < 100) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 100);
                }
                if (period.amount < 200) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 200);
                }
                if (period.amount < 500) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 500);
                }
                return new TimePeriod(GregorianCalendar.SECOND, 1);
            case GregorianCalendar.SECOND:
                if (period.amount < 2) {
                    return new TimePeriod(GregorianCalendar.SECOND, 2);
                }
                if (period.amount < 5) {
                    return new TimePeriod(GregorianCalendar.SECOND, 5);
                }
                if (period.amount < 10) {
                    return new TimePeriod(GregorianCalendar.SECOND, 10);
                }
                if (period.amount < 15) {
                    return new TimePeriod(GregorianCalendar.SECOND, 15);
                }
                if (period.amount < 30) {
                    return new TimePeriod(GregorianCalendar.SECOND, 30);
                }
                return new TimePeriod(GregorianCalendar.MINUTE, 1);
            case GregorianCalendar.MINUTE:
                if ( period.amount < 2 ) {
                    return new TimePeriod( GregorianCalendar.MINUTE , 2 );
                }
                if ( period.amount < 5 ) {
                    return new TimePeriod( GregorianCalendar.MINUTE , 5 );
                }
                if ( period.amount < 10 ) {
                    return new TimePeriod( GregorianCalendar.MINUTE , 10 );
                }
                if ( period.amount < 15 ) {
                    return new TimePeriod( GregorianCalendar.MINUTE , 15 );
                }
                if ( period.amount < 30 ) {
                    return new TimePeriod( GregorianCalendar.MINUTE , 30 );
                }
                return new TimePeriod( HOUR_FIELD_ID, 1 );
            case HOUR_FIELD_ID:
                if ( period.amount < 2 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 2 );
                }
                if ( period.amount < 3 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 3 );
                }
                if ( period.amount < 6 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 6 );
                }
                if ( period.amount < 12 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 12 );
                }

                //*MC why is this necessary? otherwise, we get error
                if ( period.amount < 24 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 24 );
                }
                return new TimePeriod( DAY_FIELD_ID , 1 );
            case DAY_FIELD_ID:
                if ( period.amount < 2 ) {
                    return new TimePeriod( DAY_FIELD_ID , 2 );
                }
                if ( period.amount < 4 ) {
                    return new TimePeriod( DAY_FIELD_ID , 4 );
                }
                return new TimePeriod( WEEK_FIELD_ID , 1 );
            case WEEK_FIELD_ID:
                if ( period.amount < 2 ) {
                    return new TimePeriod( WEEK_FIELD_ID , 2 );
                }
                return new TimePeriod( GregorianCalendar.MONTH , 1 );
            case GregorianCalendar.MONTH:
                if ( period.amount < 2 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 2 );
                }
                if ( period.amount < 4 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 4 );
                }
                if ( period.amount < 8 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 8 );
                }
                return new TimePeriod( GregorianCalendar.YEAR , 1 );
            case GregorianCalendar.YEAR:
                return new TimePeriod( GregorianCalendar.YEAR , period.amount/4+1 );
        }
        return null;
    }

    /**
     * Determines the time(s) that will be represented by reference lines on
     * a time graph.
     *
     * @param timeInterval the interval of time spanning the duration of the
     * time graph
     * @param period the interval of time between each reference line
     * @return a list of times evenly spaced by the duration of <code>period</code>
     * and encompassing the duration of <code>timeInterval</code>
     */
    static List<Instant> createReferences(TimeInterval timeInterval, TimePeriod period) {
        Date start = Date.from(timeInterval.getStart());
        Date end = Date.from(timeInterval.getEnd());
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(end);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(start);
        round(cal, period.fieldId);
        cal.set(period.fieldId, (cal.get(period.fieldId) / (int) period.amount) * (int) period.amount);
        List<Instant> references = new ArrayList<>();
        while (endCal.compareTo(cal) >= 0) {
            Instant newTime = cal.getTime().toInstant();
            if (timeInterval.contains(newTime)) {
                references.add(newTime);
            }
            cal.add(period.fieldId, (int) period.amount);
        }
        return references;
    }

    static void round(GregorianCalendar cal, int field) {

        if (GregorianCalendar.MILLISECOND == field) {
            return;
        }
        cal.set(GregorianCalendar.MILLISECOND, 0);

        if (GregorianCalendar.SECOND == field) {
            return;
        }
        cal.set(GregorianCalendar.SECOND, 0);

        if (GregorianCalendar.MINUTE == field) {
            return;
        }
        cal.set(GregorianCalendar.MINUTE, 0);

        if ( HOUR_FIELD_ID == field ) {
            return;
        }
        cal.set( HOUR_FIELD_ID , FIRST_HOUR );

        if ( DAY_FIELD_ID == field ) {
            return;
        }
        cal.set(DAY_FIELD_ID , FIRST_DAY );

        if ( WEEK_FIELD_ID == field ) {
            return;
        }

        //here, we are rounding down to the first week (i.e. the first day of
        //the month), so the day of the week and the week of the month no
        //longer matter - we just set the day to be the first day of the month
        cal.set( GregorianCalendar.DAY_OF_MONTH , 1 );

        if ( GregorianCalendar.MONTH == field ) {
            return;
        }

        cal.set( GregorianCalendar.MONTH , 0 );

        if ( GregorianCalendar.YEAR == field ) {
            return;
        }
        cal.set( GregorianCalendar.YEAR , 0 );

    }

    static TimePeriod nextDown(TimePeriod period) {
        switch(period.fieldId) {
            case GregorianCalendar.YEAR:
                return new TimePeriod( GregorianCalendar.YEAR , period.amount/4+1 );
            case GregorianCalendar.MONTH:
                if ( period.amount > 8 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 8 );
                }
                if ( period.amount > 4 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 4 );
                }
                if ( period.amount > 2 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 2 );
                }
                if ( period.amount > 1 ) {
                    return new TimePeriod( GregorianCalendar.MONTH , 1 );
                }
                return new TimePeriod( WEEK_FIELD_ID , 2 );
            case WEEK_FIELD_ID:
                if ( period.amount > 2 ) {
                    return new TimePeriod( WEEK_FIELD_ID , 2 );
                }
                if ( period.amount > 1 ) {
                    return new TimePeriod( WEEK_FIELD_ID , 1 );
                }
                return new TimePeriod( DAY_FIELD_ID , 3 );
            case DAY_FIELD_ID:
                if ( period.amount > 3 ) {
                    return new TimePeriod( DAY_FIELD_ID , 3 );
                }
                if ( period.amount > 1 ) {
                    return new TimePeriod( DAY_FIELD_ID , 1 );
                }
                return new TimePeriod( HOUR_FIELD_ID , 12 );
            case HOUR_FIELD_ID:
                if ( period.amount > 12 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 12 );
                }
                if ( period.amount > 8 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 8 );
                }
                if ( period.amount > 4 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 4 );
                }
                if ( period.amount > 2 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 2 );
                }
                if ( period.amount > 1 ) {
                    return new TimePeriod( HOUR_FIELD_ID , 1 );
                }
                return new TimePeriod( GregorianCalendar.MINUTE , 30 );
            case GregorianCalendar.MINUTE:
                return new TimePeriod(GregorianCalendar.SECOND, 30);
            case GregorianCalendar.SECOND:
                if (period.amount > 30) {
                    return new TimePeriod(GregorianCalendar.SECOND, 30);
                }
                if (period.amount > 15) {
                    return new TimePeriod(GregorianCalendar.SECOND, 15);
                }
                if (period.amount > 10) {
                    return new TimePeriod(GregorianCalendar.SECOND, 10);
                }
                if (period.amount > 5) {
                    return new TimePeriod(GregorianCalendar.SECOND, 5);
                }
                if (period.amount > 2) {
                    return new TimePeriod(GregorianCalendar.SECOND, 2);
                }
                if (period.amount > 1) {
                    return new TimePeriod(GregorianCalendar.SECOND, 1);
                }
                return new TimePeriod(GregorianCalendar.MILLISECOND, 500);
            case GregorianCalendar.MILLISECOND:
                if (period.amount > 500) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 500);
                }
                if (period.amount > 200) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 200);
                }
                if (period.amount > 100) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 100);
                }
                if (period.amount > 50) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 50);
                }
                if (period.amount > 20) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 20);
                }
                if (period.amount > 10) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 10);
                }
                if (period.amount > 5) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 5);
                }
                if (period.amount > 2) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 2);
                }
                if (period.amount > 1) {
                    return new TimePeriod(GregorianCalendar.MILLISECOND, 1);
                }
        }
        return new TimePeriod( GregorianCalendar.MILLISECOND , 1 );

        //TODO nanoseconds and year rounding down
    }

    static TimePeriod toTimePeriod(double seconds) {
        if ( seconds >= 36288000 ) {
            return new TimePeriod( GregorianCalendar.YEAR , seconds/3024000 );
        }
        if ( seconds >= 3024000 ) {
            return new TimePeriod( GregorianCalendar.MONTH , seconds/3024000 );
        }
        if ( seconds >= 604800 ) {
            return new TimePeriod( WEEK_FIELD_ID , seconds/604800.0 );
        }
        if ( seconds >= 86400 ) {
            return new TimePeriod( DAY_FIELD_ID , seconds/86400.0 );
        }
        if ( seconds >= 3600 ) {
            return new TimePeriod( HOUR_FIELD_ID , seconds/3600.0 );
        }
        if (seconds >= 60) {
            return new TimePeriod(GregorianCalendar.MINUTE, seconds / 60.0);
        }
        if (seconds >= 1) {
            return new TimePeriod(GregorianCalendar.SECOND, seconds);
        }
        return new TimePeriod(GregorianCalendar.MILLISECOND, 1000*seconds);
    }

    static double normalize(Instant time, TimeInterval timeInterval) {
        // XXX: if interval is more than 292 years, this will not work
        double range = timeInterval.getStart().until(timeInterval.getEnd(), ChronoUnit.NANOS);
        double value = timeInterval.getStart().until(time, ChronoUnit.NANOS);
        return value / range;
    }

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.nnnnnnnnn");
    private static ArrayInt possibleStopFromEnd = new ArrayInt(0,1,2,3,4,5,6,7,8,10,13,19,22,25,28);
    private static ArrayInt possibleStopFromStart = new ArrayInt(0,11,19,28);
    private static String zeroFormat = "0000/01/01 00:00:00.000000000";

    static List<String> createLabels(List<Instant> timestamps) {
        if (timestamps.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>(timestamps.size());
        for (Instant timestamp : timestamps) {
            ZonedDateTime z = ZonedDateTime.ofInstant(timestamp, ZoneId.systemDefault());
            System.out.println(z.toInstant());
            System.out.println(z.format(format));
            result.add(format.format(ZonedDateTime.ofInstant(timestamp, ZoneId.systemDefault())));
        }

        return result;
    }

    /**
     * Trims a list of time axis labels to remove unnecessary redundancy.
     *
     * @param labels a list of dates that will be displayed on a time axis
     * @return a list of modified dates without redundancy that can be displayed
     * on a time axis
     */
    static List<String> trimLabels( List< String > labels ) {

        //special case: if there are 1 or fewer labels, we cannot do redundance
        //checking or precision checking because there aren't enough
        //labels to compare with each other
        if ( labels.size() <= 1 ) {
            return labels;
        }

        //first, we calculate the greatest changing precision amongst all the
        //labels. This is the precision that needs to be maintained throughout
        //all the labels
        int greatestChangingPrecision = calculateGreatestChangingField( labels );

        ArrayList< String > rtn = new ArrayList< String >( labels.size() );
        DateTrimmer firstDate = new DateTrimmer( labels.get( 0 ) );

        //the first date will need to display all information, even if it is
        //redundant; however, we can drop some trailing 0s, up to the
        //precision that is changing
        rtn.add( firstDate.getCompactForm( DateTrimmer.NO_PRECISION , greatestChangingPrecision ) );
        for ( int i=1 ; i<labels.size() ; i++ ) {
            String prevLabel = labels.get( i-1 );
            String nextLabel = labels.get( i );
            int redundancePrecision = greatestRedundancePrecision( prevLabel , nextLabel );
            DateTrimmer f = new DateTrimmer( nextLabel );
            String trimmedLabel = f.getCompactForm( redundancePrecision , greatestChangingPrecision );
            rtn.add( trimmedLabel );
        }
        return rtn;
    }

    /**
     * Calculates the place of greatest precision at which both the last label
     * and the current label are the same
     *
     * @param lastLabel
     * @param currLabel
     * @return
     */
    private static int greatestRedundancePrecision( String lastLabel , String currLabel ) {
        int[] lastLabelFields = DateTrimmer.parseFields( lastLabel );
        int[] currLabelFields = DateTrimmer.parseFields( currLabel );
        for ( int i=0 ; i<lastLabelFields.length ; i++ ) {
            if ( lastLabelFields[ i ] != currLabelFields[ i ] ) {
                return i-1;
            }
        }
        return lastLabelFields.length-1;
    }

    /**
     * Calculates the greatest precision at which the last label and the
     * current label are changing. The list of labels must be non-empty.
     * If the list of labels is empty, then maximum precision is returned.
     *
     * @param labels
     * @return
     */
    private static int calculateGreatestChangingField( List< String > labels ) {
        int[][] fields = new int[ labels.size() ][];
        for ( int i=0 ; i<labels.size() ; i++ ) {
            fields[ i ] = DateTrimmer.parseFields( labels.get( i ) );
        }
        int rtn = -1;
        for ( int fieldId=fields[ 0 ].length-1 ; fieldId >= 0 ; fieldId-- ) {
            int referenceFieldValue = fields[ 0 ][ fieldId ];
            for ( int i=1 ; i<labels.size() ; i++ ) {
                if ( fields[ i ][ fieldId ] != referenceFieldValue ) {
                    rtn = fieldId;
                    break;
                }
            }
            if ( rtn != -1 ) {
                break;
            }
        }
        if ( rtn == -1 ) {
            return DateTrimmer.NANOSECOND_PRECISIONS[9];
        }

        //determine the latest decimal place at which the nanoseconds are nonzero
        //because we will need to maintain precision to that decimal place
        //for all labels
        if ( rtn == DateTrimmer.NANOSECOND_PRECISION ) {

            int modulus = 10;
            for ( int decimalPlace = 9; decimalPlace >= 1 ; decimalPlace-- ) {
                boolean isDecimalPlaceZero = true;
                for ( int i=0 ; i<labels.size() ; i++ ) {
                    if ( fields[ i ][ DateTrimmer.NANOSECOND_PRECISION ] % modulus != 0 ) {
                        isDecimalPlaceZero = false;
                    }
                }
                if ( !isDecimalPlaceZero ) {
                    return DateTrimmer.NANOSECOND_PRECISIONS[ decimalPlace ];
                }
                modulus *= 10;
            }
            return DateTrimmer.NANOSECOND_PRECISIONS[0];
        }
        return rtn;
    }

    /**
     * Trims a date given specific redundancy and precision bounds.
     *
     * <p>
     *
     * For example, suppose we have the dates
     * <ul>
     * <li> "2014/11/26 09:01:00.000000000"
     * <li> "2014/11/26 09:02:00.020000000"
     * <li> "2014/11/26 09:03:00.040000000"
     * <li> "2014/11/26 09:04:00.060000000"
     * <li> "2014/11/26 09:05:00.080000000"
     * </ul>
     * and we want to use them as labels on a time axis. It would be
     * redundant to display "2014/11/26" on every label. Thus, "2014/11/26"
     * is redundant and needs to be removed. Similarly, the trailing 0s are
     * redundant and we only need to maintain 2 decimal places of nanosecond
     * precision. <code>DateTrimmer</code> allows us to arbitrarily decide
     * if we want to display years, months, days, etc., and then builds the
     * date label.
     *
     * <p>
     *
     * Precisions are given as follows:
     * <ul>
     * <li> -1 = NO PRECISION
     * <li> 0 = YEAR
     * <li> 1 = MONTH
     * <li> 2 = DAY
     * <li> 3 = HOUR
     * <li> 4 = MINUTE
     * <li> 5 = SECOND
     * <li> 6 = NANOSECOND, 1 digit of precision
     * <li> 7 = NANOSECOND, 2 digits of precision
     * <li> 8 = NANOSECOND, 3 digits of precision
     * <li> 9 = NANOSECOND, 4 digits of precision
     * <li> 10 = NANOSECOND, 5 digits of precision
     * <li> 11 = NANOSECOND, 6 digits of precision
     * <li> 12 = NANOSECOND, 7 digits of precision
     * <li> 13 = NANOSECOND, 8 digits of precision
     * <li> 14 = NANOSECOND, 9 digits of precision
     * <li> 1000 = INFINITE PRECISION
     * </ul>
     * This ordering can be used for comparison. For example, years are less
     * precise than months, so they have a smaller precision value.
     *
     * <p>
     *
     * When removing redundant parts from the front of a date, all precision
     * from YEARS up to the given common precision will be removed. When
     * removing redundant parts from the end of a date, only a required
     * precision will be requested. The required precision is the most precise
     * precision that the date must maintain. Everything more precise than the
     * required precision will be removed.
     *
     * <p>
     *
     * The <code>DateTrimmer</code> works by allowing redundancy checks to
     * turn off years, months, days, etc. from the display. Any special cases
     * can then be handled afterwards, as years, months, days, ... can be
     * turned on again.
     *
     * @author mjchao
     */
    static class DateTrimmer {

        final public static int NO_PRECISION = -1;
        final public static int YEAR_PRECISION = 0;
        final public static int MONTH_PRECISION = 1;
        final public static int DAY_PRECISION = 2;
        final public static int HOUR_PRECISION = 3;
        final public static int MINUTE_PRECISION = 4;
        final public static int SECOND_PRECISION = 5;
        final public static int NANOSECOND_PRECISION = 6;
        final public static int[] NANOSECOND_PRECISIONS = { 5 , 6 , 7 , 8 , 9 , 10 , 11 , 12 , 13 , 14 };
        final public static int INFINITE_PRECISION = 1000;

        private int m_year;
        private boolean m_showYear = true;
        private int m_month;
        private boolean m_showMonth = true;
        private int m_day;
        private boolean m_showDay = true;
        private int m_hour;
        private boolean m_showHour = true;
        private int m_minute;
        private boolean m_showMinute = true;
        private int m_second;
        private boolean m_showSecond = true;
        private int m_nanosecond;
        private int m_nanosecondDecimalPlace = 9;
        private boolean m_showNanosecond = true;

        /**
         * Creates a <code>DateTrimmer</code> with the given date fields
         *
         * @param year
         * @param month
         * @param day
         * @param hour
         * @param minute
         * @param second
         * @param nanosecond
         */
        public DateTrimmer( int year , int month , int day , int hour , int minute , int second , int nanosecond ) {
            this.m_year = year;
            this.m_month = month;
            this.m_day = day;
            this.m_hour = hour;
            this.m_minute = minute;
            this.m_second = second;
            this.m_nanosecond = nanosecond;
        }

        /**
         * Creates a <code>DateTrimmer</code> for the given date
         *
         * @param date
         */
        public DateTrimmer( String date ) {
            int[] fields = parseFields( date );
            this.m_year = fields.length >= 1 ? fields[ 0 ] : 0;
            this.m_month = fields.length >= 2 ? fields[ 1 ] : 0;
            this.m_day = fields.length >= 3 ? fields[ 2 ] : 0;
            this.m_hour = fields.length >= 4 ? fields[ 3 ] : 0;
            this.m_minute = fields.length >= 5 ? fields[ 4 ] : 0;
            this.m_second = fields.length >= 6 ? fields[ 5 ] : 0;
            this.m_nanosecond = fields.length >= 7 ? fields[ 6 ] : 0;
        }

        /**
         * Parses the years, months, days, etc. from a complete date string.
         * If the date string is not of the form YYYY/MM/DD HH/MM/SS.NNNNNNNNN
         * (N=Nanoseconds) then there is no guarantee as to what fields are
         * parsed.
         *
         * @param date a date to parse
         * @return the years, months, day, hour, minute, second, and nanosecond
         * of the date. These properties correspond to indices 0, 1, 2, 3, 4, 5,
         * 6, respectively in the returned array
         */
        protected static int[] parseFields( String date ) {
            String[] fields = date.split( "/|:| |\\." );
            int[] rtn = new int[ fields.length ];
            for ( int i=0 ; i<fields.length ; i++ ) {
                rtn[ i ] = Integer.parseInt( fields[ i ] );
            }
            return rtn;
        }

        /**
         * Generates a compact form of the date by removing trailing zeroes,
         * while maintaining a required precision and removing redundant
         * information on the left side of the text. if the common precision
         * exceeds the required precision, then an empty label is produced.
         *
         * @param commonPrecision the parts of the date that can be left out because
         * they are common to other dates
         * @param requiredPrecision the minimum precision the compact form must have
         * @return the compact form of the date
         */
        public String getCompactForm( int commonPrecision , int requiredPrecision ) {

            //special case when the common precision exceeds the required precision
            //this means that all the labels are identicial, so the labels would be empty
            if ( commonPrecision >= requiredPrecision ) {
                return "";
            }
            removeRedundantPrecision( commonPrecision );
            maintainRequiredPrecision( requiredPrecision );

            //handle special cases:

            //days must be shown with months regardless of redundancies.
            //the label 09 is meaningless because it could be a day or a month;
            //however, the label 05/09 is unambiguous.
            if ( this.m_showDay && !this.m_showMonth ) {
                this.m_showMonth = true;
            }

            //months must be shown with a day or a year, regardless of
            //redundancies. the label 10 is meaningless because it could be
            //a day or a month; however, the labsl 2014/10 and 10/19 are
            //unambiguous.
            if ( this.m_showMonth && !this.m_showDay && !this.m_showYear ) {
                if ( requiredPrecision >= DAY_PRECISION ) {
                    this.m_showDay = true;
                }
                else {
                    this.m_showYear = true;
                }
            }

            //hours must be shown with minutes, regardless of redundancies
            //the label 12 is meaningless because it could be a day, month,
            //minute, second, etc; however, the label 12:12 is less ambiguous
            //as it could only be hour:minutes or minutes:seconds. hopefully
            //hour:minutes can then be inferred from the other labels.
            if ( this.m_showHour && !this.m_showMinute ) {
                this.m_showMinute = true;
            }
            return buildDateString();
        }

        /**
         * Removes redundant precision from a given date string; however, hours,
         * minutes, seconds, and nanoseconds must not be removed for redundancy
         * because that gives rise to ambiguous times such as 9:15. The time
         * 9:15 could then be 9 hours 15 minutes or 9 minutes 15 seconds
         * and we resolve this by defining these ambiguous times to be the
         * least precise possible.
         *
         * @param redundantPrecision the precision to which the date is redundant
         */
        void removeRedundantPrecision( int redundantPrecision ) {
            if ( redundantPrecision >= YEAR_PRECISION ) {
                this.m_showYear = false;

                if ( redundantPrecision >= MONTH_PRECISION ) {
                    this.m_showMonth = false;

                    if ( redundantPrecision >= DAY_PRECISION ) {
                        this.m_showDay = false;
                    }
                }
            }
        }

        /**
         * Ensures that the specified precision is maintained when
         * creating a final, trimmed string
         *
         * @param requiredPrecision the most precise precision this trimmer
         * must maintain
         */
        void maintainRequiredPrecision( int requiredPrecision ) {

            if ( (this.m_nanosecond == 0) && requiredPrecision < NANOSECOND_PRECISIONS[1] ) {
                this.m_showNanosecond = false;

                if ( (this.m_second == 0) && requiredPrecision < SECOND_PRECISION ) {
                    this.m_showSecond = false;

                    if ( (this.m_minute == 0) && requiredPrecision < MINUTE_PRECISION ) {
                        this.m_showMinute = false;

                        if ( (this.m_hour == 0) && requiredPrecision < HOUR_PRECISION ) {
                            this.m_showHour = false;

                            if ( (this.m_day == 1) && requiredPrecision < DAY_PRECISION ) {
                                this.m_showDay = false;

                                if ( (this.m_month == 1) && requiredPrecision < MONTH_PRECISION ) {
                                    this.m_showMonth = false;
                                }
                            }
                        }
                    }
                }
            }

            else {

                //if we must display at a nanoseconds detail level,
                //then we must determine the number of decimal places to display
                if ( requiredPrecision >= NANOSECOND_PRECISIONS[ 1 ] ) {
                    //determine the number of demical places to which we display
                    //nanoseconds
                    for ( int i=1 ; i<NANOSECOND_PRECISIONS.length ; i++ ) {
                        if ( requiredPrecision == NANOSECOND_PRECISIONS[ i ] ) {
                            this.m_nanosecondDecimalPlace = i;
                        }
                    }
                }

                //if nanoseconds need to be displayed just because they are
                //nonzero, then we can just remove trailing zeroes
                else if ( this.m_nanosecond != 0 ) {
                    String withoutTrailingZeroes = removeTrailingZeroes( createNumericalString( this.m_nanosecond , 9 ) );
                    this.m_nanosecondDecimalPlace = withoutTrailingZeroes.length();
                }
            }
        }

        /**
         * Creates the label for the date stored in this <code>DateTrimmer</code>
         * given the status of the year, month, day, etc. indicators.
         *
         * @return
         */
        private String buildDateString() {
            String rtn = "";
            if ( this.m_showYear ) {
                rtn += createNumericalString( this.m_year , 4 );
                if ( this.m_showMonth ) {
                    rtn += "/";
                }
            }
            if ( this.m_showMonth ) {
                rtn += createNumericalString( this.m_month , 2 );
                if ( this.m_showDay ) {
                    rtn += "/";
                }
            }
            if ( this.m_showDay ) {
                rtn += createNumericalString( this.m_day , 2 );
                if ( this.m_showHour ) {
                    rtn += " ";
                }
            }
            if ( this.m_showHour ) {
                rtn += createNumericalString( this.m_hour , 2 );
                if ( this.m_showMinute ) {
                    rtn += ":";
                }
            }
            if ( this.m_showMinute ) {
                rtn += createNumericalString( this.m_minute , 2 );
                if ( this.m_showSecond ) {
                    rtn += ":";
                }
            }
            if ( this.m_showSecond ) {
                rtn += createNumericalString( this.m_second , 2 );
                if ( this.m_showNanosecond ) {
                    rtn += ".";
                }
            }

            if ( this.m_showNanosecond ) {
                rtn += createNumericalString( this.m_nanosecond , 9 ).substring( 0 , this.m_nanosecondDecimalPlace );
            }
            return rtn;
        }

        /**
         * Removes trailing zeros from a string
         *
         * @param value a string
         * @return the inputed string without any trailing zeroes
         */
        private String removeTrailingZeroes( String value ) {
            String rtn = value;
            int lastIdxOfNonzero = rtn.length()-1;
            for ( ; lastIdxOfNonzero >= 0 ; lastIdxOfNonzero-- ) {
                if ( rtn.charAt( lastIdxOfNonzero ) != '0' ) {
                    break;
                }
            }
            return rtn.substring( 0 , lastIdxOfNonzero+1 );
        }
    }

    /**
     * Creates a string from an integer value that has at least the
     * specified length. The integer is padded with 0s to ensure the
     * specified length.
     *
     * @param value the value to convert to a string
     * @param minLength the minimum length the string must have
     * @return the given value as a string with length at least the specified
     * minimum
     */
    static String createNumericalString( int value , int minLength ) {
        String rtn = String.valueOf( value );
        while( rtn.length() < minLength ) {
            rtn = "0" + rtn;
        }
        return rtn;
    }
}
