package gov.bnl.nsls2.pvmanager;

/**
 * Aggregates the values by taking the average.
 * 
 * @author carcassi
 */
class AverageAggregator extends Aggregator<Double> {

    AverageAggregator(Collector collector) {
        super(Double.class, collector);
    }

    @Override
    protected Double calculate(double[] data) {
        double average = 0;
        for (double item : data) {
            average += item;
        }
        return average / data.length;
    }

}
