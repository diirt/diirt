/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import org.diirt.util.stats.Ranges;
import org.diirt.util.text.NumberFormats;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class DisplayTest {

    public DisplayTest() {
    }

    @Test
    public void create1() {
        Display display = Display.create(0.0, 1.0, 2.0, "m", NumberFormats.toStringFormat(), 8.0, 9.0, 10.0, 0.0, 10.0);
        // TODO: Range needs to support equals
//        assertThat(display.getDisplayRange(), equalTo(Ranges.range(0.0, 10.0)));
//        assertThat(display.getWarningRange(), equalTo(Ranges.range(1.0, 9.0)));
//        assertThat(display.getAlarmRange(), equalTo(Ranges.range(2.0, 8.0)));
//        assertThat(display.getControlRange(), equalTo(Ranges.range(0.0, 10.0)));
        assertThat(display.getUnit(), equalTo("m"));
        assertThat(display.getFormat(), equalTo(NumberFormats.toStringFormat()));
    }

    @Test
    public void none1() {
        Display display = Display.none();
        // TODO: add range check
        assertThat(display.getUnit(), equalTo(""));
        assertThat(display.getFormat(), equalTo(NumberFormats.toStringFormat()));
    }
    @Test
    public void equals1() {
        // TODO: implement when Range is done
//        assertThat(Alarm.create(AlarmSeverity.MINOR, "HIGH"), equalTo(Alarm.create(AlarmSeverity.MINOR, "HIGH")));
//        assertThat(Alarm.none(), equalTo(Alarm.none()));
//        assertThat(Alarm.none(), not(equalTo(null)));
//        assertThat(Alarm.create(AlarmSeverity.MINOR, "HIGH"), not(equalTo(Alarm.create(AlarmSeverity.MINOR, "LOW"))));
//        assertThat(Alarm.create(AlarmSeverity.MINOR, "HIGH"), not(equalTo(Alarm.create(AlarmSeverity.MAJOR, "HIGH"))));
    }

}
