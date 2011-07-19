/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import java.util.List;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.util.TimeDuration.*;
import static org.epics.pvmanager.TimeMatchers.*;

/**
 * Tests uniform noise distribution function
 *
 * @author carcassi
 */
public class ReplayTest {

    @Test
    public void replayValues() {
        // Creates the function
        TimeStamp start = TimeStamp.now();
        Replay replay = (Replay) NameParser.createFunction("replay(\"./src/test/resources/org/epics/pvmanager/replay/parse1.xml\")");
        List<VDouble> values = replay.createValues(ms(1000).after(start));
        assertThat(values.size(), equalTo(4));

        // Check first value
        VDouble value = values.get(0);
        assertThat(value.getValue(), equalTo(0.0));
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0)));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
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
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0).plus(ms(100))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.INVALID));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.RECORD));
        assertThat(value.getTimeUserTag(), equalTo(0));

        // Check third value
        value = values.get(2);
        assertThat(value.getValue(), equalTo(2.0));
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0).plus(ms(200))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
        assertThat(value.getTimeUserTag(), equalTo(0));

        // Check fourth value
        value = values.get(3);
        assertThat(value.getValue(), equalTo(3.0));
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0).plus(ms(500))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
        assertThat(value.getTimeUserTag(), equalTo(0));
    }

    @Test
    public void adjustTime() {
        // Creates the function
        TimeStamp start = TimeStamp.now();
        Replay replay = (Replay) NameParser.createFunction("replay(\"./src/test/resources/org/epics/pvmanager/replay/parse2.xml\")");
        List<VDouble> values = replay.createValues(ms(6000).after(start));
        assertThat(values.size(), equalTo(7));

        // Check first value
        VDouble value = values.get(0);
        assertThat(value.getValue(), equalTo(0.0));
        assertThat(value.getTimeStamp(), within(ms(1).around(start)));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
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
        assertThat(value.getTimeStamp(), within(ms(1).around(start.plus(ms(1000)))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.INVALID));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.RECORD));
        assertThat(value.getTimeUserTag(), equalTo(0));

        // Check third value
        value = values.get(2);
        assertThat(value.getValue(), equalTo(2.0));
        assertThat(value.getTimeStamp(), within(ms(1).around(start.plus(ms(2000)))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
        assertThat(value.getTimeUserTag(), equalTo(0));
    }
}
