/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class TimeInterval {

    private final TimeStamp start;
    private final TimeStamp end;

    private TimeInterval(TimeStamp start, TimeStamp end) {
        this.start = start;
        this.end = end;
    }

    public boolean contains(TimeStamp instant) {
        return start.compareTo(instant) <= 0 && end.compareTo(instant) >= 0;
    }

    public static TimeInterval between(TimeStamp start, TimeStamp end) {
        return new TimeInterval(start, end);
    }

    @Override
    public String toString() {
        return start.toString() + " - " + end.toString();
    }

}
