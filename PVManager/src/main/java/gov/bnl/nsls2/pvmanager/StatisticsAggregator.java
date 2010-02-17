package gov.bnl.nsls2.pvmanager;

import static java.lang.Math.*;

public class StatisticsAggregator extends Aggregator<TypeStatistics> {

    public StatisticsAggregator(Collector collector) {
        super(TypeStatistics.class, collector);
    }

    @Override
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
        newValue.setStatistics(totalSum / data.length, min, max,
                sqrt(totalSquareSum / data.length - (totalSum * totalSum) / (data.length * data.length)));
        return newValue;
    }

}
