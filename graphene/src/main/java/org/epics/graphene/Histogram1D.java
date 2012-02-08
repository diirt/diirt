/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public interface Histogram1D {
    
    /**
     * The plot height.
     * 
     * @return height in px
     */
    public int getImageHeight();
    
    /**
     * The plot width.
     * 
     * @return width in px
     */
    public int getImageWidth();
    
    /**
     * The minimum value that should be displayed in the 
     * x axis of the plot.
     * 
     * @return minimum x axis value
     */
    public double getMinValueRange();
    
    /**
     * The maximum value that should be displayed
     * in the x axis of the plot.
     * 
     * @return maximum x axis value
     */
    public double getMaxValueRange();
}
