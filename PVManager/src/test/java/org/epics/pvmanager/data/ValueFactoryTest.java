/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.data.ValueFactory.*;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class ValueFactoryTest {

    public ValueFactoryTest() {
    }

    @Test
    public void newAlarm1() {
        Alarm alarm = newAlarm(AlarmSeverity.MAJOR, "DEVICE");
        assertThat(alarm.getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(alarm.getAlarmName(), equalTo("DEVICE"));
    }

    @Test
    public void alarmNone1() {
        Alarm alarm = alarmNone();
        assertThat(alarm.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(alarm.getAlarmName(), equalTo("NONE"));
    }
    
    @Test
    public void newVString1() {
        VString value = newVString("Testing", alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getValue(), equalTo("Testing"));
        assertThat(value.toString(), equalTo("VString[Testing, 2012/12/05 09:57:21.521]"));
    }
    
    @Test
    public void newVEnum1() {
        VEnum value = newVEnum(1, Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getValue(), equalTo("TWO"));
        assertThat(value.getIndex(), equalTo(1));
        assertThat(value.getLabels(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
        assertThat(value.toString(), equalTo("VEnum[TWO(1), 2012/12/05 09:57:21.521]"));
    }
    
    @Test
    public void newVDouble1() {
        VDouble value = newVDouble(1.0, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getValue(), equalTo(1.0));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VDouble[1.0, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }
    
    @Test
    public void newVDoubleArray1() {
        VDoubleArray value = newVDoubleArray(new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0), newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getData(), equalTo((ListDouble) new ArrayDouble(3.14, 6.28, 1.41, 0.0, 1.0)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VDoubleArray[[3.14, 6.28, 1.41, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }
    
    @Test
    public void newVIntArray1() {
        VIntArray value = newVIntArray(new ArrayInt(3, 6, 1, 0, 1), newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(1354719441, 521786982)), displayNone());
        assertThat(value.getData(), equalTo((ListInt) new ArrayInt(3, 6, 1, 0, 1)));
        assertThat(value.getAlarmName(), equalTo("LOW"));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(value.toString(), equalTo("VIntArray[[3, 6, 1, ...], size 5, MINOR(LOW), 2012/12/05 09:57:21.521]"));
    }
    
    @Test
    public void newVEnumArray1() {
        VEnumArray value = newVEnumArray(new ArrayInt(1, 0, 2), Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getData(), equalTo(Arrays.asList("TWO", "ONE", "THREE")));
        assertThat(value.getIndexes(), equalTo((ListInt) new ArrayInt(1, 0, 2)));
        assertThat(value.getSizes(), equalTo((ListInt) new ArrayInt(3)));
        assertThat(value.getLabels(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
        assertThat(value.toString(), equalTo("VEnumArray[[TWO, ONE, THREE], size 3, 2012/12/05 09:57:21.521]"));
    }
    
    @Test
    public void newVStringArray1() {
        VStringArray value = newVStringArray(Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), newTime(Timestamp.of(1354719441, 521786982)));
        assertThat(value.getData(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
        assertThat(value.getSizes(), equalTo((ListInt) new ArrayInt(3)));
        assertThat(value.toString(), equalTo("VStringArray[[ONE, TWO, THREE], size 3, 2012/12/05 09:57:21.521]"));
    }

}