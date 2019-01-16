/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class RampTest {

    @Test
    public void values1() {
        // Creates the function
        Ramp ramp = new Ramp(-10.0, 10.0, 2.0, 1.0);
        VDouble value = ramp.nextValue();

        // Check limits
        assertThat(value.getAlarm().getSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(value.getAlarm().getName(), equalTo("LOLO"));
        assertThat(value.getDisplay().getControlRange().getMinimum(), equalTo(-10.0));
        assertThat(value.getDisplay().getDisplayRange().getMinimum(), equalTo(-10.0));
        assertThat(value.getDisplay().getAlarmRange().getMinimum(), equalTo(-8.0));
        assertThat(value.getDisplay().getWarningRange().getMinimum(), equalTo(-6.0));
        assertThat(value.getDisplay().getWarningRange().getMaximum(), equalTo(6.0));
        assertThat(value.getDisplay().getAlarmRange().getMaximum(), equalTo(8.0));
        assertThat(value.getDisplay().getDisplayRange().getMaximum(), equalTo(10.0));
        assertThat(value.getDisplay().getControlRange().getMaximum(), equalTo(10.0));

        assertThat(value.getValue(), equalTo(-10.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-8.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-6.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-4.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-2.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(0.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(2.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(4.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(6.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(8.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(10.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-10.0));
    }

    @Test
    public void values2() {
        // Creates the function
        Ramp ramp = new Ramp(-10.0, 10.0, -2.0, 1.0);
        VDouble value = ramp.nextValue();

        // Check limits
        assertThat(value.getAlarm().getSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(value.getAlarm().getName(), equalTo("HIHI"));
        assertThat(value.getDisplay().getControlRange().getMinimum(), equalTo(-10.0));
        assertThat(value.getDisplay().getDisplayRange().getMinimum(), equalTo(-10.0));
        assertThat(value.getDisplay().getAlarmRange().getMinimum(), equalTo(-8.0));
        assertThat(value.getDisplay().getWarningRange().getMinimum(), equalTo(-6.0));
        assertThat(value.getDisplay().getWarningRange().getMaximum(), equalTo(6.0));
        assertThat(value.getDisplay().getAlarmRange().getMaximum(), equalTo(8.0));
        assertThat(value.getDisplay().getDisplayRange().getMaximum(), equalTo(10.0));
        assertThat(value.getDisplay().getControlRange().getMaximum(), equalTo(10.0));

        assertThat(value.getValue(), equalTo(10.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(8.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(6.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(4.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(2.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(0.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-2.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-4.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-6.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-8.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(-10.0));
        value = ramp.nextValue();
        assertThat(value.getValue(), equalTo(10.0));
    }
}
