/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.vtype.ndarray;

import org.epics.util.array.ListInt;

/**
 *
 * @author carcassi
 */
public class Array2DDouble {
    
    private final int x1Size;
    private final int x0Size;
    private final boolean x1Invert;
    private final boolean x0Invert;

    public Array2DDouble(ListInt sizes, boolean[] invert) {
        this.x0Size = sizes.getInt(0);
        this.x1Size = sizes.getInt(1);
        this.x0Invert = invert[0];
        this.x1Invert = invert[1];
    }
    
    public int getIndex(int x1, int x0) {
        int index;
        if (!x1Invert) {
            index = x1;
        } else {
            index = x1Size - x1 - 1;
        }
        index *= x0Size;
        if (!x0Invert) {
            index += x0;
        } else {
            index += x0Size - x0 - 1;
        }
        return index;
    }
    
}
