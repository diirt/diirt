/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import static org.epics.graphene.ValueAxis.orderOfMagnitude;
import org.epics.util.array.CircularBufferDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.text.NumberFormats;

/**
 *
 * @author carcassi
 */
final class LogValueScale implements ValueScale {

    @Override
    public double scaleValue(double value, double minValue, double maxValue, double newMinValue, double newMaxValue) {
        value = Math.log10(value);
        minValue = Math.log10(minValue);
        maxValue = Math.log10(maxValue);
        double oldRange = maxValue - minValue;
        double newRange = newMaxValue - newMinValue;
        return newMinValue + (value - minValue) / oldRange * newRange;
    }

    @Override
    public ValueAxis references(Range range, int minRefs, int maxRefs) {
        return null;
    }
    
    static ListDouble generateReferenceValues(Range range, int subdivisionFactor) {
        CircularBufferDouble values = new CircularBufferDouble(100000);
        double minValue = range.getMinimum().doubleValue();
        double maxValue = range.getMaximum().doubleValue();
        int minExp = Math.getExponent(minValue);
        int maxExp = Math.getExponent(maxValue);
        
        int currentExp = minExp;
        while (currentExp <= maxExp) {
            double currentOrder = Math.pow(10, currentExp);
            if (currentOrder <= maxValue) {
                values.addDouble(currentOrder);
            }
            for (int i = 0; i < subdivisionFactor; i++) {
                double newValue = (currentOrder * 10 * i) / subdivisionFactor;
                if ((newValue <= maxValue) && (newValue > currentOrder)) {
                    values.addDouble(newValue);
                }
            }
            currentExp++;
        }
        
        return values;
    }
    
}
