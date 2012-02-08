/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class Hist1DT2 implements Histogram1D {

    @Override
    public int getImageHeight() {
        return 600;
    }

    @Override
    public int getImageWidth() {
        return 800;
    }

    @Override
    public double getMinValueRange() {
        return -10.0;
    }

    @Override
    public double getMaxValueRange() {
        return 10.0;
    }
    
}
