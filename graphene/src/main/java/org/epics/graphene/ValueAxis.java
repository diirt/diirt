/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.text.DecimalFormat;

/**
 *
 * @author carcassi
 */
public class ValueAxis {
    
    private double minValue;
    private double maxValue;
    private double[] tickValues;
    private String[] tickStrings;

    public ValueAxis(double minValue, double maxValue, double[] tickValues, String[] tickStrings) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.tickValues = tickValues;
        this.tickStrings = tickStrings;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double[] getTickValues() {
        return tickValues;
    }

    public String[] getTickLabels() {
        return tickStrings;
    }

    public static ValueAxis createAutoAxis(double minValue, double maxValue, int maxTicks) {
        return createAutoAxis(minValue, maxValue, maxTicks, Double.MIN_VALUE);
    }
    
    private static final DecimalFormat format = new DecimalFormat("0.###");
    
    public static ValueAxis createAutoAxis(double minValue, double maxValue, int maxTicks, double minIncrement) {
        double[] ticks = RangeUtil.ticksForRange(minValue, maxValue, maxTicks);
        String[] labels = new String[ticks.length];
        for (int i = 0; i < ticks.length; i++) {
            double value = ticks[i];
            labels[i] = format.format(value);
        }
        return new ValueAxis(minValue, maxValue, ticks, labels);
    }
    
}
