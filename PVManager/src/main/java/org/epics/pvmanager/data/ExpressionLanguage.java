/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import org.epics.pvmanager.AggregatedExpression;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.Expression;
import org.epics.pvmanager.QueueCollector;

/**
 * PVManager expression language support for EPICS types.
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    static {
        // Add support for Epics types.
        EpicsTypeSupport.install();
    }

    /**
     * A channel with the given name of type VDouble.
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static Expression<VDouble> vDouble(String name) {
        return new Expression<VDouble>(name, VDouble.class);
    }

    /**
     * Aggregates the sample at the scan rate and calculates statistical information.
     * @param doublePv the expression to calculate the statistics information on; can't be null
     * @return an expression representing the statistical information of the expression
     */
    public static AggregatedExpression<VStatistics> statisticsOf(Expression<VDouble> doublePv) {
        Collector<VDouble> collector = new QueueCollector<VDouble>(doublePv.getFunction());
        return new AggregatedExpression<VStatistics>(doublePv.createMontiorRecipes(collector),
                new StatisticsDoubleAggregator(collector), "stats(" + doublePv.getDefaultName() + ")");
    }

}
