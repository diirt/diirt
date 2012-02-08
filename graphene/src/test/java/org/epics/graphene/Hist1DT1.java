/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class Hist1DT1 implements Histogram1D {
    
    private int height = 200;
    private int width = 300;

    @Override
    public int getImageHeight() {
        return height;
    }

    @Override
    public int getImageWidth() {
        return width;
    }

    @Override
    public double getMinValueRange() {
        return 0.0;
    }

    @Override
    public double getMaxValueRange() {
        return 2.0;
    }

    @Override
    public void setImageHeight(int height) {
        this.height = height;
    }

    @Override
    public void setImageWidth(int width) {
        this.width = width;
    }
    
}
