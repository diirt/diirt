/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import org.diirt.util.time.TimeDuration;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
public class Log {

    private final List<Event> events = Collections.synchronizedList(new ArrayList<Event>());
    private final List<String> errors = Collections.synchronizedList(new ArrayList<String>());
    private final AtomicInteger testCount = new AtomicInteger(0);

    public <T> PVReaderListener<T> createReadListener() {
        return new PVReaderListener<T>() {

            @Override
            public void pvChanged(PVReaderEvent<T> event) {
                events.add(new ReadEvent(Instant.now(), event.getPvReader().getName(), event, event.getPvReader().isConnected(), event.getPvReader().getValue(), event.getPvReader().lastException()));
            }
        };
    }

    public <T> PVWriterListener<T> createWriteListener(final String name) {
        return new PVWriterListener<T>() {

            @Override
            public void pvChanged(PVWriterEvent<T> event) {
                events.add(new WriteEvent(Instant.now(), name, event, event.getPvWriter().isWriteConnected(), event.getPvWriter().lastWriteException()));
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

    public int getTestCount() {
        return testCount.get();
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
            errors.add(pvName + ": more connection notifications ("  + current + ") than expected ("  + connectionFlags.length + ")");
        }
        if (current < connectionFlags.length) {
            errors.add(pvName + ": fewer connection notifications ("  + current + ") than expected (" + connectionFlags.length + ")");
        }
        testCount.incrementAndGet();
    }

    public void matchWriteConnections(String pvName, boolean... connectionFlags) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof WriteEvent && event.getPvName().equals(pvName)) {
                WriteEvent writeEvent = (WriteEvent) event;
                if (writeEvent.getEvent().isConnectionChanged()) {
                    if (current < connectionFlags.length && writeEvent.isConnected() != connectionFlags[current]) {
                        errors.add(pvName + ": write connection notification " + current + " was " + writeEvent.isConnected() + " (expected " + connectionFlags[current] + ")");
                    }
                    current++;
                }
            }
        }
        if (current > connectionFlags.length) {
            errors.add(pvName + ": more write connection notifications ("  + current + ") than expected ("  + connectionFlags.length + ")");
        }
        if (current < connectionFlags.length) {
            errors.add(pvName + ": fewer write connection notifications ("  + current + ") than expected (" + connectionFlags.length + ")");
        }
        testCount.incrementAndGet();
    }

    public void matchWriteNotifications(String pvName, boolean... sucessfulWrite) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof WriteEvent && event.getPvName().equals(pvName)) {
                WriteEvent writeEvent = (WriteEvent) event;
                if (writeEvent.getEvent().isWriteSucceeded()) {
                    if (current < sucessfulWrite.length && !sucessfulWrite[current]) {
                        errors.add(pvName + ": write notification " + current + " was successful (expected unsuccessful)");
                    }
                    current++;
                } else if (writeEvent.getEvent().isWriteFailed()) {
                    if (current < sucessfulWrite.length && sucessfulWrite[current]) {
                        errors.add(pvName + ": write notification " + current + " was unsuccessful (expected successful)");
                    }
                    current++;
                }
            }
        }
        if (current > sucessfulWrite.length) {
            errors.add(pvName + ": more write notifications ("  + current + ") than expected ("  + sucessfulWrite.length + ")");
        }
        if (current < sucessfulWrite.length) {
            errors.add(pvName + ": fewer write notifications ("  + current + ") than expected (" + sucessfulWrite.length + ")");
        }
        testCount.incrementAndGet();
    }

    public void matchValues(String pvName, VTypeMatchMask mask, Object... values) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof ReadEvent && event.getPvName().equals(pvName)) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isValueChanged()) {
                    Object actualValue = readEvent.getValue();
                    if (current < values.length) {
                        Object expectedValue = values[current];
                        String message = mask.match(expectedValue, actualValue);
                        if (message != null) {
                            errors. add(pvName + ": value notification " + current + " " + message);
                        }
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
        testCount.incrementAndGet();
    }

    public void validate(String pvName, Validator validator) {
        List<String> resErrors = validator.validate(valuesForChannel(pvName, Object.class));
        for (String error : resErrors) {
            errors.add(pvName + ": " + error);
        }
        testCount.incrementAndGet();
    }

    public void matchAllValues(String pvName, VTypeMatchMask mask, Object expectedValue) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof ReadEvent && event.getPvName().equals(pvName)) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isValueChanged()) {
                    Object actualValue = readEvent.getValue();
                    String message = mask.match(expectedValue, actualValue);
                    if (message != null) {
                        errors. add(pvName + ": value notification " + current + " " + message);
                    }
                    current++;
                }
            }
        }
        testCount.incrementAndGet();
    }

    public void matchErrors(String pvName, String... messages) {
        int current = 0;
        for (Event event : events) {
            if (event instanceof ReadEvent && event.getPvName().equals(pvName)) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isExceptionChanged()) {
                    if (current < messages.length && Objects.equals(readEvent.getLastException().getMessage(), messages[current])) {
                        errors.add(pvName + ": error notification " + current + " was " + readEvent.getLastException().getMessage() + " (expected " + messages[current] + ")");
                    }
                    current++;
                }
            }
        }
        if (current > messages.length) {
            errors.add(pvName + ": more error notifications ("  + current + ") than expected ("  + messages.length + ")");
        }
        if (current < messages.length) {
            errors.add(pvName + ": fewer error notifications ("  + current + ") than expected (" + messages.length + ")");
        }
        testCount.incrementAndGet();
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
        testCount.incrementAndGet();
    }

    void matchValueEventRate(String pvName, double minRateHz, double maxRateHz) {
        Instant initialTime = null;
        Instant finalTime = null;
        int nNotifications = 0;
        for (Event event : events) {
            if (pvName.equals(event.getPvName()) && event instanceof ReadEvent) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isValueChanged()) {
                    Instant nextTime = readEvent.getTimestamp();
                    if (initialTime == null) {
                        initialTime = nextTime;
                    } else {
                        finalTime = nextTime;
                    }
                    nNotifications++;
                }
            }
        }

        if (initialTime != null && finalTime != null) {
            double seconds = TimeDuration.toSecondsDouble(Duration.between(initialTime, finalTime));
            // The period between the first two notification is going to be shorter
            // since we connect independently from the cycle.
            // We'll make sure the rate is between the two extreems: second event
            // is right after, second event is after the correct period
            double minMeasuredRate = (nNotifications - 2) / seconds;
            double maxMeasuredRate = (nNotifications - 1) / seconds;
            if (maxMeasuredRate < minRateHz || minMeasuredRate > maxRateHz) {
                errors.add(pvName + ": event rate mismatch (" + minMeasuredRate + "/" + maxMeasuredRate + " but expected between " + minRateHz + "/" + maxRateHz + ")");
            }
        }
        testCount.incrementAndGet();
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

    private List<Alarm> alarmsForChannel(String pvName) {
        List<Alarm> values = new ArrayList<>();
        for (Event event : events) {
            if (pvName.equals(event.getPvName()) && event instanceof ReadEvent) {
                ReadEvent readEvent = (ReadEvent) event;
                if (readEvent.getEvent().isValueChanged()) {
                    values.add(ValueUtil.alarmOf(readEvent.getValue()));
                }
            }
        }
        return values;
    }

    private DateTimeFormatter format = DateTimeFormatter.ofPattern("ss.NNNNNNNNN");

    public void print(PrintStream out) {
        for (Event event : events) {
            if (event instanceof ReadEvent) {
                ReadEvent readEvent = (ReadEvent) event;
                out.append(format.format(ZonedDateTime.ofInstant(readEvent.getTimestamp(), ZoneId.systemDefault())))
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
            if (event instanceof WriteEvent) {
                WriteEvent writeEvent = (WriteEvent) event;
                out.append(format.format(ZonedDateTime.ofInstant(writeEvent.getTimestamp(), ZoneId.systemDefault())))
                        .append(" W(");
                if (writeEvent.getEvent().isConnectionChanged()) {
                    out.append("C");
                }
                if (writeEvent.getEvent().isWriteSucceeded()) {
                    out.append("S");
                }
                if (writeEvent.getEvent().isWriteFailed()) {
                    out.append("F");
                }
                if (writeEvent.getEvent().isExceptionChanged()) {
                    out.append("E");
                }
                out.append(") ").append(writeEvent.getPvName());
                if (writeEvent.isConnected()) {
                    out.append(" CONN");
                } else {
                    out.append(" DISC");
                }
                if (writeEvent.getLastException() != null) {
                    out.append(" ").append(writeEvent.getLastException().getClass().getName())
                            .append(":").append(writeEvent.getLastException().getMessage());
                } else {
                    out.append(" NoException");
                }
            }
            out.append("\n");
        }
        out.flush();
    }
}
