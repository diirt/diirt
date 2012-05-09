/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.time;

/**
 *
 * @author carcassi
 */
public class TimeRelativeInterval {
    
    private final Object start;
    private final Object end;
    
    private TimeRelativeInterval(Object start, Object end) {
        this.start = start;
        this.end = end;
    }

    public static TimeRelativeInterval of(TimeStamp start, TimeStamp end) {
        return new TimeRelativeInterval(start, end);
    }
    
    public boolean isIntervalAbsolute() {
        return isStartAbsolute() && isEndAbsolute();
    }
    
    public boolean isStartAbsolute() {
        return start instanceof TimeStamp || start == null;
    }
    
    public boolean isEndAbsolute() {
        return end instanceof TimeStamp || end == null;
    }

    public Object getStart() {
        return start;
    }

    public Object getEnd() {
        return end;
    }

    public TimeStamp getAbsoluteStart() {
        return (TimeStamp) start;
    }
    
    public TimeStamp getAbsoluteEnd() {
        return (TimeStamp) end;
    }
    
    public TimeDuration getRelativeStart() {
        return (TimeDuration) start;
    }
    
    public TimeDuration getRelativeEnd() {
        return (TimeDuration) end;
    }
    
    public TimeInterval toAbsoluteInterval(TimeStamp reference) {
        TimeStamp absoluteStart;
        if (isStartAbsolute()) {
            absoluteStart = getAbsoluteStart();
        } else {
            absoluteStart = reference.plus(getRelativeStart());
        }
        TimeStamp absoluteEnd;
        if (isEndAbsolute()) {
            absoluteEnd = getAbsoluteEnd();
        } else {
            absoluteEnd = reference.plus(getRelativeEnd());
        }
        return TimeInterval.between(absoluteStart, absoluteEnd);
    }
    
    
}
