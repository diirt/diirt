/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.vtype.ndarray;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.BufferInt;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.array.ListNumber;
import org.epics.util.array.ListNumbers;
import org.epics.vtype.ArrayDimensionDisplay;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.VType;
import org.epics.vtype.ValueFactory;
import org.epics.vtype.ValueUtil;
import org.epics.vtype.table.ListNumberProvider;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class VNumberArrayFactory {
    
    public static DimensionInfo dimInfo(final int size, final ListNumberProvider boundaryProvider, final boolean invert, final String unit) {
        return new DimensionInfo(size, boundaryProvider.createListNumber(size + 1), invert, unit);
    }
    
    public static DimensionInfo dimInfo(final int size, final ListNumberProvider boundaryProvider, final boolean invert) {
        return new DimensionInfo(size, boundaryProvider.createListNumber(size + 1), invert, "");
    }
    
    public static DimensionInfo dimInfo(final int size, final ListNumberProvider boundaryProvider) {
        return new DimensionInfo(size, boundaryProvider.createListNumber(size + 1), false, "");
    }
    
    public static DimensionInfo dimInfo(final ListNumber boundaries, final boolean invert, final String unit) {
        return new DimensionInfo(boundaries.size() - 1, boundaries, invert, unit);
    }
    
    public static DimensionInfo dimInfo(final int size, final boolean invert, final String unit) {
        return dimInfo(ListNumbers.linearList(0, 1, size + 1), invert, unit);
    }
    
    public static DimensionInfo dimInfo(final int size, final boolean invert) {
        return dimInfo(ListNumbers.linearList(0, 1, size + 1), invert, "");
    }
    
    public static DimensionInfo dimInfo(final int size) {
        return dimInfo(ListNumbers.linearList(0, 1, size + 1), false, "");
    }
    
    public static VNumberArray ndArray(VNumberArray data, DimensionInfo... dimensions) {
        int[] sizes = new int[dimensions.length];
        boolean[] invert = new boolean[dimensions.length];
        List<ArrayDimensionDisplay> displays = new ArrayList<>();
        for (int i = 0; i < dimensions.length; i++) {
            DimensionInfo dimensionInfo = dimensions[i];
            sizes[i] = dimensionInfo.getSize();
            invert[i] = dimensionInfo.isInvert();
            displays.add(ValueFactory.newDisplay(dimensionInfo.getBoundaries(), dimensionInfo.getUnit()));
        }
        return ValueFactory.newVNumberArray(data.getData(), new ArrayInt(sizes), displays, data, data, data);
    }
}
