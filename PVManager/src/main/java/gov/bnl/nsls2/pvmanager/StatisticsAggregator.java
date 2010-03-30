package gov.bnl.nsls2.pvmanager;

import static java.lang.Math.*;

class StatisticsAggregator extends Aggregator<DoubleStatistics> {

    StatisticsAggregator(Collector collector) {
        super(DoubleStatistics.class, collector);
    }

    @Override
    protected DoubleStatistics calculate(double[] data) {
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
        DoubleStatistics newValue = new DoubleStatistics();
        newValue.setStatistics(totalSum / data.length, min, max,
                sqrt(totalSquareSum / data.length - (totalSum * totalSum) / (data.length * data.length)));
        return newValue;
    }

}
