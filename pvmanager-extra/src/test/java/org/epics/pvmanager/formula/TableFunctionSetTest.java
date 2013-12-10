/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;
import static org.epics.pvmanager.formula.BaseTestForFormula.testTwoArgNumericFunction;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;
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
    public void columnOf1() {
        VTable data = newVTable(Arrays.<Class<?>>asList(String.class, double.class, double.class),
                Arrays.asList("x", "y", "z"), Arrays.<Object>asList(Arrays.asList("a", "b", "c"), new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        VStringArray expected = newVStringArray(Arrays.asList("a", "b", "c"), alarmNone(), timeNow());
        testFunction(set, "columnOf", expected, data, newVString("x", alarmNone(), timeNow()));
    }
    
    @Test
    public void columnOf2() {
        VTable data = newVTable(Arrays.<Class<?>>asList(String.class, double.class, double.class),
                Arrays.asList("x", "y", "z"), Arrays.<Object>asList(Arrays.asList("a", "b", "c"), new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        VNumberArray expected = newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone());
        testFunction(set, "columnOf", expected, data, newVString("y", alarmNone(), timeNow()));
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
