/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.stats.Statistics;
import java.util.Random;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.diirt.util.stats.Range;

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
    public Range getDisplayRange() {
        return null;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public ListNumber getValues() {
        return values;
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
