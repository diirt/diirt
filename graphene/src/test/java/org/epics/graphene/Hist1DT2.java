/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public final class Hist1DT2 extends MockHistogram1D {

    public Hist1DT2() {
        setImageHeight(200);
        setImageWidth(300);
        setMinValueRange(-10.0);
        setMaxValueRange(10.0);
        setMinCountRange(0);
        setMaxCountRange(550);
    }

    
}
