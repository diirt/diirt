/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public final class Hist1DT3 extends MockHistogram1D {

    public Hist1DT3() {
        setMinValueRange(0.0);
        setMaxValueRange(10.0);
        setMinCountRange(0);
        setMaxCountRange(10);
        setBinValueBoundary(new double[] {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
        setBinCount(new int[] {0,2,3,0,4,5,0,6,7});
    }

    
}
