/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class SineTest {

    @Test
    public void rampValues() {
        Sine sine = new Sine(0.0, 10.0, 4.0, 1.0);
        VDouble firstValue = sine.nextValue();
        assertEquals(5.0, firstValue.getValue(), 0.0001);
        assertThat(firstValue.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertTrue(firstValue.getAlarmStatus().isEmpty());
        assertThat(firstValue.getLowerCtrlLimit(), equalTo(0.0));
        assertThat(firstValue.getLowerDisplayLimit(), equalTo(0.0));
        assertThat(firstValue.getLowerAlarmLimit(), equalTo(1.0));
        assertThat(firstValue.getLowerWarningLimit(), equalTo(2.0));
        assertThat(firstValue.getUpperWarningLimit(), equalTo(8.0));
        assertThat(firstValue.getUpperAlarmLimit(), equalTo(9.0));
        assertThat(firstValue.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(firstValue.getUpperCtrlLimit(), equalTo(10.0));
        assertEquals(10.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(0.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(10.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(0.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertThat(sine.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(sine.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(sine.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(sine.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
    }
}
