/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.venum;

import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.formula.*;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.Alarm;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.Time;
import org.epics.vtype.VEnum;

import org.junit.Test;



/**
 *
 * @author carcassi
 */
public class VEnumFunctionSetTest {

    private VEnumFunctionSet set = new VEnumFunctionSet();

    @Test
    public void enumOf1() {
        List<String> labels = Arrays.asList("LOW", "MIDDLE", "HIGH");

        VEnum low = VEnum.of(0, EnumDisplay.of(labels), Alarm.none(), Time.now());
        VEnum middle = VEnum.of(1, EnumDisplay.of(labels), Alarm.none(), Time.now());
        VEnum high = VEnum.of(2, EnumDisplay.of(labels), Alarm.none(), Time.now());

        FunctionTester.findByName(set, "enumOf")
                .compareReturnValue(low, -1.5, ArrayDouble.of(-1.0, 1.0), labels)
                .compareReturnValue(middle, 0.0, ArrayDouble.of(-1.0, 1.0), labels)
                .compareReturnValue(high, 1.5, ArrayDouble.of(-1.0, 1.0), labels);
    }

    @Test
    public void enumOf2() {
        List<String> labels = Arrays.asList("A", "B", "C", "D");
        ArrayDouble intervals = ArrayDouble.of(-10.0, 0.0, 10.0);

        VEnum a = VEnum.of(0, EnumDisplay.of(labels), Alarm.none(), Time.now());
        VEnum b = VEnum.of(1, EnumDisplay.of(labels), Alarm.none(), Time.now());
        VEnum c = VEnum.of(2, EnumDisplay.of(labels), Alarm.none(), Time.now());
        VEnum d = VEnum.of(3, EnumDisplay.of(labels), Alarm.none(), Time.now());

        FunctionTester.findByName(set, "enumOf")
                .compareReturnValue(a, -11.0, intervals, labels)
                .compareReturnValue(b, -1.0, intervals, labels)
                .compareReturnValue(c, 2.0, intervals, labels)
                .compareReturnValue(d, 11.0, intervals, labels);
    }

    @Test
    public void indexOf(){
        Alarm none = Alarm.none();
        List<String> labels = Arrays.asList("One", "Two", "Three");
        FunctionTester.findByName(set, "indexOf")
                .compareReturnValue(0, VEnum.of(0, EnumDisplay.of(labels), none, Time.now()));
    }
}
