/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.text.NumberFormat;
import org.diirt.util.array.CircularBufferDouble;
import org.diirt.util.array.CollectionNumbers;
import org.diirt.util.array.ListDouble;
import org.diirt.util.text.NumberFormats;

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
    public double invScaleValue(double scaleValue, double actualMinValue, double actualMaxValue, double scaleMinValue, double scaleMaxValue) {
        actualMinValue = Math.log10(actualMinValue);
        actualMaxValue = Math.log10(actualMaxValue);
        double actualRange = actualMaxValue - actualMinValue;
        double scaleRange = scaleMaxValue - scaleMinValue;
        return Math.pow(10.0, actualMinValue + (scaleValue - scaleMinValue) / scaleRange * actualRange);
    }

    @Override
    public ValueAxis references(Range range, int minRefs, int maxRefs) {
        // XXX: Once the range is too short, the log scale should be using
        // a linear scale to determine the references. For example: 100 to 100.1
        // should be divided linearly as the more we zoom in, the more the
        // difference to a linear scale does not exist. The current code will
        // probably not work well here.
        double minValue = range.getMinimum();
        double maxValue = range.getMaximum();
        if (minValue == 0 || maxValue == 0) {
            throw new IllegalArgumentException("The range for a log scale can't include 0");
        }
        if (Math.signum(minValue) != Math.signum(maxValue)) {
            throw new IllegalArgumentException("The range for a log scale must be all positive or all negative");
        }
        int minExp = MathUtil.orderOf(minValue);
        int maxExp = MathUtil.orderOf(maxValue);

        int expRange = maxExp - minExp + 1;
        int maxRefsPerOrder = maxRefs / expRange;
        int currentFactor = quantize(maxRefsPerOrder);

        ListDouble references = generateReferenceValues(range, currentFactor);
        while (references.size() > maxRefs && currentFactor != 1) {
            currentFactor = decreaseFactor(currentFactor);
            references = generateReferenceValues(range, currentFactor);
        }

        // Number of digits required after first number
        int orderOfIncrement = MathUtil.orderOf(currentFactor);
        NumberFormat format;
        boolean useExponentialNotation;
        if ((minExp - orderOfIncrement) < -3 || maxExp > 3) {
            // Would need more than 3 decimal places or more than 3 zeros
            useExponentialNotation = true;
            format = NumberFormats.format(orderOfIncrement);
        } else {
            useExponentialNotation = false;
            format = NumberFormats.format(Math.max(0, orderOfIncrement - minExp));
        }

        String[] labels = new String[references.size()];
        for (int i = 0; i < references.size(); i++) {
            double value = references.getDouble(i);
            if (useExponentialNotation) {
                labels[i] = format(value, format, Integer.toString(MathUtil.orderOf(value)), Math.pow(10, MathUtil.orderOf(value)));
            } else {
                labels[i] = format(value, format, null, 1);
            }
        }

        return new ValueAxis(minValue, maxValue, CollectionNumbers.doubleArrayCopyOf(references), labels);
    }

    static String format(double number, NumberFormat format, String exponent, double normalization) {
        if (exponent != null) {
            return format.format(number/normalization) + "e" + exponent;
        } else {
            return format.format(number/normalization);
        }
    }

    static int decreaseFactor(int factor) {
        if (factor == 1) {
            return 1;
        }

        int order = 1;
        while (factor >= 10) {
            factor /= 10;
            order *= 10;
        }

        if (factor == 1) {
            return order / 2;
        }

        if (factor == 5) {
            return order * 2;
        }

        if (factor == 2) {
            return order;
        }

        throw new IllegalStateException("Logic error: this should be unreachable");
    }

    static int quantize(double value) {
        if (value <= 1) {
            return 1;
        }

        int exp = MathUtil.orderOf(value);
        double order = Math.pow(10, exp);
        double normalizedValue = value / order;

        if (normalizedValue <= 1) {
            normalizedValue = 1;
        } else if (normalizedValue <= 2) {
            normalizedValue = 2;
        } else if (normalizedValue <= 5) {
            normalizedValue = 5;
        } else if (normalizedValue <= 10) {
            normalizedValue = 10;
        } else {
            throw new IllegalStateException("Logic error: this should be unreachable");
        }

        return (int) (normalizedValue * order);
    }

    static ListDouble generateReferenceValues(Range range, int subdivisionFactor) {
        CircularBufferDouble values = new CircularBufferDouble(100000);
        double minValue = range.getMinimum();
        double maxValue = range.getMaximum();
        int minExp = MathUtil.orderOf(minValue);
        int maxExp = MathUtil.orderOf(maxValue);

        int currentExp = minExp;
        while (currentExp <= maxExp) {
            double currentOrder = Math.pow(10, currentExp);
            if (currentOrder <= maxValue && currentOrder >= minValue) {
                values.addDouble(currentOrder);
            }
            for (int i = 0; i < subdivisionFactor; i++) {
                double newValue = (currentOrder * 10 * i) / subdivisionFactor;
                if ((newValue <= maxValue) && (newValue > currentOrder) && (newValue > minValue)) {
                    values.addDouble(newValue);
                }
            }
            currentExp++;
        }

        return values;
    }

}
