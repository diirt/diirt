/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.epics.util.array.ListNumber;

/**
 * A dataset consisting on a set of 2D points.
 * <p>
 * It represents a list of ordered tuples of two values, and their statistical information.
 * The order may not be meaningful, but can be used to identify the points.
 *
 * @author carcassi
 */
public interface Point2DDataset {
    
    /**
     * The x values of the point.
     * <p>
     * If the dataset is empty, it returns an empty list.
     * 
     * @return the x values; never null
     */
    public ListNumber getXValues();
    
    /**
     * The y values of the point.
     * <p>
     * If the dataset is empty, it returns an empty list.
     * 
     * @return the y values; never null
     */
    public ListNumber getYValues();
    
    public double getXMinValue();
    
    public double getXMaxValue();
    
    public double getYMinValue();
    
    public double getYMaxValue();
    
    /**
     * The number of points in the dataset.
     * <p>
     * This number matches the size of the list returned by {@link #getValues() }.
     * 
     * @return the number of values in this dataset
     */
    public int getCount();
    
}
