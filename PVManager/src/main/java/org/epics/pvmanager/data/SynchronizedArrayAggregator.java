/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.ArrayList;
import java.util.Collections;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimeInterval;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.TimedCacheCollector;
import java.util.List;
import static org.epics.pvmanager.TypeSupport.*;

/**
 * Provides an aggregator that returns a synchronized set of data by looking
 * into a timed cache.
 *
 * @author carcassi
 */
class SynchronizedArrayAggregator extends Function<VMultiDouble> {

    private final TimeDuration tolerance;
    private final List<TimedCacheCollector<VDouble>> collectors;

    /**
     * Creates a new aggregators, that takes a list of collectors
     * and reconstructs a synchronized array.
     *
     * @param names names of the individual pvs
     * @param collectors collectors that contain the past few samples
     * @param tolerance the tolerance around the reference time for samples to be included
     */
    @SuppressWarnings("unchecked")
    public SynchronizedArrayAggregator(List<String> names, List<TimedCacheCollector<VDouble>> collectors, TimeDuration tolerance) {
        super(VMultiDouble.class);
        this.tolerance = tolerance;
        this.collectors = collectors;
    }

    @Override
    public VMultiDouble getValue() {
        TimeStamp reference = electReferenceTimeStamp(collectors);
        if (reference == null)
            return null;

        TimeInterval allowedInterval = tolerance.around(reference);
        List<VDouble> values = new ArrayList<VDouble>(collectors.size());
        for (TimedCacheCollector<VDouble> collector : collectors) {
            VDouble value = closestElement(collector.getData(), allowedInterval);
            values.add(value);
        }
        return ValueFactory.newVMultiDouble(values, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Collections.<String>emptyList(), reference, null,
                null, null, null, null, null, null, null, null, null, null);
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

    private static <T> T closestElement(List<T> data, TimeInterval interval) {
        T latest = null;
        for (T value : data) {
            if (interval.contains(timestampOf(value))) {
                latest = value;
            }
        }
        return latest;
    }

}
