/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.types;

import org.epics.pvmanager.Aggregator;
import org.epics.pvmanager.Collector;
import java.util.List;

/**
 * Aggregates the values by taking the average.
 * 
 * @author carcassi
 */
class AverageAggregator extends Aggregator<Double, Double> {

    AverageAggregator(Collector<Double> collector) {
        super(Double.class, collector);
    }

    @Override
    protected Double calculate(List<Double> data) {
        double average = 0;
        for (double item : data) {
            average += item;
        }
        return average / data.size();
    }

}
