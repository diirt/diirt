package gov.bnl.nsls2.pvmanager;

public class SimpleAggregator extends Aggregator {

    private final Collector collector;
    // TODO There may not be a last value!!!
    private double lastValue = 0;

    public SimpleAggregator(Collector collector) {
        this.collector = collector;
    }

    @Override
    public synchronized double getValue() {
        double[] data = collector.getData();
        if (data.length > 0) {
            lastValue = calculate(data);
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
