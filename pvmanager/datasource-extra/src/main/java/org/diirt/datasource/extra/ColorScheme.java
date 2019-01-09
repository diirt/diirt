/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.extra;

import java.awt.Color;
import java.util.Random;
import org.epics.vtype.Display;
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

    /**
     * A simple color scheme that uses only one gradient for the whole
     * display range.
     *
     * @param minValueColor color for the minimum value in the display range
     * @param maxValueColor color for the maximum value in the display range
     * @return a new color scheme
     */
    public static ColorScheme singleRangeGradient(final Color minValueColor, final Color maxValueColor) {
        return singleRangeGradient(minValueColor, maxValueColor, Color.BLACK);
    }

    public static ColorScheme singleRangeGradient(final Color minValueColor, final Color maxValueColor, final Color nanColor) {
        return new ColorScheme() {
            Random rand = new Random();

            @Override
            public int color(double value, Display ranges) {
                if (Double.isNaN(value))
                    return nanColor.getRGB();

                double normalValue = ranges.getDisplayRange().normalize(value);
                normalValue = Math.min(normalValue, 1.0);
                normalValue = Math.max(normalValue, 0.0);
                int alpha = 255;
                int red = (int) (minValueColor.getRed() + (maxValueColor.getRed() - minValueColor.getRed()) * normalValue);
                int green = (int) (minValueColor.getGreen() + (maxValueColor.getGreen() - minValueColor.getGreen()) * normalValue);
                int blue = (int) (minValueColor.getBlue() + (maxValueColor.getBlue() - minValueColor.getBlue()) * normalValue);
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }

    public static ColorScheme multipleRangeGradient(final Color lowerDisplayColor,
            final Color lowerAlarmColor, final Color lowerWarningColor,
            final Color upperWarningColor, final Color upperAlarmColor,
            final Color upperDisplayColor) {
        return new ColorScheme() {
            Random rand = new Random();

            @Override
            public int color(double value, Display ranges) {
                if (Double.isNaN(value))
                    return 0;

                double normalValue = 0.0;
                Color minValueColor = null;
                Color maxValueColor = null;
                // Determine in which range the value is.
                // The equals are put so that if the value is at the limit,
                // the range closer to the center wins
                if (value < ranges.getDisplayRange().getMinimum()) {
                    return lowerDisplayColor.getRGB();
                } else if (value < ranges.getAlarmRange().getMinimum()) {
                    normalValue = normalize(value, ranges.getDisplayRange().getMinimum(), ranges.getAlarmRange().getMinimum());
                    minValueColor = lowerDisplayColor;
                    maxValueColor = lowerAlarmColor;
                } else if (value < ranges.getWarningRange().getMinimum()) {
                    normalValue = normalize(value, ranges.getAlarmRange().getMinimum(), ranges.getWarningRange().getMinimum());
                    minValueColor = lowerAlarmColor;
                    maxValueColor = lowerWarningColor;
                } else if (value <= ranges.getWarningRange().getMaximum()) {
                    normalValue = normalize(value, ranges.getWarningRange().getMinimum(), ranges.getWarningRange().getMaximum());
                    minValueColor = lowerWarningColor;
                    maxValueColor = upperWarningColor;
                } else if (value <= ranges.getAlarmRange().getMaximum()) {
                    normalValue = normalize(value, ranges.getWarningRange().getMaximum(), ranges.getAlarmRange().getMaximum());
                    minValueColor = upperWarningColor;
                    maxValueColor = upperAlarmColor;
                } else {
                    return upperDisplayColor.getRGB();
                }

                int alpha = 255;
                int red = (int) (minValueColor.getRed() + (maxValueColor.getRed() - minValueColor.getRed()) * normalValue);
                int green = (int) (minValueColor.getGreen() + (maxValueColor.getGreen() - minValueColor.getGreen()) * normalValue);
                int blue = (int) (minValueColor.getBlue() + (maxValueColor.getBlue() - minValueColor.getBlue()) * normalValue);
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }

    /**
     * Normalizes the given value according to the given range;
     *
     * @param value a value
     * @param lowValue the lowest value in the range
     * @param highValue the highest value in the range
     * @return the normalized value, or null if any value is null
     */
    public static Double normalize(Number value, Number lowValue, Number highValue) {
        if (value == null || lowValue == null || highValue == null) {
            return null;
        }

        return (value.doubleValue() - lowValue.doubleValue()) / (highValue.doubleValue() - lowValue.doubleValue());
    }
}
