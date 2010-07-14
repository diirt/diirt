/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager;

import java.util.Date;

/**
 * Represent a time stamp at nanosecond accuracy. The time is internally stored
 * as two values: the UNIX timestamp (number of seconds since
 * 1/1/1970) and the nanoseconds past that timestamp. The UNIX timestamp is
 * stored as a signed long, which has the range of 292 billion years before
 * and another 292 past the epoch.
 * <p>
 * Note that while TimeStamp are usually created according to system clocks which
 * takes into account leap seconds, all the math operations on TimeStamps do
 * not take leap seconds into account.
 *
 * @author carcassi
 */
public class TimeStamp implements Comparable {

    private static TimeStamp base = TimeStamp.timestampOf(new Date());
    private static long baseNano = System.nanoTime();
    
    /**
     * Constant to convert epics seconds to unix seconds. It counts the number
     * of seconds for 20 years, 5 of which leap years. It does _not_ count the
     * number of leap seconds (which should have been 15).
     */
    private static long TS_EPOCH_SEC_PAST_1970=631152000; //7305*86400;

    private final long unixSec;
    private final long nanoSec;
    
    /**
     * Date object is created lazily.
     */
    private Date date;

    private TimeStamp(long epicsSec, long nanoSec) {
        if (nanoSec < 0)
            throw new IllegalArgumentException("Nanoseconds cannot be negative");
        if (nanoSec > 999999999)
            throw new IllegalArgumentException("Nanoseconds cannot be more than 999999999");
        this.unixSec = epicsSec;
        this.nanoSec = nanoSec;
    }

    /**
     * TODO: get epicsSec out and put it in a utility class
     * @return
     */
    public long getEpicsSec() {
        return unixSec;
    }

    public long getSec() {
        // TODO
        throw new UnsupportedOperationException("To implement");
    }

    public long getNanoSec() {
        return nanoSec;
    }

    /**
     * Returns a new timestamp using EPICS time arguments.
     *
     * @param epicsSec number of second in EPICS time
     * @param nanoSec nanoseconds from the given second
     * @return a new timestamp
     */
    public static TimeStamp epicsTime(long epicsSec, long nanoSec) {
        return new TimeStamp(epicsSec + TS_EPOCH_SEC_PAST_1970, nanoSec);
    }

    public static TimeStamp time(long unixSec, long nanoSec) {
        return new TimeStamp(unixSec, nanoSec);
    }

    public static TimeStamp timestampOf(Date date) {
        long time = date.getTime();
        long nanoSec = (time % 1000) * 1000000;
        long epicsSec = (time / 1000);
        return time(epicsSec, nanoSec);
    }

    public static TimeStamp now() {
        return base.plus(TimeDuration.nanos(System.nanoTime() - baseNano));
    }

    /**
     * Converts the time stamp to a standard Date. The convertion is done once.
     *
     * @return
     */
    public Date asDate() {
        if (date == null)
            prepareDate();
        return date;
    }

    /**
     * Prepares the date object
     */
    private void prepareDate() {
        date = new Date((unixSec+TS_EPOCH_SEC_PAST_1970)*1000+nanoSec/1000000);
    }

    @Override
    public int hashCode() {
        return new Long(nanoSec).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeStamp) {
            TimeStamp other = (TimeStamp) obj;
            return other.nanoSec == nanoSec && other.unixSec == unixSec;
        }

        return false;
    }

    @Override
    public int compareTo(Object o) {
        TimeStamp other = (TimeStamp) o;
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
        long secToAdd = duration.getNanoSec() / 1000000000;
        long nanoToAdd = duration.getNanoSec() % 1000000000;
        long newNano = nanoSec + nanoToAdd;
        if (newNano > 999999999) {
            newNano -= 1000000000;
            secToAdd += 1;
        }
        return new TimeStamp(unixSec + secToAdd, newNano);
    }

    /**
     * Subtracts the given duration to this timestamp and returns the result.
     * @param duration a time duration
     * @return a new timestamp
     */
    public TimeStamp minus(TimeDuration duration) {
        return new TimeStamp(unixSec - (duration.getNanoSec() / 1000000000), nanoSec - (duration.getNanoSec() % 1000000000));
    }

    @Override
    public String toString() {
        return unixSec + "." + nanoSec;
    }

}
