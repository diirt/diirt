/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class VTypeTest {

    @Test
    public void highestSeverityOf1() {
        Alarm none = Alarm.none();
        Alarm minor = Alarm.create(AlarmSeverity.MINOR, "Minor alarm");
        Alarm otherMinor = Alarm.create(AlarmSeverity.MINOR, "Other minor alarm");
        Alarm major = Alarm.create(AlarmSeverity.MAJOR, "Major alarm");
        Alarm invalid = Alarm.create(AlarmSeverity.INVALID, "Invalid alarm");
        Alarm undefined = Alarm.create(AlarmSeverity.UNDEFINED, "Undefined alarm");
        assertThat(VType.highestAlarmOf(Arrays.asList(VType.toVType(0.0, none), VType.toVType(0.0, minor)), true), sameInstance(minor));
        assertThat(VType.highestAlarmOf(Arrays.asList(VType.toVType(0.0, none), VType.toVType(0.0, minor), VType.toVType(0.0, otherMinor)), true), sameInstance(minor));
        assertThat(VType.highestAlarmOf(Arrays.asList(null, VType.toVType(0.0, minor), VType.toVType(0.0, otherMinor)), true), sameInstance(minor));
        assertThat(VType.highestAlarmOf(Arrays.asList(null, VType.toVType(0.0, minor), VType.toVType(0.0, otherMinor)), false), sameInstance(Alarm.noValue()));
        assertThat(VType.highestAlarmOf(Arrays.asList(VType.toVType(0.0, none), VType.toVType(0.0, major), VType.toVType(0.0, minor), VType.toVType(0.0, otherMinor)), true), sameInstance(major));
        assertThat(VType.highestAlarmOf(Arrays.asList(VType.toVType(0.0, none), VType.toVType(0.0, major), VType.toVType(0.0, minor), VType.toVType(0.0, otherMinor), VType.toVType(0.0, invalid)), true), sameInstance(invalid));
        assertThat(VType.highestAlarmOf(Arrays.asList(VType.toVType(0.0, none), VType.toVType(0.0, major), VType.toVType(0.0, minor), VType.toVType(0.0, undefined), VType.toVType(0.0, invalid)), true), sameInstance(undefined));
        assertThat(VType.highestAlarmOf(Arrays.asList(VType.toVType(0.0, none), VType.toVType(0.0, major), VType.toVType(0.0, minor), VType.toVType(0.0, undefined), VType.toVType(0.0, invalid), null), false), sameInstance(undefined));
    }
}
