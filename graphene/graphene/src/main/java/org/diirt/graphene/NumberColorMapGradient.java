/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.diirt.util.array.ListDouble;

/**
 * A numeric color map defined by interpolated colors (gradients).
 *
 * @author sjdallst
 */
class NumberColorMapGradient implements NumberColorMap {

    private final List<Color> colors; 
    private final List<Double> positions; 
    private boolean relative; 
    Color nanColor,belowRange,overRange; 
    private final String name;

    /**
     * Creates a new color map.
     * <p>
     * TODO: use List, separate NaN color, allow for relative percentage offsets
     * 
     * @param colors 
     */
    public NumberColorMapGradient(List<Color> colors,List<Double> positions,boolean relative,Color nanColor,Color belowRange
            ,Color overRange, String name) {
        this.colors = colors;
        this.relative=relative; 
        this.name = name;
        this.nanColor=nanColor; 
        this.belowRange=belowRange; 
        this.overRange=overRange; 
        this.positions=positions;
    }

    @Override
    public NumberColorMapInstance createInstance(Range range) {
        return new ValueColorSchemeInstanceGradient(colors, range,nanColor,belowRange,overRange,relative);
    }

    class ValueColorSchemeInstanceGradient implements NumberColorMapInstance {

        protected List<Color> colors;
        protected List<Double> positions; 
        protected int nanColor,belowRange,overRange;
        protected Range range;
        private boolean relative; 
        public ValueColorSchemeInstanceGradient(List<Color> colors, Range range,Color nanColor,Color belowRange
                ,Color overRange,boolean relative) {
            this.range = range;
            this.colors = colors;
            this.nanColor =nanColor.getRGB();
             this.belowRange=belowRange.getRGB(); 
            this.overRange=overRange.getRGB(); 
            this.relative=relative; 
        }

        @Override
        public int colorFor(double value) {
            if (Double.isNaN(value)) {
                return nanColor;
            }
            if (range == null) {
                throw new NullPointerException("range can not be null.");
            }
            double fullRange = range.getMaximum() - range.getMinimum();
            int alpha = 0, red = 0, green = 0, blue = 0;
            if(relative){
                if (fullRange > 0) {
                    for (int i = 0; i < positions.size() - 1; i++) {
                        if (range.getMinimum() + positions.get(i) * fullRange <= value && value <= range.getMinimum() + positions.get(i + 1) * fullRange) {
                            double normalValue = MathUtil.normalize(value, range.getMinimum() + positions.get(i) * fullRange, range.getMinimum() + positions.get(i + 1) * fullRange);
                            normalValue = Math.min(normalValue, 1.0);
                            normalValue = Math.max(normalValue, 0.0);
                            alpha = 255;
                            red = (int) (colors.get(i).getRed() + (colors.get(i+1).getRed() - colors.get(i).getRed()) * normalValue);
                            green = (int) (colors.get(i).getGreen() + (colors.get(i+1).getGreen() - colors.get(i).getGreen()) * normalValue);
                            blue = (int) (colors.get(i).getBlue() + (colors.get(i+1).getBlue() - colors.get(i).getBlue()) * normalValue);
                        }
                    }
                } 
            }
            /*else {
                for (int i = 0; i < percentages.size() - 1; i++) {
                    if (percentages.get(i) <= .5 && .5 <= percentages.get(i + 1)) {
                        double normalValue = 0;
                        normalValue = Math.min(normalValue, 1.0);
                        normalValue = Math.max(normalValue, 0.0);
                        alpha = 255;
                        red = (int) (colors[i].getRed() + (colors[i + 1].getRed() - colors[i].getRed()) * normalValue);
                        green = (int) (colors[i].getGreen() + (colors[i + 1].getGreen() - colors[i].getGreen()) * normalValue);
                        blue = (int) (colors[i].getBlue() + (colors[i + 1].getBlue() - colors[i].getBlue()) * normalValue);
                    }
                }
            }
            */
            if (value > range.getMaximum()) {
                return overRange; 
            }
            if (value < range.getMinimum()) {
                return belowRange; 
            }
            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        @Override
        public String toString() {
            return name + " " + range;
        }

    }
    
 
    @Override
    public String toString() {
        return name;
    }
    
}
