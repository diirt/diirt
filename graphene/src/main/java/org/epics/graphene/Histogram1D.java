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
     * Changes the plot height.
     * 
     * @param height new height in px
     */
    public void setImageHeight(int height);
    
    /**
     * The plot width.
     * 
     * @return width in px
     */
    public int getImageWidth();
    
    /**
     * Changes the plot height.
     * 
     * @param width new height in px
     */
    public void setImageWidth(int width);
    
    
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
    

    public int getMaxCountRange();

    public void setMaxCountRange(int maxCountRange);

    public int getMinCountRange();

    public void setMinCountRange(int minCountRange);
}
