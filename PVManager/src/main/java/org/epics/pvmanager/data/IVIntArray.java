/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.text.NumberFormat;
import java.util.List;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
class IVIntArray extends IVNumeric implements VIntArray {

    private final int[] array;
    private final ListInt data;
    private final ListInt sizes;

    public IVIntArray(ListInt data, ListInt sizes,
            Alarm alarm, Time time, Display display) {
        super(alarm, time, display);
        this.array = null;
        this.sizes = sizes;
        this.data = data;
    }

    @Override
    public ListInt getSizes() {
        return sizes;
    }

    @Override
    public ListInt getData() {
        return data;
    }

}
