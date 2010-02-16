package gov.bnl.nsls2.pvmanager;

import static gov.bnl.nsls2.pvmanager.PVExpressionLanguage.*;

public class PVManager {
    // Pass the PV type wanted - since the returned PV is independent
    // from the one created by the connection manager.

    public static PV<TypeDouble> createPV(String name, int updatePerSec) {

        return null;
    }

    public static PV<TypeDouble> createPVavg(String name, int updatePerSec) {
        return null;
    }

    public static PV<TypeDouble> readPV(String name, long scanPeriodMs) {
        PV<TypeDouble> pv = PV.createPv(TypeDouble.class);
        ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(TypeDouble.class);
        Collector collector = new Collector(cache);
        AverageAggregator aggregator = new AverageAggregator(collector);
        PullNotificator<TypeDouble> notificator = new PullNotificator<TypeDouble>(pv, aggregator);
        Scanner.scan(notificator, scanPeriodMs);
        MonitorRecipe<TypeDouble> connRecipe = new MonitorRecipe<TypeDouble>();
        connRecipe.cache = cache;
        connRecipe.pvName = name;
        connRecipe.collector = collector;
        MockConnectionManager.instance.monitor(connRecipe);
        return pv;
    }

    public static PV<TypeDouble> read(PVExpression<TypeDouble> pvExpression, long scanPeriodMs) {
        PV<TypeDouble> pv = PV.createPv(TypeDouble.class);
        AggregatedPVExpression<TypeDouble> aggreg = averageOf(pvExpression);
        @SuppressWarnings("unchecked") MonitorRecipe<TypeDouble> connRecipe = aggreg.getRecipe();
        AverageAggregator aggregator = new AverageAggregator(connRecipe.collector);
        PullNotificator<TypeDouble> notificator = new PullNotificator<TypeDouble>(pv, aggregator);
        Scanner.scan(notificator, scanPeriodMs);
        MockConnectionManager.instance.monitor(connRecipe);
        return pv;
    }

    public static PVManagerExpression<TypeDouble> read(PVExpression<TypeDouble> pvExpression) {
        return new PVManagerExpression<TypeDouble>(averageOf(pvExpression));
    }

    public static class PVManagerExpression<T extends PVType<T>>  {

        private AggregatedPVExpression<T> aggregatedPVExpression;

        private PVManagerExpression(AggregatedPVExpression<T> aggregatedPVExpression) {
            this.aggregatedPVExpression = aggregatedPVExpression;
        }

        public PV<T> atHz(double rate) {
            long scanPeriodMs = (long) (1000.0 * (1.0 / rate));
            PV<T> pv = PV.createPv(aggregatedPVExpression.getOutputType());
            @SuppressWarnings("unchecked") MonitorRecipe connRecipe = aggregatedPVExpression.getRecipe();
            Aggregator<T> aggregator = aggregatedPVExpression.getAggregator();
            PullNotificator<T> notificator = new PullNotificator<T>(pv, aggregator);
            Scanner.scan(notificator, scanPeriodMs);
            MockConnectionManager.instance.monitor(connRecipe);
            return pv;
        }
    }
}
