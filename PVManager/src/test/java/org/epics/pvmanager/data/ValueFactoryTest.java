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
    public void newVEnum1() {
        VEnum value = newVEnum(1, Arrays.asList("ONE", "TWO", "THREE"), alarmNone(), timeNow());
        assertThat(value.getValue(), equalTo("TWO"));
        assertThat(value.getIndex(), equalTo(1));
        assertThat(value.getLabels(), equalTo(Arrays.asList("ONE", "TWO", "THREE")));
    }

}