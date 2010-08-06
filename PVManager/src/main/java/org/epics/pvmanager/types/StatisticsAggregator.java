/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.types;

import org.epics.pvmanager.Aggregator;
import org.epics.pvmanager.Collector;
import java.util.List;
import static java.lang.Math.*;

class StatisticsAggregator extends Aggregator<DoubleStatistics, Double> {

    StatisticsAggregator(Collector<Double> collector) {
        super(DoubleStatistics.class, collector);
    }

    @Override
    protected DoubleStatistics calculate(List<Double> data) {
        double totalSum = 0;
        double totalSquareSum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double item : data) {
            totalSum += item;
            totalSquareSum += item * item;
            min = min(min, item);
            max = max(min, item);
        }
        DoubleStatistics newValue = new DoubleStatistics();
        newValue.setStatistics(totalSum / data.size(), min, max,
                sqrt(totalSquareSum / data.size() - (totalSum * totalSum) / (data.size() * data.size())));
        return newValue;
    }

}
