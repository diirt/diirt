/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.VDouble;
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
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(value.getAlarmName(), equalTo("LOLO"));
        assertThat(value.getLowerCtrlLimit(), equalTo(-10.0));
        assertThat(value.getLowerDisplayLimit(), equalTo(-10.0));
        assertThat(value.getLowerAlarmLimit(), equalTo(-8.0));
        assertThat(value.getLowerWarningLimit(), equalTo(-6.0));
        assertThat(value.getUpperWarningLimit(), equalTo(6.0));
        assertThat(value.getUpperAlarmLimit(), equalTo(8.0));
        assertThat(value.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(value.getUpperCtrlLimit(), equalTo(10.0));

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
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(value.getAlarmName(), equalTo("HIHI"));
        assertThat(value.getLowerCtrlLimit(), equalTo(-10.0));
        assertThat(value.getLowerDisplayLimit(), equalTo(-10.0));
        assertThat(value.getLowerAlarmLimit(), equalTo(-8.0));
        assertThat(value.getLowerWarningLimit(), equalTo(-6.0));
        assertThat(value.getUpperWarningLimit(), equalTo(6.0));
        assertThat(value.getUpperAlarmLimit(), equalTo(8.0));
        assertThat(value.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(value.getUpperCtrlLimit(), equalTo(10.0));

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
