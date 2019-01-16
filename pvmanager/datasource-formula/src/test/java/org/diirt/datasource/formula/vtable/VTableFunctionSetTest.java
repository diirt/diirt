/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vtable;

import java.util.Arrays;
import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FunctionTester;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.VType;
import org.diirt.vtype.table.Column;
import org.diirt.vtype.table.VTableFactory;

import static org.diirt.vtype.table.VTableFactory.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class VTableFunctionSetTest {

    private static FormulaFunctionSet set = new VTableFunctionSet();

    @Test
    public void columnOf() {
        VTable data = VTable.of(Arrays.<Class<?>>asList(String.class, double.class, double.class),
                Arrays.asList("x", "y", "z"), Arrays.<Object>asList(Arrays.asList("a", "b", "c"), ArrayDouble.of(1,2,3), ArrayDouble.of(5,4,6)));
        VStringArray expected1 = VStringArray.of(Arrays.asList("a", "b", "c"), Alarm.none(), Time.now());
        VNumberArray expected2 = VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none());
        FunctionTester.findByName(set, "columnOf")
                .compareReturnValue(expected1, data, "x")
                .compareReturnValue(expected2, data, "y")
                .compareReturnValue(null, data, null)
                .compareReturnValue(null, null, "y");
    }

    @Test
    public void tableOf1() {
        VTable expected = VTable.of(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("A", "B"), Arrays.<Object>asList(ArrayDouble.of(0.0, 0.1, 0.2), ArrayDouble.of(1,2,3)));
        Column column1 = VTableFactory.column("A", VTableFactory.step(0, 0.1));
        Column column2 = VTableFactory.column("B", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none()));

        FunctionTester.findByName(set, "tableOf")
                .compareReturnValue(expected, column1, column2);
    }

    @Test
    public void tableOf2() {
        VTable expected = VTable.of(Arrays.<Class<?>>asList(double.class),
                Arrays.asList("B"), Arrays.<Object>asList(ArrayDouble.of(1,2,3)));
        Column column2 = VTableFactory.column("B", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none()));

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
        VStringArray array = VStringArray.of(Arrays.asList("A", "B", "C"), Alarm.none(), Time.now());
        Column column = VTableFactory.column("A", array);

        FunctionTester.findBySignature(set, "column", VString.class, VStringArray.class)
                .compareReturnValue(column, "A", array);
    }

    @Test
    public void tableRangeFilter1() {
        VTable table = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286"), Alarm.none(), Time.now())));

        FunctionTester.findBySignature(set, "tableRangeFilter", VTable.class, VString.class, VType.class, VType.class)
                .compareReturnValue(expected, table, "Slot", 1.0, 2.5)
                .compareReturnValue(null, null, "Slot", 1.0, 2.5)
                .compareReturnValue(null, table, null, 1.0, 2.5)
                .compareReturnValue(null, table, "Slot", null, 2.5)
                .compareReturnValue(null, table, "Slot", 1.0, null);
    }

    @Test
    public void tableValueFilter1() {
        VTable table = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286"), Alarm.none(), Time.now())));

        FunctionTester.findByName(set, "tableValueFilter")
                .compareReturnValue(expected, table, "CPU", "286")
                .compareReturnValue(null, null, "CPU", "286")
                .compareReturnValue(null, table, null, "286")
                .compareReturnValue(null, table, "CPU", null);
    }

    @Test
    public void tableValueFilter2() {
        VTable table = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286"), Alarm.none(), Time.now())));

        FunctionTester.findByName(set, "tableStringMatchFilter")
                .compareReturnValue(expected, table, "CPU", "28")
                .compareReturnValue(null, null, "CPU", "28")
                .compareReturnValue(null, table, null, "28")
                .compareReturnValue(null, table, "CPU", null);
    }

    @Test
    public void tableRangeFilter2() {
        
        VTable table = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286"), Alarm.none(), Time.now())));

        FunctionTester.findBySignature(set, "tableRangeFilter", VTable.class, VString.class, VNumberArray.class)
                .compareReturnValue(expected, table, "Slot", ArrayDouble.of(1.0, 2.5))
                .compareReturnValue(null, null, "Slot", ArrayDouble.of(1.0, 2.5))
                .compareReturnValue(null, table, null, ArrayDouble.of(1.0, 2.5))
                .compareReturnValue(null, table, "Slot", null);
    }

    @Test
    public void tableUnion() {
        VTable table1 = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable table2 = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("B", "B", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(3,2,1), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected = VTableFactory.newVTable(column("Table", VStringArray.of(Arrays.asList("1", "1", "1", "2", "2", "2"), Alarm.none(), Time.now())),
                                 column("Rack", VStringArray.of(Arrays.asList("A", "A", "B", "B", "B", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3,3,2,1), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386","286", "286", "386"), Alarm.none(), Time.now())));

        // TODO: add null cases
        FunctionTester.findBySignature(set, "union", VString.class, VStringArray.class, VTable.class)
                .compareReturnValue(expected, "Table", Arrays.asList("1", "2"), table1, table2);
    }

    @Test
    public void tableUnion2() {
        VTable table1 = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable table2 = VTableFactory.newVTable(column("Rack", VStringArray.of(Arrays.asList("B", "B", "A"), Alarm.none(), Time.now())),
                                 column("Position", VDoubleArray.of(ArrayDouble.of(3,2,1), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected = VTableFactory.newVTable(column("Table", VStringArray.of(Arrays.asList("1", "1", "1", "2", "2", "2"), Alarm.none(), Time.now())),
                                 column("Rack", VStringArray.of(Arrays.asList("A", "A", "B", "B", "B", "A"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3,Double.NaN,Double.NaN,Double.NaN), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386","286", "286", "386"), Alarm.none(), Time.now())),
                                 column("Position", VDoubleArray.of(ArrayDouble.of(Double.NaN,Double.NaN,Double.NaN,3,2,1), Alarm.none(), Time.now(), Display.none())));
        VTable expected2 = VTableFactory.newVTable(column("Table", VStringArray.of(Arrays.asList("1", "1", "1"), Alarm.none(), Time.now())),
                                 column("Rack", VStringArray.of(Arrays.asList("A", "A", "B"), Alarm.none(), Time.now())),
                                 column("Slot", VDoubleArray.of(ArrayDouble.of(1,2,3), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));
        VTable expected3 = VTableFactory.newVTable(column("Table", VStringArray.of(Arrays.asList("2", "2", "2"), Alarm.none(), Time.now())),
                                 column("Rack", VStringArray.of(Arrays.asList("B", "B", "A"), Alarm.none(), Time.now())),
                                 column("Position", VDoubleArray.of(ArrayDouble.of(3,2,1), Alarm.none(), Time.now(), Display.none())),
                                 column("CPU", VStringArray.of(Arrays.asList("286", "286", "386"), Alarm.none(), Time.now())));

        // TODO: add null cases
        FunctionTester.findBySignature(set, "union", VString.class, VStringArray.class, VTable.class)
                .compareReturnValue(expected, "Table", Arrays.asList("1", "2"), table1, table2)
                .compareReturnValue(expected2, "Table", Arrays.asList("1", "2"), table1, null)
                .compareReturnValue(expected3, "Table", Arrays.asList("1", "2"), null, table2)
                .compareReturnValue(null, "Table", Arrays.asList("1", "2"), null, null);
    }

}
