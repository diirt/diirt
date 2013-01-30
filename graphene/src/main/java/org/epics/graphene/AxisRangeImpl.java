/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
class AxisRangeImpl implements AxisRange {
    
    private double min;
    private double max;

    public AxisRangeImpl(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Range axisRange(Range range) {
        return RangeUtil.range(min, max);
    }
    
}
