/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public final class Hist1DT6 extends MockHistogram1D {

    public Hist1DT6() {
        setMinValueRange(0.999999999999);
        setMaxValueRange(1.0000000004);
        setMinCountRange(0);
        setMaxCountRange(10);
        setBinValueBoundary(new double[] {1.0000, 1.00000000005, 1.0000000001, 1.00000000015, 1.0000000002, 1.00000000025, 1.0000000003, 1.00000000035, 1.0000000004});
        setBinCount(new int[] {0,2,3,0,4,5,0});
    }

    
}
