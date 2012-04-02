/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public final class Hist1DT4 extends MockHistogram1D {

    public Hist1DT4() {
        setMinValueRange(0.999999999);
        setMaxValueRange(1.0000004);
        setMinCountRange(0);
        setMaxCountRange(10);
        setBinValueBoundary(new double[] {1.0000, 1.00000005, 1.0000001, 1.00000015, 1.0000002, 1.00000025, 1.0000003, 1.00000035, 1.0000004});
        setBinCount(new int[] {0,2,3,0,4,5,0});
    }

    
}
