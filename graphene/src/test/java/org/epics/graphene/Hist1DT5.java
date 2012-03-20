/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public final class Hist1DT5 extends MockHistogram1D {

    public Hist1DT5() {
        setMinValueRange(0.99999999999);
        setMaxValueRange(1.000000004);
        setMinCountRange(0);
        setMaxCountRange(10);
        setBinValueBoundary(new double[] {1.0000, 1.0000000005, 1.000000001, 1.0000000015, 1.000000002, 1.0000000025, 1.000000003, 1.0000000035, 1.000000004});
        setBinCount(new int[] {0,2,3,0,4,5,0});
    }

    
}
