/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * Aggregates the data out of a Collector into a new data type.
 *
 * @author carcassi
 */
abstract class Aggregator<T extends PVType<T>> extends PVFunction<T> {

    private final Collector collector;
    // TODO There may not be a last value!!!
    private T lastValue = PVType.newInstanceOf(getType());

    Aggregator(Class<T> type, Collector collector) {
        super(type);
        this.collector = collector;
    }

    @Override
    public T getValue() {
        double[] data = collector.getData();
        if (data.length > 0) {
            lastValue = calculate(data);
        }
        return lastValue;
    }

    protected abstract T calculate(double[] data);
}
