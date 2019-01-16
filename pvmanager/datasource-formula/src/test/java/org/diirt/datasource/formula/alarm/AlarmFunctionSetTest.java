/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.alarm;

import org.diirt.datasource.formula.*;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.Time;
import org.epics.vtype.VEnum;

import org.epics.vtype.VString;
import org.junit.Test;


/**
 *
 * @author carcassi
 */
public class AlarmFunctionSetTest {

    private AlarmFunctionSet set = new AlarmFunctionSet();

    @Test
    public void highestSeverity() {
        VString dataA = VString.of("a", Alarm.none(), Time.now());
        VString dataB = VString.of("b", Alarm.none(), Time.now());
        VString dataC = VString.of("c", Alarm.none(), Time.now());
        VString dataD = VString.of("d", Alarm.of(AlarmSeverity.MAJOR, null, "Help!"), Time.now());

        VEnum expected1 = VEnum.of(0, EnumDisplay.of(AlarmSeverity.labels()), Alarm.none(), Time.now());
        VEnum expected2 = VEnum.of(2, EnumDisplay.of(AlarmSeverity.labels()), Alarm.none(), Time.now());

        FunctionTester.findByName(set, "highestSeverity")
                .compareReturnValue(expected1, dataA, dataB, dataC)
                .compareReturnValue(expected2, dataB, dataC, dataD);
    }

    @Test
    public void alarmOf() {
        VString dataA = VString.of("a", Alarm.none(), Time.now());
        VString dataB = VString.of("d", Alarm.of(AlarmSeverity.MAJOR, null, "Help!"), Time.now());

        VEnum expected1 = VEnum.of(0, EnumDisplay.of(AlarmSeverity.labels()), Alarm.none(), Time.now());
        VEnum expected2 = VEnum.of(2, EnumDisplay.of(AlarmSeverity.labels()), Alarm.none(), Time.now());

        FunctionTester.findByName(set, "alarmOf")
                .compareReturnValue(expected1, dataA)
                .compareReturnValue(expected2, dataB);
    }
}
