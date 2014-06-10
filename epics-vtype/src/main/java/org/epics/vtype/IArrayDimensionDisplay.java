/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.vtype;

import org.epics.util.array.ListNumber;

class IArrayDimensionDisplay implements ArrayDimensionDisplay {
    
    private final ListNumber cellBoundaries;
    private final boolean reversed;
    private final String units;

    public IArrayDimensionDisplay(ListNumber cellBoundaries, boolean reversed, String units) {
        this.cellBoundaries = cellBoundaries;
        this.reversed = reversed;
        this.units = units;
    }
    
    @Override
    public ListNumber getCellBoundaries() {
        return cellBoundaries;
    }

    @Override
    public String getUnits() {
        return units;
    }

    @Override
    public boolean isReversed() {
        return reversed;
    }
    
}
