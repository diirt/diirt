/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.integration;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.util.time.Timestamp;
import org.epics.util.time.TimestampFormat;
import org.epics.vtype.VNumber;
import org.epics.vtype.VTypeValueEquals;
import org.epics.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
public class Log {
    
    private final List<Event> events = Collections.synchronizedList(new ArrayList<Event>());
    private final List<String> errors = Collections.synchronizedList(new ArrayList<String>());
    
    public <T> PVReaderListener<T> createListener() {
        return new PVReaderListener<T>() {

            @Override
            public void pvChanged(PVReaderEvent<T> event) {
                events.add(new ReadEvent(Timestamp.now(), event.getPvReader().getName(), event, event.getPvReader().isConnected(), event.getPvReader().getValue(), event.getPvReader().lastException()));
            }
        };
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public boolean isCorrect() {
        return errors.isEmpty();
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void matchConnections(String pvName, boolean... connectionFlags) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof ReadEvent && event.getPvName().equals(pvName)) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isConnectionChanged()) {
                    if (current < connectionFlags.length && readEvent.isConnected() != connectionFlags[current]) {
                        errors.add(pvName + ": connection notification " + current + " was " + readEvent.isConnected() + " (expected " + connectionFlags[current] + ")");
                    }
                    current++;
                }
            }
        }
        if (current > connectionFlags.length) {
            errors.add(pvName + ": more connection notification ("  + current + ") than expected ("  + connectionFlags.length + ")");
        }
        if (current < connectionFlags.length) {
            errors.add(pvName + ": fewer connection notification ("  + current + ") than expected (" + connectionFlags.length + ")");
        }
    }
    
    public void matchValues(String pvName, Object... values) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof ReadEvent && event.getPvName().equals(pvName)) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isValueChanged()) {
                    Object actualValue = readEvent.getValue();
                    Object expectedValue = values[current];
                    if (!VTypeValueEquals.typeEquals(actualValue, expectedValue)) {
                        errors.add(pvName + ": value notification " + current + " TYPE mismatch: was " + actualValue + " (expected " + expectedValue + ")");
                    } else if (!VTypeValueEquals.valueEquals(actualValue, expectedValue)) {
                        errors.add(pvName + ": value notification " + current + " VALUE mismatch: was " + actualValue + " (expected " + expectedValue + ")");
                    } else if (!VTypeValueEquals.alarmEquals(ValueUtil.alarmOf(actualValue), ValueUtil.alarmOf(expectedValue))) {
                        errors.add(pvName + ": value notification " + current + " ALARM mismatch: was " + actualValue + " (expected " + expectedValue + ")");
                    } else if (!VTypeValueEquals.timeEquals(ValueUtil.timeOf(actualValue), ValueUtil.timeOf(expectedValue))) {
                        errors.add(pvName + ": value notification " + current + " TIME mismatch: was " + actualValue + " (expected " + expectedValue + ")");
                    }
                    current++;
                }
            }
        }
        if (current > values.length) {
            errors.add(pvName + ": more value notification ("  + current + ") than expected ("  + values.length + ")");
        }
        if (current < values.length) {
            errors.add(pvName + ": fewer value notification ("  + current + ") notification than expected (" + values.length + ")");
        }
    }
    
    public void matchSequentialNumberValues(String pvName, int expectedRepeatedValues) {
        List<VNumber> values = valuesForChannel(pvName, VNumber.class);
        Double currentValue = null;
        int repeatedValues = 0;
        for (VNumber vNumber : values) {
            if (vNumber == null) {
                errors.add(pvName + ": value was null");
            } else {
                if (currentValue == null) {
                    currentValue = vNumber.getValue().doubleValue();
                } else {
                    double nextValue = vNumber.getValue().doubleValue();
                    if (nextValue == currentValue) {
                        repeatedValues++;
                    } else if (nextValue != currentValue + 1) {
                        errors.add(pvName + ": value was not sequential (" + nextValue + " after " + currentValue + ")");
                    }
                    currentValue = nextValue;
                }
            }
        }
        if (repeatedValues != expectedRepeatedValues) {
            errors.add(pvName + ": repeated value occurences mismatch (" + repeatedValues + " but expected " + expectedRepeatedValues + ")");
        }
    }
    
    private <T> List<T> valuesForChannel(String pvName, Class<T> clazz) {
        List<T> values = new ArrayList<>();
        for (Event event : events) {
            if (pvName.equals(event.getPvName()) && event instanceof ReadEvent) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isValueChanged()) {
                    try {
                        T value = clazz.cast(readEvent.getValue());
                        values.add(value);
                    } catch(ClassCastException ex) {
                        errors.add(pvName + ": value is not " + clazz.getSimpleName() + " (was " + readEvent.getValue() + ")");
                    }
                }
            }
        }
        return values;
    } 
    
    TimestampFormat format = new TimestampFormat("ss.NNNNNNNNN");
    
    public void print(PrintStream out) {
        for (Event event : events) {
            if (event instanceof ReadEvent) {
                ReadEvent readEvent = (ReadEvent) event;
                out.append(format.format(readEvent.getTimestamp()))
                        .append(" R(");
                if (readEvent.getEvent().isConnectionChanged()) {
                    out.append("C");
                }
                if (readEvent.getEvent().isValueChanged()) {
                    out.append("V");
                }
                if (readEvent.getEvent().isExceptionChanged()) {
                    out.append("E");
                }
                out.append(") ").append(readEvent.getPvName());
                if (readEvent.isConnected()) {
                    out.append(" CONN ");
                } else {
                    out.append(" DISC ");
                }
                out.append(Objects.toString(readEvent.getValue()));
                if (readEvent.getLastException() != null) {
                    out.append(" ").append(readEvent.getLastException().getClass().getName())
                            .append(":").append(readEvent.getLastException().getMessage());
                } else {
                    out.append(" NoException");
                }
            }
            out.append("\n");
        }
        out.flush();
    }
}
