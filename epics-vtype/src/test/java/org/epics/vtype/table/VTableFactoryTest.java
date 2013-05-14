/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.vtype.table;

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
}