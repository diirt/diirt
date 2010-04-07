/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.bnl.nsls2.pvmanager.types.SynchronizedArray;
import java.util.List;

/**
 *
 * @author carcassi
 */
class SynchronizedArrayAggregator<T> extends PVFunction<SynchronizedArray<T>> {

    private final SynchronizedArray<T> array = new SynchronizedArray<T>();
    private final TimeDuration timeInterval;

    @SuppressWarnings("unchecked")
    public SynchronizedArrayAggregator(List<String> names, List<TimedCacheCollector<T>> collectors, TimeDuration timeInterval) {
        super((Class) SynchronizedArray.class);
        array.setNames(names);
        this.timeInterval = timeInterval;
    }

    @Override
    public SynchronizedArray<T> getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
