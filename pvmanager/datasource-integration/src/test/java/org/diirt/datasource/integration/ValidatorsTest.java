/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class ValidatorsTest {

    public ValidatorsTest() {
    }

    @Test
    public void matchCycle() {
        List<Object> matchValues = Arrays.<Object>asList(VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(3.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(4.0, Alarm.none(), Time.now(), Display.none()));
        List<Object> values = Arrays.<Object>asList(VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(3.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(4.0, Alarm.none(), Time.now(), Display.none()));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 0, matchValues, values), equalTo(true));
        values = Arrays.<Object>asList(VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(3.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(4.0, Alarm.none(), Time.now(), Display.none()),
                VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(3.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(4.0, Alarm.none(), Time.now(), Display.none()),
                VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 0, matchValues, values), equalTo(true));
        values = Arrays.<Object>asList(VDouble.of(3.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(4.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()), VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 0, matchValues, values), equalTo(false));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 1, matchValues, values), equalTo(false));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 2, matchValues, values), equalTo(true));
    }

    
}
