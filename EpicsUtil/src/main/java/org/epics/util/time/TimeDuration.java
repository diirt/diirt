/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

import java.math.BigInteger;

/**
 * A duration of time (such as 3 ofSeconds, 30ms, 1nsec) at the nanosecond precision.
 * The duration is stored a (signed) long, which makes the maximum valid duration
 * to around 292 years. No checks for overflows are done.
 * <p>
 * Note that while TimeStamp are usually created according to system clocks which
 * takes into account leap seconds, all the math operations on TimeStamps do
 * not take leap seconds into account.
 * <h3>JSR 310 compatibility</h3>
 * This class is essentially equivalent to {@code javax.time.Duration}.
 * When it will be released, the plan is to phase out this class where appropriate.
 * 
 * @author carcassi
 */
public class TimeDuration {

    private final long sec;
    private final int nanoSec;
    
    private static final int NANOSEC_IN_SEC = 1000000000;

    private TimeDuration(long sec, int nanoSec) {
        if (nanoSec < 0 || nanoSec >= NANOSEC_IN_SEC)
            throw new IllegalArgumentException("Nanoseconds must be between 0 and 999,999,999. Was " + nanoSec);
        this.nanoSec = nanoSec;
        this.sec = sec;
    }

    public int getNanoSec() {
        return nanoSec;
    }

    public long getSec() {
        return sec;
    }
    
    /**
     * A new duration in hours.
     * 
     * @param ofHours hours
     * @return a new duration
     */
    public static TimeDuration ofHours(double hour) {
        return ofNanos((long) (hour * 60 * 60 * 1000000000));
    }
    
    /**
     * A new duration in minutes.
     * 
     * @param ofMinutes minutes
     * @return a new duration
     */
    public static TimeDuration ofMinutes(double min) {
        return ofNanos((long) (min * 60 * 1000000000));
    }
    
    /**
     * A new duration in seconds.
     * 
     * @param ofSeconds seconds
     * @return a new duration
     */
    public static TimeDuration ofSeconds(double sec) {
        return ofNanos((long) (sec * 1000000000));
    }
    
    /**
     * A new duration in hertz, will convert to the length of the period.
     * 
     * @param ofHertz frequency to be converted to a duration
     * @return a new duration
     */
    public static TimeDuration ofHertz(double hz) {
        if (hz <= 0.0) {
            throw new IllegalArgumentException("Frequency has to be greater than 0.0");
        }
        return ofNanos((long) (1000000000.0 / hz));
    }

    /**
     * A new duration in milliseconds.
     * @param ofMillis milliseconds of the duration
     * @return a new duration
     * @throws IllegalArgumentException if the duration is negative
     */
    public static TimeDuration ofMillis(int ms) {
        return ofNanos(((long) ms) * 1000000);
    }

    private static TimeDuration ofNanos(double durationInNanos) {
        long sec = (long) (durationInNanos / NANOSEC_IN_SEC);
        int nanoSec = (int) (durationInNanos - sec * NANOSEC_IN_SEC);
        return new TimeDuration(sec, nanoSec);
    }

    
    /**
     * A new duration in nanoseconds.
     * @param nanoSec nanoseconds of the duration
     * @return a new duration
     * @throws IllegalArgumentException if the duration is negative
     */
    public static TimeDuration ofNanos(long nanoSec) {
        return createWithCarry(nanoSec / NANOSEC_IN_SEC, (int) (nanoSec % NANOSEC_IN_SEC));
    }

    /**
     * Returns a new duration which is smaller by the given factor.
     * 
     * @param factor constant to divide
     * @return a new duration
     * @throws IllegalArgumentException if factor is negative
     */
    public TimeDuration divideBy(int factor) {
        return createWithCarry(sec / factor, sec % factor + nanoSec / factor);
    }

    /**
     * Returns a new duration which is bigger by the given factor.
     *
     * @param factor constant to multiply
     * @return a new duration
     * @throws IllegalArgumentException if factor is negative
     */
    public TimeDuration multiplyBy(int factor) {
        return createWithCarry(sec * factor, nanoSec * factor);
    }

    private static TimeDuration createWithCarry(long seconds, long nanos) {
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

        return new TimeDuration(seconds, (int) nanos);
    }

    /**
     * Returns a time interval that lasts this duration and is centered
     * around the given timestamp.
     * 
     * @param reference a timestamp
     * @return a new time interval
     */
    public TimeInterval around(TimeStamp reference) {
        TimeDuration half = this.divideBy(2);
        return TimeInterval.between(reference.minus(half), reference.plus(half));
    }

    /**
     * Returns a time interval that lasts this duration and starts from the
     * given timestamp.
     *
     * @param reference a timestamp
     * @return a new time interval
     */
    public TimeInterval after(TimeStamp reference) {
        return TimeInterval.between(reference, reference.plus(this));
    }

    /**
     * Returns a time interval that lasts this duration and ends at the
     * given timestamp.
     *
     * @param reference a timestamp
     * @return a new time interval
     */
    public TimeInterval before(TimeStamp reference) {
        return TimeInterval.between(reference.minus(this), reference);
    }

    @Override
    public String toString() {
        return "" + nanoSec;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(nanoSec).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof TimeDuration) {
            if (nanoSec == ((TimeDuration) obj).nanoSec)
                return true;
        }

        return false;
    }

}
