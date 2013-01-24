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
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class MockDataset1D implements Point1DDataset {
    
    private ListNumber values;
    private double minValue = Double.POSITIVE_INFINITY;
    private double maxValue = Double.NEGATIVE_INFINITY;
    private Statistics statistics;

    public MockDataset1D(double[] data) {
        values = new ArrayDouble(data);
        statistics = StatisticsUtil.statisticsOf(values);
    }

    @Override
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public ListNumber getValues() {
        return values;
    }
    
    public static Point1DDataset uniform(double minValue, double maxValue, int nSamples) {
        return new MockDataset1D(RangeUtil.createBins(minValue, maxValue, nSamples));
    }
    
    public static Point1DDataset gaussian(int nSamples) {
        Random rand = new Random();
        double[] values = new double[nSamples];
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextGaussian();
        }
        return new MockDataset1D(values);
    }
}
