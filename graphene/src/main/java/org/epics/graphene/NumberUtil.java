/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import org.epics.util.array.IteratorDouble;

/**
 *
 * @author carcassi
 */
public class NumberUtil {
    
    /**
     *Returns the ratio of the given value to the total range as a <code>double</code> between 0 and 1.
     * @param value - number between min and max, inclusive.
     * @param min - lowest number in range.
     * @param max - highest number in range.
     * @return <code>double</code> from 0 to 1, inclusive.
     */
    public static double normalize(double value, double min, double max) {
        if(value < min || value > max)
            throw new IllegalArgumentException("value not in range");
        return (value - min) / (max - min);
    }
    
    /**
     *Returns the ratio of the given value to the total range as a <code>double</code> between 0 and 1, multiplied by newWidth.
     * @param value - number between min and max, inclusive.
     * @param min - lowest number in range.
     * @param max - highest number in range.
     * @param newWidth - scalar to multiply the ratio by.
     * @return <code>double</code> from 0 to newWidth, inclusive.
     */
    public static double scale(double value, double min, double max, double newWidth) {
        return (value - min) * newWidth / (max - min);
    }
    
    /**
     * Returns the minimum and the maximum value in the array.
     * 
     * @param array an array
     * @return the minimum and the maximum
     */
    public static int[] minMax(int[] array) {
        if (array.length == 0) {
            return null;
        }
        
        int[] minMax = new int[] {array[0], array[0]};
        for (int i = 1; i < array.length; i++) {
            int value = array[i];
            if (value > minMax[1])
                minMax[1] = value;
            if (value < minMax[0])
                minMax[0] = value;
        }
        
        return minMax;
    }
    
    /**
     * Returns the minimum and the maximum value in the array.
     * 
     * @param array an array
     * @return the minimum and the maximum
     */
    public static double[] minMax(double[] array) {
        return minMax(Iterators.arrayIterator(array));
    }
    
    public static double[] minMax(IteratorDouble iterator) {
        if (!iterator.hasNext()) {
            return null;
        }
        double firstValue = iterator.nextDouble();
        
        double[] minMax = new double[] {firstValue, firstValue};
        while (iterator.hasNext()) {
            double value = iterator.nextDouble();
            if (value > minMax[1])
                minMax[1] = value;
            if (value < minMax[0])
                minMax[0] = value;
        }
        
        return minMax;
    }
}
