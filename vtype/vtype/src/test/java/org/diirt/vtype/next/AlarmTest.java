/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class AlarmTest {

    public AlarmTest() {
    }

    @Test
    public void create1() {
        Alarm alarm = Alarm.create(AlarmSeverity.MAJOR, "DEVICE");
        assertThat(alarm.getSeverity(), equalTo(AlarmSeverity.MAJOR));
        assertThat(alarm.getName(), equalTo("DEVICE"));
        assertThat(alarm.toString(), equalTo("MAJOR(DEVICE)"));
    }

    @Test
    public void none1() {
        Alarm alarm = Alarm.none();
        assertThat(alarm.getSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(alarm.getName(), equalTo("None"));
        assertThat(alarm.toString(), equalTo("NONE(None)"));
    }
    @Test
    public void equals1() {
        assertThat(Alarm.create(AlarmSeverity.MINOR, "HIGH"), equalTo(Alarm.create(AlarmSeverity.MINOR, "HIGH")));
        assertThat(Alarm.none(), equalTo(Alarm.none()));
        assertThat(Alarm.none(), not(equalTo(null)));
        assertThat(Alarm.create(AlarmSeverity.MINOR, "HIGH"), not(equalTo(Alarm.create(AlarmSeverity.MINOR, "LOW"))));
        assertThat(Alarm.create(AlarmSeverity.MINOR, "HIGH"), not(equalTo(Alarm.create(AlarmSeverity.MAJOR, "HIGH"))));
    }

}
