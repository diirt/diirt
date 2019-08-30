/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.ad;

import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.displayNone;
import static org.diirt.vtype.ValueFactory.newVIntArray;
import static org.diirt.vtype.ValueFactory.newVLongArray;
import static org.diirt.vtype.ValueFactory.newVEnum;
import static org.diirt.vtype.ValueFactory.timeNow;

import java.util.Arrays;
import java.util.List;

import org.diirt.datasource.formula.FunctionTester;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayLong;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VNumberArray;
import org.junit.Test;

/**
 * Test Area detector data manipulation formulas
 * 
 * @author kunal
 *
 */
public class AreaDetectorFunctionSetTest {

    private AreaDetectorFunctionSet set = new AreaDetectorFunctionSet();
    private final List<String> labels = Arrays.asList("Int8", "UInt8", "Int16", "UInt16", "Int32", "UInt32", "Float32",
            "Float64");

    @Test
    public void arrayOfInt8() {
        VNumberArray array = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray expectedArray = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "adData-enum", VNumberArray.class, VEnum.class)
                .compareReturnValue(expectedArray, array, newVEnum(0, labels, alarmNone(), timeNow()));

    }

    @Test
    public void arrayOfUInt8() {
        VNumberArray array = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray expectedArray = newVIntArray(new ArrayInt(255, 0, 1), alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "adData-enum", VNumberArray.class, VEnum.class)
                .compareReturnValue(expectedArray, array, newVEnum(1, labels, alarmNone(), timeNow()));

    }

    @Test
    public void arrayOfInt16() {
        VNumberArray array = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray expectedArray = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "adData-enum", VNumberArray.class, VEnum.class)
                .compareReturnValue(expectedArray, array, newVEnum(2, labels, alarmNone(), timeNow()));

    }

    @Test
    public void arrayOfUInt16() {
        VNumberArray array = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray expectedArray = newVIntArray(new ArrayInt(65535, 0, 1), alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "adData-enum", VNumberArray.class, VEnum.class)
                .compareReturnValue(expectedArray, array, newVEnum(3, labels, alarmNone(), timeNow()));

    }

    @Test
    public void arrayOfInt32() {
        VNumberArray array = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray expectedArray = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "adData-enum", VNumberArray.class, VEnum.class)
                .compareReturnValue(expectedArray, array, newVEnum(4, labels, alarmNone(), timeNow()));

    }

    @Test
    public void arrayOfUInt32() {
        VNumberArray array = newVIntArray(new ArrayInt(-1, 0, 1), alarmNone(), timeNow(), displayNone());
        VNumberArray expectedArray = newVLongArray(new ArrayLong(4294967295L, 0, 1), alarmNone(), timeNow(), displayNone());

        FunctionTester.findBySignature(set, "adData-enum", VNumberArray.class, VEnum.class)
                .compareReturnValue(expectedArray, array, newVEnum(5, labels, alarmNone(), timeNow()));

    }
}
