/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class Histogram1DUpdate {

    protected Integer imageHeight;
    protected Integer imageWidth;
    
    public Histogram1DUpdate imageHeight(int height) {
        this.imageHeight = height;
        return this;
    }
    
    public Histogram1DUpdate imageWidth(int width) {
        this.imageWidth = width;
        return this;
    }
    
    public Integer getImageHeight() {
        return imageHeight;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }
    
    
}
