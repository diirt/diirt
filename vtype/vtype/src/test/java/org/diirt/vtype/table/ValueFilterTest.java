/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.table;

import java.util.Arrays;
import org.diirt.util.array.ArrayDouble;
import org.diirt.vtype.VTable;
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
public class ValueFilterTest {

    @Test
    public void filterRowNumber() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        ValueFilter valueFilter = new ValueFilter(table1, "Slot", ValueFactory.toVType(2));
        assertThat(valueFilter.filterRow(0), equalTo(false));
        assertThat(valueFilter.filterRow(1), equalTo(true));
        assertThat(valueFilter.filterRow(2), equalTo(false));
    }

    @Test
    public void filterRowString() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        ValueFilter valueFilter = new ValueFilter(table1, "CPU", ValueFactory.toVType("286"));
        assertThat(valueFilter.filterRow(0), equalTo(true));
        assertThat(valueFilter.filterRow(1), equalTo(true));
        assertThat(valueFilter.filterRow(2), equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterRowTypeMismatch1() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        ValueFilter valueFilter = new ValueFilter(table1, "CPU", ValueFactory.toVType(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void filterRowTypeMismatch2() {
        VTable table1 = newVTable(column("Rack", newVStringArray(Arrays.asList("A", "A", "B"), alarmNone(), timeNow())),
                                 column("Slot", newVDoubleArray(new ArrayDouble(1,2,3), alarmNone(), timeNow(), displayNone())),
                                 column("CPU", newVStringArray(Arrays.asList("286", "286", "386"), alarmNone(), timeNow())));
        ValueFilter valueFilter = new ValueFilter(table1, "Slot", ValueFactory.toVType("286"));
    }
}