/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.util.List;

/**
 * Aggregates the data out of a Collector into a new data type.
 *
 * @author carcassi
 */
public abstract class Aggregator<T, E> extends Function<T> {

    private final Collector<E> collector;
    // TODO There may not be a last value!!!
    private T lastValue;

    Aggregator(Class<T> type, Collector<E> collector) {
        super(type);
        this.collector = collector;
    }

    @Override
    public T getValue() {
        List<E> data = collector.getData();
        if (data.size() > 0) {
            lastValue = calculate(data);
        }
        return lastValue;
    }

    protected abstract T calculate(List<E> data);
}
