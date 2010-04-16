/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * A duration of time (such as 3 sec, 30ms, 1nsec) at the nanosecond precision.
 *
 * @author carcassi
 */
public class TimeDuration {
    private long nanoSec;

    private TimeDuration(long nanoSec) {
        this.nanoSec = nanoSec;
    }

    /**
     * Duration in nanoseconds.
     * @return duration in nanoseconds.
     */
    public long getNanoSec() {
        return nanoSec;
    }

    /**
     * A new duration in milliseconds.
     * @param ms milliseconds of the duration
     * @return a new duration
     */
    public static TimeDuration ms(int ms) {
        return new TimeDuration(ms * 1000000);
    }

    /**
     * A new duration in nanoseconds.
     * @param nanoSec nanoseconds of the duration
     * @return a new duration
     */
    public static TimeDuration nanos(int nanoSec) {
        return new TimeDuration(nanoSec);
    }

    /**
     * Returns a new duration which is smaller by the given factor.
     * 
     * @param factor constant to divide
     * @return a new duration
     */
    public TimeDuration divideBy(int factor) {
        return new TimeDuration(nanoSec / factor);
    }

    /**
     * Returns a new duration which is bigger by the given factor.
     *
     * @param factor constant to multiply
     * @return a new duration
     */
    public TimeDuration multiplyBy(int factor) {
        return new TimeDuration(nanoSec * factor);
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

}
