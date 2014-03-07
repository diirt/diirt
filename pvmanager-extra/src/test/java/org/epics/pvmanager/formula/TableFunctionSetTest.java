/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import static org.epics.vtype.ValueFactory.*;
import org.epics.vtype.table.Column;
import org.epics.vtype.table.VTableFactory;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class TableFunctionSetTest extends BaseTestForFormula {

    private static FormulaFunctionSet set = new TableFunctionSet();
    
    @Test
    public void columnOf() {
        VTable data = newVTable(Arrays.<Class<?>>asList(String.class, double.class, double.class),
                Arrays.asList("x", "y", "z"), Arrays.<Object>asList(Arrays.asList("a", "b", "c"), new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        VStringArray expected1 = newVStringArray(Arrays.asList("a", "b", "c"), alarmNone(), timeNow());
        VNumberArray expected2 = newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone());
        FunctionTester.findByName(set, "columnOf")
                .compareReturnValue(expected1, data, "x")
                .compareReturnValue(expected2, data, "y")
                .compareReturnValue(null, data, null)
                .compareReturnValue(null, null, "y");
    }
    
    @Test
    public void tableOf1() {
        VTable expected = newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("A", "B"), Arrays.<Object>asList(new ArrayDouble(0.0, 0.1, 0.2), new ArrayDouble(1,2,3)));
        Column column1 = VTableFactory.column("A", VTableFactory.step(0, 0.1));
        Column column2 = VTableFactory.column("B", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone()));

        FunctionTester.findByName(set, "tableOf")
                .compareReturnValue(expected, column1, column2);
    }
    
    @Test
    public void tableOf2() {
        VTable expected = newVTable(Arrays.<Class<?>>asList(double.class),
                Arrays.asList("B"), Arrays.<Object>asList(new ArrayDouble(1,2,3)));
        Column column2 = VTableFactory.column("B", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone()));

        FunctionTester.findByName(set, "tableOf")
                .compareReturnValue(expected, null, column2);
    }
    
    @Test
    public void tableOf3() {
        Column column1 = VTableFactory.column("A", VTableFactory.step(0, 0.1));

        FunctionTester.findByName(set, "tableOf")
                .compareReturnValue(null, column1, null);
    }
}
