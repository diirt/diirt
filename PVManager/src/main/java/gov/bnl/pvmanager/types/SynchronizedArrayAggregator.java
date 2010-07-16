/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.types;

import gov.bnl.pvmanager.Function;
import gov.bnl.pvmanager.TimeDuration;
import gov.bnl.pvmanager.TimeInterval;
import gov.bnl.pvmanager.TimeStamp;
import gov.bnl.pvmanager.TimedCacheCollector;
import java.util.List;
import static gov.bnl.pvmanager.TypeSupport.*;

/**
 * Provides an aggregator that returns a synchronized set of data by looking
 * into a timed cache.
 *
 * @author carcassi
 */
class SynchronizedArrayAggregator<T> extends Function<SynchronizedArray<T>> {

    private final SynchronizedArray<T> array = new SynchronizedArray<T>();
    private final TimeDuration tolerance;
    private final List<TimedCacheCollector<T>> collectors;

    /**
     * Creates a new aggregators, that takes a list of collectors
     * and reconstructs a synchronized array.
     *
     * @param names names of the individual pvs
     * @param collectors collectors that contain the past few samples
     * @param tolerance the tolerance around the reference time for samples to be included
     */
    @SuppressWarnings("unchecked")
    public SynchronizedArrayAggregator(List<String> names, List<TimedCacheCollector<T>> collectors, TimeDuration tolerance) {
        super((Class) SynchronizedArray.class);
        array.setNames(names);
        this.tolerance = tolerance;
        this.collectors = collectors;
    }

    @Override
    public SynchronizedArray<T> getValue() {
        TimeStamp reference = electReferenceTimeStamp(collectors);
        if (reference == null)
            return null;

        array.setTimeStamp(reference);
        TimeInterval allowedInterval = tolerance.around(reference);
        int index = 0;
        for (TimedCacheCollector<T> collector : collectors) {
            T value = closestElement(collector.getData(), allowedInterval);
            array.getValues().set(index, value);
            index++;
        }
        return array;
    }

    private static <T> TimeStamp electReferenceTimeStamp(List<TimedCacheCollector<T>> collectors) {
        for (TimedCacheCollector<T> collector : collectors) {
            List<T> data = collector.getData();
            if (data.size() > 1) {
                TimeStamp time = timestampOf(data.get(data.size() - 2));
                if (time != null)
                    return time;
            }
        }
        return null;
    }

    private T closestElement(List<T> data, TimeInterval interval) {
        T latest = null;
        for (T value : data) {
            if (interval.contains(timestampOf(value))) {
                latest = value;
            }
        }
        return latest;
    }

}
