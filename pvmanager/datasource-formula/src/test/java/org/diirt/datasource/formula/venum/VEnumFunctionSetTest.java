/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.venum;

import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.formula.*;
import org.diirt.util.array.ArrayDouble;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.VEnum;

import org.junit.Test;

import static org.diirt.vtype.ValueFactory.*;


/**
 *
 * @author carcassi
 */
public class VEnumFunctionSetTest {

    private VEnumFunctionSet set = new VEnumFunctionSet();

    @Test
    public void enumOf1() {
        List<String> labels = Arrays.asList("LOW", "MIDDLE", "HIGH");

        VEnum low = newVEnum(0, labels, alarmNone(), timeNow());
        VEnum middle = newVEnum(1, labels, alarmNone(), timeNow());
        VEnum high = newVEnum(2, labels, alarmNone(), timeNow());

        FunctionTester.findByName(set, "enumOf")
                .compareReturnValue(low, -1.5, new ArrayDouble(-1.0, 1.0), labels)
                .compareReturnValue(middle, 0.0, new ArrayDouble(-1.0, 1.0), labels)
                .compareReturnValue(high, 1.5, new ArrayDouble(-1.0, 1.0), labels);
    }

    @Test
    public void enumOf2() {
        List<String> labels = Arrays.asList("A", "B", "C", "D");
        ArrayDouble intervals = new ArrayDouble(-10.0, 0.0, 10.0);

        VEnum a = newVEnum(0, labels, alarmNone(), timeNow());
        VEnum b = newVEnum(1, labels, alarmNone(), timeNow());
        VEnum c = newVEnum(2, labels, alarmNone(), timeNow());
        VEnum d = newVEnum(3, labels, alarmNone(), timeNow());

        FunctionTester.findByName(set, "enumOf")
                .compareReturnValue(a, -11.0, intervals, labels)
                .compareReturnValue(b, -1.0, intervals, labels)
                .compareReturnValue(c, 2.0, intervals, labels)
                .compareReturnValue(d, 11.0, intervals, labels);
    }

    @Test
    public void indexOf(){
        Alarm none = alarmNone();
        List<String> labels = Arrays.asList("One", "Two", "Three");
        FunctionTester.findByName(set, "indexOf")
                .compareReturnValue(0, newVEnum(0, labels, none, timeNow()));
    }
}
