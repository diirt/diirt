/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.epics.util.array.CollectionNumber;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public interface Point1DDataset {
    
    public ListNumber getValues();
    
    public Statistics getStatistics();
    
    public Number getMinValue();
    
    public Number getMaxValue();
    
    public int getCount();
    
    public void update(Point1DDatasetUpdate update);
}
