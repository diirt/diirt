/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import static org.epics.graphene.ValueAxis.orderOfMagnitude;
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
    
}
