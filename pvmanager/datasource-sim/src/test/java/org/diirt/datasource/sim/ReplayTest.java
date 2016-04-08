/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.diirt.datasource.sim.NameParser;
import org.diirt.datasource.sim.Replay;

import java.util.List;

import org.diirt.util.time.TimeInterval;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.VDouble;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static java.time.Duration.*;
import static org.diirt.datasource.test.TimeMatchers.*;

import java.time.Instant;

import org.junit.BeforeClass;

/**
 * Tests uniform noise distribution function
 *
 * @author carcassi
 */
public class ReplayTest {

    @Test
    public void replayValues() {
        // Creates the function
        Instant start = Instant.now();
        Replay replay = (Replay) NameParser.createFunction("replay(\"./src/test/resources/org/diirt/datasource/replay/parse1.xml\")");
        List<VDouble> values = replay.createValues(TimeInterval.after(ofMillis(1000), start));
        assertThat(values.size(), equalTo(4));

        // Check first value
        VDouble value = values.get(0);
        assertThat(value.getValue(), equalTo(0.0));
        assertThat(value.getTimestamp(), equalTo(Instant.ofEpochSecond(0, 0)));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmName(), equalTo("NONE"));
        assertThat(value.getTimeUserTag(), equalTo(0));
        assertThat(value.getLowerCtrlLimit(), equalTo(-10.0));
        assertThat(value.getLowerDisplayLimit(), equalTo(-10.0));
        assertThat(value.getLowerAlarmLimit(), equalTo(-9.0));
        assertThat(value.getLowerWarningLimit(), equalTo(-8.0));
        assertThat(value.getUpperWarningLimit(), equalTo(8.0));
        assertThat(value.getUpperAlarmLimit(), equalTo(9.0));
        assertThat(value.getUpperCtrlLimit(), equalTo(10.0));
        assertThat(value.getUpperDisplayLimit(), equalTo(10.0));

        // Check second value
        value = values.get(1);
        assertThat(value.getValue(), equalTo(1.0));
        assertThat(value.getTimestamp(), equalTo(Instant.ofEpochSecond(0, 0).plus(ofMillis(100))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.INVALID));
        assertThat(value.getAlarmName(), equalTo("RECORD"));
        assertThat(value.getTimeUserTag(), equalTo(0));

        // Check third value
        value = values.get(2);
        assertThat(value.getValue(), equalTo(2.0));
        assertThat(value.getTimestamp(), equalTo(Instant.ofEpochSecond(0, 0).plus(ofMillis(200))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmName(), equalTo("NONE"));
        assertThat(value.getTimeUserTag(), equalTo(0));

        // Check fourth value
        value = values.get(3);
        assertThat(value.getValue(), equalTo(3.0));
        assertThat(value.getTimestamp(), equalTo(Instant.ofEpochSecond(0, 0).plus(ofMillis(500))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmName(), equalTo("NONE"));
        assertThat(value.getTimeUserTag(), equalTo(0));
    }

    @BeforeClass
    public static void initializeParser() {
        Replay replay = (Replay) NameParser.createFunction("replay(\"./src/test/resources/org/diirt/datasource/replay/parse2.xml\")");
        if (replay.hashCode() == 0)
            System.out.println("");
    }

    @Test
    public void adjustTime() {
        // Creates the function
        Instant start = Instant.now();
        Replay replay = (Replay) NameParser.createFunction("replay(\"./src/test/resources/org/diirt/datasource/replay/parse2.xml\")");
        List<VDouble> values = replay.createValues(TimeInterval.after(ofMillis(6000), start));
        assertThat(values.size(), equalTo(7));

        // Check first value
        VDouble value = values.get(0);
        assertThat(value.getValue(), equalTo(0.0));
        assertThat(value.getTimestamp(), within(TimeInterval.around(ofMillis(1), start)));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmName(), equalTo("NONE"));
        assertThat(value.getTimeUserTag(), equalTo(0));
        assertThat(value.getLowerCtrlLimit(), equalTo(-10.0));
        assertThat(value.getLowerDisplayLimit(), equalTo(-10.0));
        assertThat(value.getLowerAlarmLimit(), equalTo(-9.0));
        assertThat(value.getLowerWarningLimit(), equalTo(-8.0));
        assertThat(value.getUpperWarningLimit(), equalTo(8.0));
        assertThat(value.getUpperAlarmLimit(), equalTo(9.0));
        assertThat(value.getUpperCtrlLimit(), equalTo(10.0));
        assertThat(value.getUpperDisplayLimit(), equalTo(10.0));

        // Check second value
        value = values.get(1);
        assertThat(value.getValue(), equalTo(1.0));
        assertThat(value.getTimestamp(), within(TimeInterval.around(ofMillis(1), start.plus(ofMillis(1000)))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.INVALID));
        assertThat(value.getAlarmName(), equalTo("RECORD"));
        assertThat(value.getTimeUserTag(), equalTo(0));

        // Check third value
        value = values.get(2);
        assertThat(value.getValue(), equalTo(2.0));
        assertThat(value.getTimestamp(), within(TimeInterval.around(ofMillis(1), start.plus(ofMillis(2000)))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmName(), equalTo("NONE"));
        assertThat(value.getTimeUserTag(), equalTo(0));
    }
}
