/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import org.epics.util.array.ListDouble;

/**
 * The scale to be used to create axis references and rescale values.
 *
 * @author carcassi
 */
public interface ValueScale {
    double scaleValue(double value, double minValue, double maxValue, double newMinValue, double newMaxValue);
    ListDouble references(Range range, int minRefs, int maxRegs);
}
