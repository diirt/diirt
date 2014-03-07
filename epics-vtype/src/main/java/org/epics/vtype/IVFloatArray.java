/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.vtype;

import java.util.List;
import org.epics.util.array.ListFloat;
import org.epics.util.array.ListInt;

/**
 *
 * @author carcassi
 */
class IVFloatArray extends IVNumeric implements VFloatArray {

    private final ListFloat data;
    private final ListInt sizes;
    private final List<ArrayDimensionDisplay> dimensionDisplay;
    
    public IVFloatArray(ListFloat data, ListInt sizes,
            Alarm alarm, Time time, Display display) {
        this(data, sizes, null, alarm, time, display);
    }

    public IVFloatArray(ListFloat data, ListInt sizes, List<ArrayDimensionDisplay> dimDisplay,
            Alarm alarm, Time time, Display display) {
        super(alarm, time, display);
        this.sizes = sizes;
        this.data = data;
        if (dimDisplay == null) {
            this.dimensionDisplay = ValueUtil.defaultArrayDisplay(sizes);
        } else {
            this.dimensionDisplay = dimDisplay;
        }
    }

    @Override
    public ListInt getSizes() {
        return sizes;
    }

    @Override
    public ListFloat getData() {
        return data;
    }

    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

    @Override
    public List<ArrayDimensionDisplay> getDimensionDisplay() {
        return dimensionDisplay;
    }

}
