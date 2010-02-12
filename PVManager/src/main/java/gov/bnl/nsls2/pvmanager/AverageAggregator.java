package gov.bnl.nsls2.pvmanager;

/**
 * Aggregates the values by taking the average.
 * 
 * @author carcassi
 */
public class AverageAggregator extends Aggregator<TypeDouble> {

    private final Collector collector;
    // TODO There may not be a last value!!!
    private TypeDouble lastValue = new TypeDouble();

    public AverageAggregator(Collector collector) {
        this.collector = collector;
    }

    @Override
    public synchronized TypeDouble getValue() {
        double[] data = collector.getData();
        if (data.length > 0) {
            lastValue = new TypeDouble();
            synchronized (lastValue) {
                lastValue.setDouble(calculate(data));
            }
        }
        return lastValue;
    }

    protected double calculate(double[] data) {
        double average = 0;
        for (double item : data) {
            average += item;
        }
        return average / data.length;
    }

}
