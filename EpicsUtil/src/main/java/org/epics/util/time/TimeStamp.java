/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

import java.util.Date;

/**
 * Represents a time stamp at nanosecond accuracy. The time is internally stored
 * as a 64 + 32 bit value: number of seconds from the Java/UNIX epoch 
 * (1/1/1970) and the nanoseconds past that instant. 
 * The range is of 292 billion years before and another 292 past the epoch.
 * <p>
 * Note that while TimeStamp are usually created according to system clocks which
 * may take into account leap seconds, all the math operations on TimeStamps do
 * not take leap seconds into account.
 * <p>
 * <h3>JSR 310 compatibility</h3>
 * This class is essentially equivalent to {@code javax.time.Instant}.
 * When it will be released, the plan is to phase out
 * this class in the same way that {@link java.util.Date} is going to be phased out.
 * As of 2012/05/07, there are still multiple proposal on the table on that will
 * work, but in all cases it requires having this class implement a single
 * method interface and make all clients use that single method interface.
 *
 * @author carcassi
 */
public class TimeStamp implements Comparable<TimeStamp> {

    /*
     * When the class is initialized, get the current timestamp and nanotime,
     * so that future instances can be calculated from this reference.
     */
    private static final TimeStamp base = TimeStamp.of(new Date());
    private static final long baseNano = System.nanoTime();

    /**
     * Unix timestamp
     */
    private final long unixSec;

    /**
     * Nanoseconds past the timestamp. Must be 0 < nanoSec < 999,999,999
     */
    private final int nanoSec;

    private TimeStamp(long unixSec, int nanoSec) {
        if (nanoSec < 0 || nanoSec > 999999999)
            throw new IllegalArgumentException("Nanoseconds must be between 0 and 999,999,999");
        this.unixSec = unixSec;
        this.nanoSec = nanoSec;
    }

    /**
     * Unix time; seconds from midnight 1/1/1970.
     * @return unix time
     */
    public long getSec() {
        return unixSec;
    }

    /**
     * Nanoseconds within the given second.
     * @return nanoseconds (0 < nanoSec < 999,999,999)
     */
    public int getNanoSec() {
        return nanoSec;
    }

    /**
     * Returns a new timestamp from Java/UNIX time.
     *
     * @param epochSec number of seconds in the Java/UNIX epoch.
     * @param nanoSec nanoseconds past the given seconds (must be 0 < nanoSec < 999,999,999)
     * @return a new timestamp
     */
    public static TimeStamp of(long epochSec, int nanoSec) {
        return new TimeStamp(epochSec, nanoSec);
    }

    /**
     * Converts a {@link java.util.Date} to a timestamp. Date is accurate to
     * milliseconds, so the last 6 digits are always going to be zeros.
     *
     * @param date the date to convert
     * @return a new timestamp
     */
    public static TimeStamp of(Date date) {
        long time = date.getTime();
        int nanoSec = (int) (time % 1000) * 1000000;
        long epochSec = (time / 1000);
        return of(epochSec, nanoSec);
    }

    /**
     * Returns a new timestamp for the current instant. The timestamp is calculated
     * using {@link java.lang.System#nanoTime()}, so it has the accuracy given
     * by that function.
     *
     * @return a new timestamp
     */
    public static TimeStamp now() {
        return base.plus(TimeDuration.ofNanos(System.nanoTime() - baseNano));
    }

    /**
     * Converts the time stamp to a standard Date. The conversion is done once,
     * and it trims all precision below milliSec.
     *
     * @return a date
     */
    public Date toDate() {
        return new Date((unixSec)*1000+nanoSec/1000000);
    }

    @Override
    public int hashCode() {
        return Long.valueOf(nanoSec).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeStamp) {
            TimeStamp other = (TimeStamp) obj;
            return other.nanoSec == nanoSec && other.unixSec == unixSec;
        }

        return false;
    }

    /**
     * Defines the natural ordering for timestamp as forward in time.
     *
     * @param o another object
     * @return comparison result
     */
    @Override
    public int compareTo(TimeStamp other) {
	if (unixSec < other.unixSec) {
            return -1;
        } else if (unixSec == other.unixSec) {
            if (nanoSec < other.nanoSec) {
                return -1;
            } else if (nanoSec == other.nanoSec) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    /**
     * Adds the given duration to this timestamp and returns the result.
     * @param duration a time duration
     * @return a new timestamp
     */
    public TimeStamp plus(TimeDuration duration) {
        return createWithCarry(unixSec + duration.getSec(), nanoSec + duration.getNanoSec());
    }

    /**
     * Creates a new time stamp by carrying nanosecs into seconds if necessary.
     *
     * @param seconds new seconds
     * @param ofNanos new nanoseconds (can be the whole long range)
     * @return the new timestamp
     */
    private static TimeStamp createWithCarry(long seconds, long nanos) {
        if (nanos > 999999999) {
            seconds = seconds + nanos / 1000000000;
            nanos = nanos % 1000000000;
        }

        if (nanos < 0) {
            long pastSec = nanos / 1000000000;
            pastSec--;
            seconds += pastSec;
            nanos -= pastSec * 1000000000;
        }

        return new TimeStamp(seconds, (int) nanos);
    }

    /**
     * Subtracts the given duration to this timestamp and returns the result.
     * @param duration a time duration
     * @return a new timestamp
     */
    public TimeStamp minus(TimeDuration duration) {
        return createWithCarry(unixSec - duration.getSec(), nanoSec - duration.getNanoSec());
    }

    @Override
    public String toString() {
        return unixSec + "." + nanoSec;
    }

    /**
     * Calculates the time passed from the reference to this timeStamp.
     * The resulting duration is the absolute value, so it does not matter
     * on which object the function is called.
     *
     * @param reference another time stamp
     * @return the duration between the two timeStamps
     */
    public TimeDuration durationFrom(TimeStamp reference) {
        long nanoSecDiff = reference.nanoSec - nanoSec;
        nanoSecDiff += (reference.unixSec - unixSec) * 1000000000;
        nanoSecDiff = Math.abs(nanoSecDiff);
        return TimeDuration.ofNanos(nanoSecDiff);
    }

}
