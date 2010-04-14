package gov.bnl.nsls2.pvmanager;

import java.util.HashSet;
import java.util.List;
import static gov.bnl.nsls2.pvmanager.ExpressionLanguage.*;

/**
 * Manages the PV creation and scanning.
 *
 * @author carcassi
 */
public class PVManager {

    public static void useMockData() {
        ConnectionManager.useMockConnectionManager();
    }

    public static void useChannelAccess() {
        ConnectionManager.useCAConnectionManager();
    }

    public static volatile ThreadSwitch defaultOnThread = ExpressionLanguage.onSwingEDT();

    /**
     * Reads the given expression. Will return the average of the values collected
     * at the scan rate.
     *
     * @param pvExpression the expression to read
     * @return a pv manager expression
     */
    public static PVManagerExpression<Double> read(Expression<Double> pvExpression) {
        return new PVManagerExpression<Double>(averageOf(pvExpression));
    }

    /**
     * Reads the given expression.
     *
     * @param pvExpression the expression to read
     * @return a pv manager expression
     */
    public static <T> PVManagerExpression<T> read(AggregatedExpression<T> pvExpression) {
        return new PVManagerExpression<T>(pvExpression);
    }

    /**
     * An expression used to set the final parameters on how the pv expression
     * should be monitored.
     * @param <T> the type of the expression
     */
    public static class PVManagerExpression<T>  {

        private AggregatedExpression<T> aggregatedPVExpression;
        private ThreadSwitch onThread;

        private PVManagerExpression(AggregatedExpression<T> aggregatedPVExpression) {
            this.aggregatedPVExpression = aggregatedPVExpression;
        }

        public PVManagerExpression<T> andNotify(ThreadSwitch onThread) {
            if (this.onThread == null)  {
                this.onThread = onThread;
            } else {
                throw new IllegalStateException("Already set what thread to notify");
            }
            return this;
        }

        /**
         * Sets the rate of scan of the expression and creates the actual {@link PV}
         * object that can be monitored through listeners.
         * @param rate rate in Hz; should be between 1 and 50
         * @return the PV
         */
        public PV<T> atHz(double rate) {
            long scanPeriodMs = (long) (1000.0 * (1.0 / rate));
            PV<T> pv = PV.createPv(aggregatedPVExpression.getDefaultName(), aggregatedPVExpression.getOutputType());
            List<MonitorRecipe> monRecipes = aggregatedPVExpression.getMonitorRecipes();
            Function<T> aggregatedFunction = aggregatedPVExpression.getFunction();
            if (onThread == null) {
                onThread = defaultOnThread;
            }
            PullNotificator<T> notificator = new PullNotificator<T>(pv, aggregatedFunction, onThread);
            Scanner.scan(notificator, scanPeriodMs);
            ConnectionRecipe connRecipe = new ConnectionRecipe();
            connRecipe.channelNames = new HashSet<String>();
            connRecipe.pv = pv;
            for (MonitorRecipe recipe : monRecipes) {
                connRecipe.channelNames.add(recipe.pvName);
            }
            //ConnectionManager.getInstance().connect(connRecipe);
            for (MonitorRecipe recipe : monRecipes) {
                ConnectionManager.getInstance().monitor(recipe);
            }
            return pv;
        }
    }
}
