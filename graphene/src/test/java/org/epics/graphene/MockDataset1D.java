/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.CollectionDouble;
import org.epics.util.array.CollectionNumber;
import org.epics.util.array.IteratorDouble;

/**
 *
 * @author carcassi
 */
public class MockDataset1D implements Dataset1D {
    
    private double[] data;
    private double minValue = Double.POSITIVE_INFINITY;
    private double maxValue = Double.NEGATIVE_INFINITY;

    public MockDataset1D(double[] data) {
        for (int i = 0; i < data.length; i++) {
            double d = data[i];
            if (d > maxValue)
                maxValue = d;
            if (d < minValue)
                minValue = d;
        }
        this.data = data;
    }
    
    

    @Override
    public CollectionNumber getValues() {
        return new ArrayDouble(data);
    }

    @Override
    public Number getMinValue() {
        return minValue;
    }

    @Override
    public Number getMaxValue() {
        return maxValue;
    }
    
    
    public static Dataset1D uniform(double minValue, double maxValue, int nSamples) {
        return new MockDataset1D(RangeUtil.createBins(minValue, maxValue, nSamples));
    }
    
    public static Dataset1D gaussian(int nSamples) {
        Random rand = new Random();
        double[] values = new double[nSamples];
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextGaussian();
        }
        return new MockDataset1D(values);
    }

    @Override
    public void update(Dataset1DUpdate update) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
