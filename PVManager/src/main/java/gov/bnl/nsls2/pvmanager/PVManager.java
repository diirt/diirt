package gov.bnl.nsls2.pvmanager;

import java.util.HashSet;
import java.util.List;
import static gov.bnl.nsls2.pvmanager.PVExpressionLanguage.*;

public class PVManager {

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
            List<MonitorRecipe> monRecipes = aggregatedPVExpression.getMonitorRecipes();
            PVFunction<T> aggregatedFunction = aggregatedPVExpression.getFunction();
            PullNotificator<T> notificator = new PullNotificator<T>(pv, aggregatedFunction);
            Scanner.scan(notificator, scanPeriodMs);
            ConnectionRecipe connRecipe = new ConnectionRecipe();
            connRecipe.channelNames = new HashSet<String>();
            connRecipe.pv = pv;
            for (MonitorRecipe recipe : monRecipes) {
                connRecipe.channelNames.add(recipe.pvName);
            }
            for (MonitorRecipe recipe : monRecipes) {
                MockConnectionManager.instance.monitor(recipe);
            }
            return pv;
        }
    }
}
