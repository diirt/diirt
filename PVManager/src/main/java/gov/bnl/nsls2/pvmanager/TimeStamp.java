/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.Date;

/**
 * Represent a time stamp at nanosecond accuracy.
 *
 * @author carcassi
 */
public class TimeStamp implements Comparable {
    
    /**
     * Constant to convert epics seconds to unix seconds
     */
    private static long TS_EPOCH_SEC_PAST_1970=7305*86400;

    // The time is kept in epics seconds to minimize convertions
    private final long epicsSec;
    private final long nanoSec;
    
    /**
     * Date object is created lazily.
     */
    private Date date;

    private TimeStamp(long epicsSec, long nanoSec) {
        this.epicsSec = epicsSec;
        this.nanoSec = nanoSec;
    }

    /**
     * Returns a new timestamp using EPICS time arguments.
     *
     * @param epicsSec number of second in EPICS time
     * @param nanoSec nanoseconds from the given second
     * @return a new timestamp
     */
    public static TimeStamp epicsTime(long epicsSec, long nanoSec) {
        return new TimeStamp(epicsSec, nanoSec);
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
        date = new Date((epicsSec+TS_EPOCH_SEC_PAST_1970)*1000+nanoSec/1000000);
    }

    @Override
    public int hashCode() {
        return new Long(nanoSec).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeStamp) {
            TimeStamp other = (TimeStamp) obj;
            return other.nanoSec == nanoSec && other.epicsSec == epicsSec;
        }

        return false;
    }

    @Override
    public int compareTo(Object o) {
        TimeStamp other = (TimeStamp) o;
	if (epicsSec < other.epicsSec) {
            return -1;
        } else if (epicsSec == other.epicsSec) {
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
        return new TimeStamp(epicsSec + (duration.getNanoSec() / 1000000000), nanoSec + (duration.getNanoSec() % 1000000000));
    }

    /**
     * Subtracts the given duration to this timestamp and returns the result.
     * @param duration a time duration
     * @return a new timestamp
     */
    public TimeStamp minus(TimeDuration duration) {
        return new TimeStamp(epicsSec - (duration.getNanoSec() / 1000000000), nanoSec - (duration.getNanoSec() % 1000000000));
    }

    @Override
    public String toString() {
        return epicsSec + "." + nanoSec;
    }

}
