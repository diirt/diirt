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
public class SineTest {

    @Test
    public void values() {
        // Creates the function
        Sine sine = new Sine(0.0, 10.0, 4.0, 1.0);
        VDouble firstValue = sine.nextValue();

        // Check limits
        assertEquals(5.0, firstValue.getValue(), 0.0001);
        assertThat(firstValue.getAlarm(), equalTo(AlarmSeverity.NONE));
        assertThat(firstValue.getAlarm().getName(), equalTo("NONE"));
        assertThat(firstValue.getDisplay().getControlRange().getMinimum(), equalTo(0.0));
        assertThat(firstValue.getDisplay().getDisplayRange().getMinimum(), equalTo(0.0));
        assertThat(firstValue.getDisplay().getAlarmRange().getMinimum(), equalTo(1.0));
        assertThat(firstValue.getDisplay().getWarningRange().getMinimum(), equalTo(2.0));
        assertThat(firstValue.getDisplay().getWarningRange().getMaximum(), equalTo(8.0));
        assertThat(firstValue.getDisplay().getAlarmRange().getMaximum(), equalTo(9.0));
        assertThat(firstValue.getDisplay().getDisplayRange().getMaximum(), equalTo(10.0));
        assertThat(firstValue.getDisplay().getControlRange().getMaximum(), equalTo(10.0));

        // Check values
        assertEquals(10.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(0.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(10.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(0.0, sine.nextValue().getValue(), 0.0001);
        assertEquals(5.0, sine.nextValue().getValue(), 0.0001);
        assertThat(sine.nextValue().getAlarm().getSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(sine.nextValue().getAlarm().getSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(sine.nextValue().getAlarm().getSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(sine.nextValue().getAlarm().getSeverity(), equalTo(AlarmSeverity.NONE));
    }
}
