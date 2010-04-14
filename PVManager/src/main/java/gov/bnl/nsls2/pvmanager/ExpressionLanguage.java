/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.bnl.nsls2.pvmanager.types.DoubleStatistics;

/**
 * Operators to constructs expression of PVs that the {@link PVManager} will
 * be able to monitor.
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    /**
     * A CA channel with the given name of type double.
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static Expression<Double> doublePv(String name) {
        return new Expression<Double>(name, Double.class);
    }

    /**
     * Aggrates the sample at the scan rate and takes the average.
     * @param doublePv the expression to take the average of; can't be null
     * @return an expression representing the average of the expression
     */
    public static AggregatedExpression<Double> averageOf(Expression<Double> doublePv) {
        Collector<Double> collector = new QueueCollector<Double>(doublePv.getFunction());
        return new AggregatedExpression<Double>(doublePv.createMontiorRecipes(collector),
                Double.class, new AverageAggregator(collector), "avg(" + doublePv.getDefaultName() + ")");
    }

    /**
     * Aggreages the sample at the scan rate and calculates statistical information.
     * @param doublePv the expression to calculate the statistics information on; can't be null
     * @return an expression representing the statistical information of the expression
     */
    public static AggregatedExpression<DoubleStatistics> statisticsOf(Expression<Double> doublePv) {
        Collector<Double> collector = new QueueCollector<Double>(doublePv.getFunction());
        return new AggregatedExpression<DoubleStatistics>(doublePv.createMontiorRecipes(collector),
                DoubleStatistics.class, new StatisticsAggregator(collector), "stats(" + doublePv.getDefaultName() + ")");
    }

    /**
     * Tells the PV manager to notify on the Swing Event Dispatch Thread using
     * SwingUtilities.invokeLater().
     * @return an object that posts events on the EDT
     */
    public static ThreadSwitch onSwingEDT() {
        return ThreadSwitch.SWING;
    }

    /**
     * Tells the PV manager to notify on the timer thread.
     * @return an object that runs tasks on the timer thread
     */
    public static ThreadSwitch onTimerThread() {
        return ThreadSwitch.TIMER;
    }

}
