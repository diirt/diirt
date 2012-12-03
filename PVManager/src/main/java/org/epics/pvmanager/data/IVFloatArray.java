/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.util.List;
import org.epics.util.array.ListFloat;

/**
 *
 * @author carcassi
 */
class IVFloatArray extends IVNumeric implements VFloatArray {

    private final float[] array;
    private final ListFloat data;
    private final List<Integer> sizes;

    public IVFloatArray(ListFloat data, List<Integer> sizes,
            Alarm alarm, Time time, Display display) {
        super(alarm, time, display);
        this.array = null;
        this.sizes = sizes;
        this.data = data;
    }

    @Override
    public List<Integer> getSizes() {
        return sizes;
    }

    @Override
    public ListFloat getData() {
        return data;
    }

}
