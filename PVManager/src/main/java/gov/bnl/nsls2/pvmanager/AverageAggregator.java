package gov.bnl.nsls2.pvmanager;

/**
 * Aggregates the values by taking the average.
 * 
 * @author carcassi
 */
public class AverageAggregator extends Aggregator<TypeDouble> {

    public AverageAggregator(Collector collector) {
        super(TypeDouble.class, collector);
    }

    @Override
    protected TypeDouble calculate(double[] data) {
        double average = 0;
        for (double item : data) {
            average += item;
        }
        TypeDouble newValue = PVType.newInstanceOf(getType());
        newValue.setDouble(average / data.length);
        return newValue;
    }

}
