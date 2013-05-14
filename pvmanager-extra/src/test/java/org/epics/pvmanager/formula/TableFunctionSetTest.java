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
}
