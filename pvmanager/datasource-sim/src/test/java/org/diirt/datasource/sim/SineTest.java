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
public class SineTest {

    @Test
    public void values() {
        // Creates the function
        Sine sine = new Sine(0.0, 10.0, 4.0, 1.0);
        VDouble firstValue = sine.nextValue();

        // Check limits
        assertEquals(5.0, firstValue.getValue(), 0.0001);
        assertThat(firstValue.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(firstValue.getAlarmName(), equalTo("NONE"));
        assertThat(firstValue.getLowerCtrlLimit(), equalTo(0.0));
        assertThat(firstValue.getLowerDisplayLimit(), equalTo(0.0));
        assertThat(firstValue.getLowerAlarmLimit(), equalTo(1.0));
        assertThat(firstValue.getLowerWarningLimit(), equalTo(2.0));
        assertThat(firstValue.getUpperWarningLimit(), equalTo(8.0));
        assertThat(firstValue.getUpperAlarmLimit(), equalTo(9.0));
        assertThat(firstValue.getUpperDisplayLimit(), equalTo(10.0));
        assertThat(firstValue.getUpperCtrlLimit(), equalTo(10.0));

        // Check values
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
