/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

/**
 * The scale to be used to create axis references and rescale values.
 *
 * @author carcassi
 */
public interface ValueScale {
    double scaleValue(double value, double minValue, double maxValue, double newMinValue, double newMaxValue);
    ValueAxis references(Range range, int minRefs, int maxRegs);
}
