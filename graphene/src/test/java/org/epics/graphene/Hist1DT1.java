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

    @Override
    public int getImageHeight() {
        return 200;
    }

    @Override
    public int getImageWidth() {
        return 300;
    }
    
}
