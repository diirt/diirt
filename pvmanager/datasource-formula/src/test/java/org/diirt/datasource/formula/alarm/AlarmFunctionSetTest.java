/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.alarm;

import org.diirt.datasource.formula.*;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.VEnum;

import org.diirt.vtype.VString;
import org.junit.Test;

import static org.diirt.vtype.ValueFactory.*;


/**
 *
 * @author carcassi
 */
public class AlarmFunctionSetTest {

    private AlarmFunctionSet set = new AlarmFunctionSet();

    @Test
    public void highestSeverity() {
        VString dataA = newVString("a", alarmNone(), timeNow());
        VString dataB = newVString("b", alarmNone(), timeNow());
        VString dataC = newVString("c", alarmNone(), timeNow());
        VString dataD = newVString("d", newAlarm(AlarmSeverity.MAJOR, "Help!"), timeNow());

        VEnum expected1 = newVEnum(0, AlarmSeverity.labels(), alarmNone(), timeNow());
        VEnum expected2 = newVEnum(2, AlarmSeverity.labels(), alarmNone(), timeNow());

        FunctionTester.findByName(set, "highestSeverity")
                .compareReturnValue(expected1, dataA, dataB, dataC)
                .compareReturnValue(expected2, dataB, dataC, dataD);
    }

    @Test
    public void alarmOf() {
        VString dataA = newVString("a", alarmNone(), timeNow());
        VString dataB = newVString("d", newAlarm(AlarmSeverity.MAJOR, "Help!"), timeNow());

        VEnum expected1 = newVEnum(0, AlarmSeverity.labels(), alarmNone(), timeNow());
        VEnum expected2 = newVEnum(2, AlarmSeverity.labels(), alarmNone(), timeNow());

        FunctionTester.findByName(set, "alarmOf")
                .compareReturnValue(expected1, dataA)
                .compareReturnValue(expected2, dataB);
    }
}
