/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class RangeUtil {
    
    /**
     * Range from given min and max.
     * 
     * @param minValue minimum value
     * @param maxValue maximum value
     * @return the range
     */
    public static Range range(final double minValue, final double maxValue) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("minValue should be less then maxValue (" + minValue+ ", " + maxValue + ")");
        }
        return new Range() {

            @Override
            public Number getMinimum() {
                return minValue;
            }

            @Override
            public Number getMaximum() {
                return maxValue;
            }
        };
    }
    
    /**
     * Determines whether the subrange is contained in the range or not.
     * 
     * @param range a range
     * @param subrange a possible subrange
     * @return true if subrange is contained in range
     */
    public static boolean contains(Range range, Range subrange) {
        return range.getMinimum().doubleValue() <= subrange.getMinimum().doubleValue()
                && range.getMaximum().doubleValue() >= subrange.getMaximum().doubleValue();
        
    }
    
    public static double[] createBins(double min, double max, int nBins) {
        double increment = (max - min) / nBins;
        double[] boundary = new double[nBins+1];
        boundary[0] = min;
        for (int i = 1; i < boundary.length; i++) {
            boundary[i] = min + ( (max - min) * i / nBins );
        }
        return boundary;
    }
}
