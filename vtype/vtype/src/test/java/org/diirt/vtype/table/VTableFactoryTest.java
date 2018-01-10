/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayInt;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.diirt.vtype.table.VTableFactory.*;
import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class VTableFactoryTest {

    public VTableFactoryTest() {
    }

    @Test
    public void table1() {
        VTable table = newVTable(column("Index", step(0, 1)),
                                 column("Value", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        assertThat(table.getColumnCount(), equalTo(2));
        assertThat(table.getRowCount(), equalTo(3));
        assertThat(table.getColumnName(0), equalTo("Index"));
        assertThat(table.getColumnName(1), equalTo("Value"));
        assertThat(table.getColumnType(0), equalTo((Object) double.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayDouble(0,1,2)));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(3,2,1)));
    }

    @Test
    public void table2() {
        VTable table = newVTable(column("Index", range(0, 1.5)),
                                 column("Value", newVDoubleArray(new ArrayDouble(4.0,3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        assertThat(table.getColumnCount(), equalTo(2));
        assertThat(table.getRowCount(), equalTo(4));
        assertThat(table.getColumnName(0), equalTo("Index"));
        assertThat(table.getColumnName(1), equalTo("Value"));
        assertThat(table.getColumnType(0), equalTo((Object) double.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayDouble(0,0.5,1,1.5)));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(4,3,2,1)));
    }

    @Test
    public void selectRows1() {
        VTable table = newVTable(column("Index", step(0, 1)),
                                 column("Value", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable rowTable = extractRow(table, 1);
        assertThat(rowTable.getColumnCount(), equalTo(2));
        assertThat(rowTable.getRowCount(), equalTo(1));
        assertThat(rowTable.getColumnName(0), equalTo("Index"));
        assertThat(rowTable.getColumnName(1), equalTo("Value"));
        assertThat(rowTable.getColumnType(0), equalTo((Object) double.class));
        assertThat(rowTable.getColumnType(1), equalTo((Object) double.class));
        assertThat(rowTable.getColumnData(0), equalTo((Object) new ArrayDouble(1)));
        assertThat(rowTable.getColumnData(1), equalTo((Object) new ArrayDouble(2)));
    }

    @Test
    public void join1() {
        VTable table1 = newVTable(column("Index", newVDoubleArray(new ArrayDouble(1.0,2.0,3.0), alarmNone(), timeNow(), displayNone())),
                                 column("Value1", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable table2 = newVTable(column("Value2", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())),
                                 column("Index", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable table = join(table1, table2);
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(3));
        assertThat(table.getColumnName(0), equalTo("Index"));
        assertThat(table.getColumnName(1), equalTo("Value1"));
        assertThat(table.getColumnName(2), equalTo("Value2"));
        assertThat(table.getColumnType(0), equalTo((Object) double.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayDouble(1,2,3)));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(3,2,1)));
        assertThat(table.getColumnData(2), equalTo((Object) new ArrayDouble(1,2,3)));
    }

    @Test
    public void join2() {
        VTable table1 = newVTable(column("Name", newVStringArray(Arrays.asList("Gabriele", "Kunal", "Eric"), alarmNone(), timeNow())),
                                 column("Score", newVDoubleArray(new ArrayDouble(9.0,10.0,8.0), alarmNone(), timeNow(), displayNone())));
        VTable table2 = newVTable(column("Name", newVStringArray(Arrays.asList("Gabriele", "Kunal", "Eric"), alarmNone(), timeNow())),
                                 column("Title", newVStringArray(Arrays.asList("Eng", "Dr", "Mr"), alarmNone(), timeNow())));
        VTable table = join(table1, table2);
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(3));
        assertThat(table.getColumnName(0), equalTo("Name"));
        assertThat(table.getColumnName(1), equalTo("Score"));
        assertThat(table.getColumnName(2), equalTo("Title"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("Gabriele", "Kunal", "Eric")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(9,10,8)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("Eng", "Dr", "Mr")));
    }

    @Test
    public void join3() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table2 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(2,3,4,3), alarmNone(), timeNow(), displayNone())),
                                 column("Price", newVDoubleArray(new ArrayDouble(300,300,400,500), alarmNone(), timeNow(), displayNone())));
        VTable table = join(table1, table2);
        assertThat(table.getColumnCount(), equalTo(4));
        assertThat(table.getRowCount(), equalTo(2));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnName(3), equalTo("Price"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnType(3), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A", "B")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(2,3)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286", "386")));
        assertThat(table.getColumnData(3), equalTo((Object) new ArrayDouble(300,500)));
    }

    @Test
    public void join4() {
        VTable table1 = newVTable(column("Rack", newVStringArray(new ArrayList<String>(), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(new ArrayList<String>(), alarmNone(), timeNow())));
        VTable table2 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(2,3,4,3), alarmNone(), timeNow(), displayNone())),
                                 column("Price", newVDoubleArray(new ArrayDouble(300,300,400,500), alarmNone(), timeNow(), displayNone())));
        VTable table = join(table1, table2);
        assertThat(table.getColumnCount(), equalTo(4));
        assertThat(table.getRowCount(), equalTo(0));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnName(3), equalTo("Price"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnType(3), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayList<String>()));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble()));
        assertThat(table.getColumnData(2), equalTo((Object) new ArrayList<String>()));
        assertThat(table.getColumnData(3), equalTo((Object) new ArrayDouble()));
    }

    @Test
    public void union1() {
        VTable table1 = newVTable(column("A", newVDoubleArray(new ArrayDouble(1.0,2.0,3.0), alarmNone(), timeNow(), displayNone())),
                                 column("B", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable table2 = newVTable(column("A", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())),
                                 column("B", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable table = union((VString) ValueFactory.toVType("C"), (VStringArray) ValueFactory.toVType(Arrays.asList("A", "B")), table1, table2);
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(6));
        assertThat(table.getColumnName(0), equalTo("C"));
        assertThat(table.getColumnName(1), equalTo("A"));
        assertThat(table.getColumnName(2), equalTo("B"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A","A","A","B","B","B")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(1,2,3,3,2,1)));
        assertThat(table.getColumnData(2), equalTo((Object) new ArrayDouble(3,2,1,3,2,1)));
    }

    @Test
    public void union2() {
        VTable table1 = newVTable(column("A", newVDoubleArray(new ArrayDouble(1.0,2.0,3.0), alarmNone(), timeNow(), displayNone())),
                                 column("B", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable table2 = newVTable(column("A", newVDoubleArray(new ArrayDouble(3.0,2.0,1.0), alarmNone(), timeNow(), displayNone())));
        VTable table = union((VString) ValueFactory.toVType("C"), (VStringArray) ValueFactory.toVType(Arrays.asList("B", "A")), table1, table2);
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(6));
        assertThat(table.getColumnName(0), equalTo("C"));
        assertThat(table.getColumnName(1), equalTo("A"));
        assertThat(table.getColumnName(2), equalTo("B"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) double.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("B","B","B","A","A","A")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(1,2,3,3,2,1)));
        assertThat(table.getColumnData(2), equalTo((Object) new ArrayDouble(3,2,1,Double.NaN,Double.NaN,Double.NaN)));
    }

    @Test
    public void valueTable1() {
        VDouble value1 = newVDouble(3.1);
        VDouble value2 = newVDouble(3.2, newAlarm(AlarmSeverity.MINOR, "HI"), timeNow(), displayNone());
        VDouble value3 = newVDouble(3.3);
        VTable table = valueTable(Arrays.asList(value1, value2, value3));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(3));
        assertThat(table.getColumnName(0), equalTo("Value"));
        assertThat(table.getColumnName(1), equalTo("Severity"));
        assertThat(table.getColumnName(2), equalTo("Status"));
        assertThat(table.getColumnType(0), equalTo((Object) double.class));
        assertThat(table.getColumnType(1), equalTo((Object) String.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayDouble(3.1,3.2,3.3)));
        assertThat(table.getColumnData(1), equalTo((Object) Arrays.asList("NONE", "MINOR", "NONE")));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("NONE", "HI", "NONE")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueTable2() {
        VDouble value1 = newVDouble(3.1);
        VString value2 = newVString("test", newAlarm(AlarmSeverity.MINOR, "HI"), timeNow());
        VDouble value3 = newVDouble(3.3);
        VTable table = valueTable(Arrays.asList(value1, value2, value3));
    }

    @Test
    public void valueTable3() {
        VTable table = valueTable(Collections.<VType>emptyList());
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(0));
        assertThat(table.getColumnName(0), equalTo("Value"));
        assertThat(table.getColumnName(1), equalTo("Severity"));
        assertThat(table.getColumnName(2), equalTo("Status"));
        assertThat(table.getColumnType(0), equalTo((Object) double.class));
        assertThat(table.getColumnType(1), equalTo((Object) String.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayDouble()));
        assertThat(table.getColumnData(1), equalTo((Object) Arrays.asList()));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList()));
    }

    @Test
    public void valueTable4() {
        VTable table = valueTable(Arrays.asList((VType) null));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(0));
        assertThat(table.getColumnName(0), equalTo("Value"));
        assertThat(table.getColumnName(1), equalTo("Severity"));
        assertThat(table.getColumnName(2), equalTo("Status"));
        assertThat(table.getColumnType(0), equalTo((Object) double.class));
        assertThat(table.getColumnType(1), equalTo((Object) String.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) new ArrayDouble()));
        assertThat(table.getColumnData(1), equalTo((Object) Arrays.asList()));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList()));
    }

    @Test
    public void valueTable5() {
        VDouble value1 = newVDouble(3.1);
        VDouble value2 = newVDouble(3.2, newAlarm(AlarmSeverity.MINOR, "HI"), timeNow(), displayNone());
        VDouble value3 = newVDouble(3.3);
        VTable table = valueTable(Arrays.asList("A", "B", "C"), Arrays.asList(value1, value2, value3));
        assertThat(table.getColumnCount(), equalTo(4));
        assertThat(table.getRowCount(), equalTo(3));
        assertThat(table.getColumnName(0), equalTo("Name"));
        assertThat(table.getColumnName(1), equalTo("Value"));
        assertThat(table.getColumnName(2), equalTo("Severity"));
        assertThat(table.getColumnName(3), equalTo("Status"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnType(3), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A", "B", "C")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(3.1,3.2,3.3)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("NONE", "MINOR", "NONE")));
        assertThat(table.getColumnData(3), equalTo((Object) Arrays.asList("NONE", "HI", "NONE")));
    }

    @Test
    public void valueTable6() {
        VTable table = valueTable(Arrays.asList("A"), Arrays.asList((VType) null));
        assertThat(table.getColumnCount(), equalTo(4));
        assertThat(table.getRowCount(), equalTo(0));
        assertThat(table.getColumnName(0), equalTo("Name"));
        assertThat(table.getColumnName(1), equalTo("Value"));
        assertThat(table.getColumnName(2), equalTo("Severity"));
        assertThat(table.getColumnName(3), equalTo("Status"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnType(3), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList()));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble()));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList()));
        assertThat(table.getColumnData(3), equalTo((Object) Arrays.asList()));
    }

    @Test
    public void valueTable7() {
        VTable table = valueTable(Arrays.asList("A", "B", "C", "D"), Arrays.asList((VType) null, null, null, null));
        assertThat(table.getColumnCount(), equalTo(4));
        assertThat(table.getRowCount(), equalTo(0));
        assertThat(table.getColumnName(0), equalTo("Name"));
        assertThat(table.getColumnName(1), equalTo("Value"));
        assertThat(table.getColumnName(2), equalTo("Severity"));
        assertThat(table.getColumnName(3), equalTo("Status"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnType(3), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList()));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble()));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList()));
        assertThat(table.getColumnData(3), equalTo((Object) Arrays.asList()));
    }

    @Test
    public void select1() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table = select(table1, new ArrayInt(1));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(1));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(2)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286")));
    }

    @Test
    public void tableValueFilter1() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table = tableValueFilter(table1, "Slot", ValueFactory.toVType(2));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(1));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(2)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286")));
    }

    @Test
    public void tableValueFilter2() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table = tableValueFilter(table1, "CPU", ValueFactory.toVType("286"));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(2));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A", "A")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(1, 2)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286", "286")));
    }

    @Test
    public void tableStringMatchFilter1() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table = tableStringMatchFilter(table1, "CPU", "28");
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(2));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A", "A")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(1, 2)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286", "286")));
    }

    @Test
    public void tableRangeFilter1() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table = tableRangeFilter(table1, "Slot", ValueFactory.toVType(1), ValueFactory.toVType(2));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(1));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(1)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286")));
    }

    @Test
    public void tableRangeFilter2() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        VTable table = tableRangeFilter(table1, "Rack", ValueFactory.toVType("A"), ValueFactory.toVType("BB"));
        assertThat(table.getColumnCount(), equalTo(3));
        assertThat(table.getRowCount(), equalTo(2));
        assertThat(table.getColumnName(0), equalTo("Rack"));
        assertThat(table.getColumnName(1), equalTo("Slot"));
        assertThat(table.getColumnName(2), equalTo("CPU"));
        assertThat(table.getColumnType(0), equalTo((Object) String.class));
        assertThat(table.getColumnType(1), equalTo((Object) double.class));
        assertThat(table.getColumnType(2), equalTo((Object) String.class));
        assertThat(table.getColumnData(0), equalTo((Object) Arrays.asList("A", "B")));
        assertThat(table.getColumnData(1), equalTo((Object) new ArrayDouble(1, 2)));
        assertThat(table.getColumnData(2), equalTo((Object) Arrays.asList("286", "286")));
    }
}