/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class RampTest {

    @Test
    public void rampValues() {
        // Creates the function
        Ramp ramp1 = new Ramp(0.0, 10.0, 1.0, 1.0);
        VDouble firstValue = ramp1.nextValue();

        // Check limits
        assertThat(firstValue.getValue(), equalTo(0.0));
        assertThat(firstValue.getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(firstValue.getAlarmStatus(), equalTo(AlarmStatus.NONE));
        assertThat(firstValue.getLowerCtrlLimit(), equalTo(0.0));
        assertThat(firstValue.getLowerDisplayLimit(), equalTo(0.0));
        assertThat(firstValue.getLowerAlarmLimit(), equalTo(1.0));
        assertThat(firstValue.getLowerWarningLimit(), equalTo(2.0));
        assertThat(firstValue.getUpperWarningLimit(), equalTo(8.0));
        assertThat(firstValue.getUpperAlarmLimit(), equalTo(9.0));
        assertThat(firstValue.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(firstValue.getUpperCtrlLimit(), equalTo(10.0));
        
        // Check values
        assertThat(ramp1.nextValue().getValue(), equalTo(1.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(2.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(3.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(4.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(5.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(6.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(7.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(8.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(9.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(10.0));
        assertThat(ramp1.nextValue().getValue(), equalTo(0.0));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MINOR));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(ramp1.nextValue().getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
    }
}
