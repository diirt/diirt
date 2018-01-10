/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;

/**
 * An optimized instance of a color map, where colors are pre-calculated.
 * <p>
 * TODO: allow choice of number of colors
 *
 * @author sjdallst
 */
class NumberColorMapInstanceOptimized implements NumberColorMapInstance {

    private final int arrayLength = 1000;
    private final int[] colors = new int[arrayLength];
    private final int nanColor;
    private final Range range;
    private final double max, min, total;
    private final String name;

    NumberColorMapInstanceOptimized(NumberColorMapInstance instance, Range range) {
        min = range.getMinimum();
        max = range.getMaximum();
        total = max - min;
        for (int i = 0; i < arrayLength; i++) {
            //account for possible rounding errors on last entry.
            if (i == arrayLength - 1) {
                colors[i] = instance.colorFor(max);
            } else {
                colors[i] = instance.colorFor(min + i * (total / ((double) (arrayLength - 1))));
            }
        }
        this.range = range;
        this.name = instance.toString() + " opt(" + arrayLength + ")";
        this.nanColor = instance.colorFor(Double.NaN);
    }

    // TODO: what is this doing?
    NumberColorMapInstanceOptimized(NumberColorMapInstance instance, Range oldRange, Range newRange) {
        double oldMin = oldRange.getMinimum();
        double oldMax = oldRange.getMaximum();
        double oldTotal = oldMax - oldMin;
        for (int i = 0; i < arrayLength; i++) {
            //account for possible rounding errors on last entry.
            if (i == arrayLength - 1) {
                colors[i] = instance.colorFor(oldMax);
            } else {
                colors[i] = instance.colorFor(oldMin + i * (oldTotal / ((double) (arrayLength - 1))));
            }
        }
        min = newRange.getMinimum();
        max = newRange.getMaximum();
        total = max - min;
        this.range = newRange;
        this.name = instance.toString() + " opt2(" + arrayLength + ")";
        this.nanColor = instance.colorFor(Double.NaN);
    }

    @Override
    public int colorFor(double value) {
        if (Double.isNaN(value)) {
            return nanColor;
        }

        int index = (int) ((value - min) / total * (arrayLength - 1));
        if (index < 0) {
            index = 0;
        } else if (index >= colors.length) {
            index = colors.length - 1;
        }
        return colors[index];
    }

    @Override
    public String toString() {
        return name;
    }

}
