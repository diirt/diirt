package gov.bnl.nsls2.pvmanager;

import static java.lang.Math.*;

public class StatisticsAggregator extends Aggregator<TypeStatistics> {

    private final Collector collector;
    // TODO There may not be a last value!!!
    private TypeStatistics lastValue = new TypeStatistics();

    public StatisticsAggregator(Collector collector) {
        this.collector = collector;
    }

    @Override
    public synchronized TypeStatistics getValue() {
        double[] data = collector.getData();
        if (data.length > 0) {
            TypeDouble newValue = new TypeDouble();
            synchronized (newValue) {
                lastValue = calculate(data);
            }
        }
        return lastValue;
    }

    protected TypeStatistics calculate(double[] data) {
        double totalSum = 0;
        double totalSquareSum = 0;
        double min = 0;
        double max = 0;
        for (double item : data) {
            totalSum += item;
            totalSquareSum += item * item;
            min = min(min, item);
            max = max(min, item);
        }
        TypeStatistics newValue = new TypeStatistics();
        synchronized (newValue) {
            newValue.setStatistics(totalSum / data.length, min, max,
                    sqrt(totalSquareSum / data.length - (totalSum * totalSum) / (data.length * data.length)));
        }
        return newValue;
    }

}
