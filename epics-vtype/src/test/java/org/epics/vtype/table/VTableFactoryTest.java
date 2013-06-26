/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.vtype.table;

import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.VTable;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.vtype.table.VTableFactory.*;
import static org.epics.vtype.ValueFactory.*;

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
}