/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.vtype.ndarray;

import java.util.Objects;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class DimensionInfo {
    
    private final int size;
    private final ListNumber boundaries;
    private final boolean invert;
    private final String unit;

    public DimensionInfo(int size, ListNumber boundaries, boolean invert, String unit) {
        this.size = size;
        this.boundaries = boundaries;
        this.invert = invert;
        this.unit = unit;
    }

    public int getSize() {
        return size;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isInvert() {
        return invert;
    }

    public ListNumber getBoundaries() {
        return boundaries;
    }

    @Override
    public int hashCode() {
        return getBoundaries().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DimensionInfo) {
            DimensionInfo info = (DimensionInfo) obj;
            return Objects.equals(info.getSize(), getSize()) &&
                    Objects.equals(info.isInvert(), isInvert()) &&
                    Objects.equals(info.getUnit(), getUnit()) &&
                    Objects.equals(info.getBoundaries(), getBoundaries());
                    
        }
        
        return false;
    }
    
}
