/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 * A scheme that will associate a RBG color to any data value.
 * This is a transformation from <b>value</b> to <b>color</b>.
 * 
 * @author carcassi
 */
public interface ValueColorScheme {
    
    /**
     * Calculate the color for the value according to the ranges and puts it
     * into the colors buffer.
     *
     * @param value the value to color
     * @return the RGB color
     */
    public int colorFor(double value);
}
