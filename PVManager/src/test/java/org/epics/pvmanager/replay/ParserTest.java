/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.replay;

import java.net.URI;
import java.net.URL;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class ParserTest {

    public ParserTest() {
    }

    @Test
    public void unmarshalParse1() throws Exception {
        // Unmarshal XML file
        XmlValues values = Parser.parse(new URI("./src/test/resources/org/epics/pvmanager/replay/parse1.xml"));
        assertThat(values.getValue().size(), equalTo(4));
        assertThat(values.getValue().get(0), is(XmlVDouble.class));

        // Check first value
        XmlVDouble value = (XmlVDouble) values.getValue().get(0);
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
        value = (XmlVDouble) values.getValue().get(1);
        assertThat(value.getValue(), equalTo(1.0));
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0).plus(ms(100))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.INVALID));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.RECORD));
        assertThat(value.getTimeUserTag(), nullValue());

        // Check third value
        value = (XmlVDouble) values.getValue().get(2);
        assertThat(value.getValue(), equalTo(2.0));
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0).plus(ms(200))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
        assertThat(value.getTimeUserTag(), nullValue());

        // Check fourth value
        value = (XmlVDouble) values.getValue().get(3);
        assertThat(value.getValue(), equalTo(3.0));
        assertThat(value.getTimeStamp(), equalTo(TimeStamp.time(0, 0).plus(ms(500))));
        assertThat(value.getAlarmSeverity(), equalTo(AlarmSeverity.NONE));
        assertThat(value.getAlarmStatus(), equalTo(AlarmStatus.NONE));
        assertThat(value.getTimeUserTag(), nullValue());
    }

}