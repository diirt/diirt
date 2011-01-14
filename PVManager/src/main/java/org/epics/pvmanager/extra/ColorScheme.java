/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.Color;
import java.util.Random;
import org.epics.pvmanager.data.Display;
import org.epics.pvmanager.data.Util;

/**
 *
 * @author carcassi
 */
public abstract class ColorScheme {
    
    /**
     * Calculate the color for the value according to the ranges and puts it
     * into the colors buffer.
     *
     * @param value the value to color
     * @param ranges the display ranges
     * @return the RGB color
     */
    public abstract int color(double value, Display ranges);

    public static ColorScheme fullRangeGradient(final Color minValueColor, final Color maxValueColor) {
        return new ColorScheme() {
            Random rand = new Random();

            @Override
            public int color(double value, Display ranges) {
                double normalValue = Util.normalize(value, ranges);
                int alpha = 0;
                int red = (int) (minValueColor.getRed() + (maxValueColor.getRed() - minValueColor.getRed()) * normalValue);
                int green = (int) (minValueColor.getGreen() + (maxValueColor.getGreen() - minValueColor.getGreen()) * normalValue);
                int blue = (int) (minValueColor.getBlue() + (maxValueColor.getBlue() - minValueColor.getBlue()) * normalValue);
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }
}
