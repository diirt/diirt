/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.epics.pvmanager.DesiredRateExpression;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.SourceRateExpression;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.QueueCollector;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimedCacheCollector;

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
     *
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static SourceRateExpression<VDouble> vDouble(String name) {
        return new SourceRateExpression<VDouble>(name, VDouble.class);
    }

    /**
     * A list of channels with the given names, all of type VDouble.
     *
     * @param names the channel names; can't be null
     * @return a list of expressions representing the channels
     */
    public static List<SourceRateExpression<VDouble>> vDoubles(List<String> names) {
        List<SourceRateExpression<VDouble>> expressions = new ArrayList<SourceRateExpression<VDouble>>();
        for (String name : names) {
            expressions.add(vDouble(name));
        }
        return expressions;
    }

    /**
     * Aggregates the sample at the scan rate and takes the average.
     * @param doublePv the expression to take the average of; can't be null
     * @return an expression representing the average of the expression
     */
    public static DesiredRateExpression<VDouble> averageOf(SourceRateExpression<VDouble> doublePv) {
        Collector<VDouble> collector = new QueueCollector<VDouble>(doublePv.getFunction());
        return new DesiredRateExpression<VDouble>(doublePv.createMontiorRecipes(collector),
                new AverageAggregator(collector), "avg(" + doublePv.getDefaultName() + ")");
    }

    /**
     * Aggregates the sample at the scan rate and calculates statistical information.
     *
     * @param doublePv the expression to calculate the statistics information on; can't be null
     * @return an expression representing the statistical information of the expression
     */
    public static DesiredRateExpression<VStatistics> statisticsOf(SourceRateExpression<VDouble> doublePv) {
        Collector<VDouble> collector = new QueueCollector<VDouble>(doublePv.getFunction());
        return new DesiredRateExpression<VStatistics>(doublePv.createMontiorRecipes(collector),
                new StatisticsDoubleAggregator(collector), "stats(" + doublePv.getDefaultName() + ")");
    }

    /**
     * Applies {@link #statisticsOf(org.epics.pvmanager.SourceRateExpression) to all
     * arguments.
     *
     * @param doubleExpressions a list of double expressions
     * @return a list of statistical expressions
     */
    public static List<DesiredRateExpression<VStatistics>> statisticsOf(List<SourceRateExpression<VDouble>> doubleExpressions) {
        List<DesiredRateExpression<VStatistics>> expressions = new ArrayList<DesiredRateExpression<VStatistics>>();
        for (SourceRateExpression<VDouble> doubleExpression : doubleExpressions) {
            expressions.add(statisticsOf(doubleExpression));
        }
        return expressions;
    }

    /**
     * A synchronized array from the given expression.
     *
     * @param tolerance maximum time difference between samples
     * @param expressions the expressions from which to reconstruct the array
     * @return an expression for the array
     */
    public static DesiredRateExpression<VMultiDouble>
            synchronizedArrayOf(TimeDuration tolerance, List<SourceRateExpression<VDouble>> expressions) {
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<VDouble>> collectors = new ArrayList<TimedCacheCollector<VDouble>>();
        DataRecipe recipe = new DataRecipe();
        for (SourceRateExpression<VDouble> expression : expressions) {
            TimedCacheCollector<VDouble> collector =
                    new TimedCacheCollector<VDouble>(expression.getFunction(), tolerance.multiplyBy(10));
            collectors.add(collector);
            recipe = recipe.includeRecipe(expression.createMontiorRecipes(collector));
            names.add(expression.getDefaultName());
        }
        SynchronizedVDoubleAggregator aggregator =
                new SynchronizedVDoubleAggregator(names, collectors, tolerance);
        return new DesiredRateExpression<VMultiDouble>(recipe,
                aggregator, "syncArray");
    }

    /**
     * A synchronized array from the given expression.
     *
     * @param tolerance maximum time difference between samples in the
     * reconstructed array
     * @param cacheDepth maximum time difference between samples in the caches
     * used to reconstruct the array
     * @param expressions the expressions from which to reconstruct the array
     * @return an expression for the array
     */
    public static DesiredRateExpression<VMultiDouble>
            synchronizedArrayOf(TimeDuration tolerance, TimeDuration cacheDepth, List<SourceRateExpression<VDouble>> expressions) {
        if (cacheDepth.equals(TimeDuration.ms(0)))
            throw new IllegalArgumentException("Distance between samples must be non-zero");
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<VDouble>> collectors = new ArrayList<TimedCacheCollector<VDouble>>();
        DataRecipe recipe = new DataRecipe();
        for (SourceRateExpression<VDouble> expression : expressions) {
            TimedCacheCollector<VDouble> collector =
                    new TimedCacheCollector<VDouble>(expression.getFunction(), cacheDepth);
            collectors.add(collector);
            recipe = recipe.includeRecipe(expression.createMontiorRecipes(collector));
            names.add(expression.getDefaultName());
        }
        SynchronizedVDoubleAggregator aggregator =
                new SynchronizedVDoubleAggregator(names, collectors, tolerance);
        return new DesiredRateExpression<VMultiDouble>(recipe,
                aggregator, "syncArray");
    }

}
