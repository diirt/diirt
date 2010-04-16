/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.types;

import gov.bnl.nsls2.pvmanager.Function;
import gov.bnl.nsls2.pvmanager.TimeDuration;
import gov.bnl.nsls2.pvmanager.TimeInterval;
import gov.bnl.nsls2.pvmanager.TimeStamp;
import gov.bnl.nsls2.pvmanager.TimedCacheCollector;
import java.util.List;
import static gov.bnl.nsls2.pvmanager.TypeSupport.*;

/**
 * Provides an aggregator that returns a synchronized set of data by looking
 * into a timed cache.
 *
 * @author carcassi
 */
class SynchronizedArrayAggregator<T> extends Function<SynchronizedArray<T>> {

    private final SynchronizedArray<T> array = new SynchronizedArray<T>();
    private final TimeDuration duration;
    private final List<TimedCacheCollector<T>> collectors;

    @SuppressWarnings("unchecked")
    public SynchronizedArrayAggregator(List<String> names, List<TimedCacheCollector<T>> collectors, TimeDuration duration) {
        super((Class) SynchronizedArray.class);
        array.setNames(names);
        this.duration = duration;
        this.collectors = collectors;
    }

    @Override
    public SynchronizedArray<T> getValue() {
        TimeStamp reference = electReferenceTimeStamp(collectors);
        array.setTimeStamp(reference);
        TimeInterval allowedInterval = duration.around(reference);
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
