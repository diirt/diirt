/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.stats;

/**
 * A range of numeric values.
 * <p>
 * For the purpose of range calculation, NaNs should be skipped. The only case
 * where NaNs are allowed is for the UNDEFINED range.
 * <p>
 * The minimum and maximum are simply double values.
 *
 * @author carcassi
 */
public final class Range {
    
    private static final Range UNDEFINED = new Range(Double.NaN, Double.NaN, false);
    
    private final double min;
    private final double max;
    private final boolean reversed;

    Range(double min, double max, boolean reversed) {
        this.min = min;
        this.max = max;
        this.reversed = reversed;
    }
    
    /**
     * The minimum value.
     * 
     * @return a value
     */
    public double getMinimum() {
        return min;
    }
    
    /**
     * The maximum value.
     * 
     * @return a value
     */
    public double getMaximum() {
        return max;
    }

    /**
     * Whether the range goes from min to max or from max to min.
     * 
     * @return true if range should be traversed from max to min
     */
    public boolean isReversed() {
        return reversed;
    }
    
    /**
     * An undefined range.
     * 
     * @return the undefined range
     */
    public static Range undefined() {
        return UNDEFINED;
    }
    
    /**
     * Range from given min and max. If max is greater than min, a reversed
     * range is returned.
     * 
     * @param minValue minimum value
     * @param maxValue maximum value
     * @return the range
     */
    public static Range create(final double minValue, final double maxValue) {
        if (minValue > maxValue) {
            return new Range(maxValue, minValue, true);
        }
        return new Range(minValue, maxValue, false);
    }
    
}
