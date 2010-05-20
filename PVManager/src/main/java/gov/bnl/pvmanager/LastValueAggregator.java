package gov.bnl.pvmanager;

import java.util.List;

/**
 * Returns the last value in the time slice.
 * 
 * @author carcassi
 */
class LastValueAggregator<T> extends Aggregator<T, T> {

    LastValueAggregator(Class<T> clazz, Collector<T> collector) {
        super(clazz, collector);
    }

    @Override
    protected T calculate(List<T> data) {
        return data.get(data.size() - 1);
    }

}
