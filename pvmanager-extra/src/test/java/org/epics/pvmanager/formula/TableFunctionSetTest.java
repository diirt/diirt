/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;
import static org.epics.vtype.ValueFactory.*;
import org.epics.vtype.table.Column;
import org.epics.vtype.table.VTableFactory;
import static org.epics.vtype.table.VTableFactory.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
    
    @Test
    public void column1() {
        VStringArray array = newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), timeNow());
        Column column = VTableFactory.column("A", array);

        FunctionTester.findBySignature(set, "column", VString.class, VStringArray.class)
                .compareReturnValue(column, "A", array);
    }
    
    @Test
    public void tableRangeFilter1() {
        VTable table = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable expected = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286"), alarmNone(), timeNow())));

        FunctionTester.findByName(set, "tableRangeFilter")
                .compareReturnValue(expected, table, "Slot", 1.0, 2.5)
                .compareReturnValue(null, null, "Slot", 1.0, 2.5)
                .compareReturnValue(null, table, null, 1.0, 2.5)
                .compareReturnValue(null, table, "Slot", null, 2.5)
                .compareReturnValue(null, table, "Slot", 1.0, null);
    }
    
    @Test
    public void tableValueFilter1() {
        VTable table = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable expected = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286"), alarmNone(), timeNow())));

        FunctionTester.findByName(set, "tableValueFilter")
                .compareReturnValue(expected, table, "CPU", "286")
                .compareReturnValue(null, null, "CPU", "286")
                .compareReturnValue(null, table, null, "286")
                .compareReturnValue(null, table, "CPU", null);
    }
    
}
