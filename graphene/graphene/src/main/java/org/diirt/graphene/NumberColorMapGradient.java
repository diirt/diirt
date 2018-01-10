/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import javafx.scene.paint.Color;
import java.util.List;
import org.diirt.util.array.ListNumber;

/**
 * A numeric color map defined by interpolated colors (gradients).
 *
 * @author sjdallst
 */
class NumberColorMapGradient implements NumberColorMap {

    private final List<Color> colors;
    private final ListNumber positions;
    private final boolean relative;
    private final Color nanColor;
    private final String name;


    /**
     * Creates a new color map.
     * <p>
     *
     * @param colors
     */
    public NumberColorMapGradient(List<Color> colors, ListNumber positions,
            boolean relative, Color nanColor, String name) {

        if(colors.size()!=positions.size())
            throw new IllegalArgumentException("Number of colors and number of positions don't match");
        if(nanColor==null)
            throw new IllegalArgumentException("NanColor should not be null");
        if(name==null || name.isEmpty() )
            throw new IllegalArgumentException("Name should not be empty or null");

        for (int i =0; i<positions.size(); ++i)   {
            //if using a relative scale, value should be bewteen 0.0 and 1.0
            if(relative && !(positions.getDouble(i) >=0 && positions.getDouble(i) <=1)){
                throw new RuntimeException("Position for relative scale should be between 0.0 and 1.0");
            }
            //check position values are in strictly increasing order
            if (i!=0 && positions.getDouble(i-1) >= positions.getDouble(i)){
                throw new RuntimeException("Position should be strictly increasing");
            }
        }

        this.colors = colors;
        this.relative = relative;
        this.name = name;
        this.nanColor = nanColor;
        this.positions = positions;
    }

    public List<Color> getColors() {
        return colors;
    }

    @Override
    public NumberColorMapInstance createInstance(Range range) {

        if(range==null) throw new NullPointerException("Range cannot be null");

        return new ValueColorSchemeInstanceGradient(range);
    }

    class ValueColorSchemeInstanceGradient implements NumberColorMapInstance {

        protected int nanColorInt;
        protected Range range;
        private final double fullRange;
        public ValueColorSchemeInstanceGradient(Range range) {
            this.range = range;
            fullRange = range.getMaximum() - range.getMinimum();
            int red = (int)(255*nanColor.getRed());
            int green = (int)(255*nanColor.getGreen());
            int blue = (int)(255*nanColor.getBlue());
            this.nanColorInt = (255 << 24) | (red<< 16) | (green<< 8) | blue;
        }

        @Override
        public int colorFor(double value) {
            if (Double.isNaN(value)) {
                return nanColorInt;
            }

            if (relative) {
                // Relative scale
                return relativeColorFor(value);
            } else {
                // Absolute scale
                return absoluteColorFor(value);
            }

        }

        private int relativeColorFor(double value) {
            int alpha = 0, red = 0, green = 0, blue = 0;

            if (value > range.getMaximum()) {

                alpha = 255;
                red = (int) (255 * (colors.get(colors.size() - 1).getRed()));
                green = (int) (255 * (colors.get(colors.size() - 1).getGreen()));
                blue = (int) (255 * (colors.get(colors.size() - 1).getBlue()));
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
            if (value < range.getMinimum()) {
                alpha = 255;
                red = (int) (255 * (colors.get(0).getRed()));
                green = (int) (255 * (colors.get(0).getGreen()));
                blue = (int) (255 * (colors.get(0).getBlue()));
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }



            if (fullRange > 0) {
                for (int i = 0; i < positions.size() - 1; i++) {
                    if (range.getMinimum() + positions.getDouble(i) * fullRange <= value && value <= range.getMinimum() + positions.getDouble(i + 1) * fullRange) {
                        double normalValue = MathUtil.normalize(value, range.getMinimum() + positions.getDouble(i) * fullRange, range.getMinimum() + positions.getDouble(i + 1) * fullRange);
                        normalValue = Math.min(normalValue, 1.0);
                        normalValue = Math.max(normalValue, 0.0);
                        alpha = 255;
                        red = (int) (255*(colors.get(i).getRed() + (colors.get(i + 1).getRed() - colors.get(i).getRed()) * normalValue));
                        green = (int)( 255*(colors.get(i).getGreen() + (colors.get(i + 1).getGreen() - colors.get(i).getGreen()) * normalValue));
                        blue = (int) (255*(colors.get(i).getBlue() + (colors.get(i + 1).getBlue() - colors.get(i).getBlue()) * normalValue));
                    }
                }
            } else {
                for (int i = 0; i < positions.size() - 1; i++) {
                    if (positions.getDouble(i) <= .5 && .5 <= positions.getDouble(i + 1)) {
                        double normalValue = 0;
                        normalValue = Math.min(normalValue, 1.0);
                        normalValue = Math.max(normalValue, 0.0);
                        alpha = 255;
                        red = (int)( 255*(colors.get(i).getRed() + (colors.get(i + 1).getRed() - colors.get(i).getRed()) * normalValue));
                        green = (int)(255* (colors.get(i).getGreen() + (colors.get(i + 1).getGreen() - colors.get(i).getGreen()) * normalValue));
                        blue = (int) (255*(colors.get(i).getBlue() + (colors.get(i + 1).getBlue() - colors.get(i).getBlue()) * normalValue));
                    }
                }
            }

            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        private int absoluteColorFor(double value){

            int alpha = 0, red = 0, green = 0, blue = 0;

            if (value > positions.getDouble(positions.size() - 1)) {
                alpha = 255;
                red = (int) (255 * (colors.get(colors.size() - 1).getRed()));
                green = (int) (255 * (colors.get(colors.size() - 1).getGreen()));
                blue = (int) (255 * (colors.get(colors.size() - 1).getBlue()));
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }

            if (value < positions.getDouble(0)) {
                alpha = 255;
                red = (int) (255 * (colors.get(0).getRed()));
                green = (int) (255 * (colors.get(0).getGreen()));
                blue = (int) (255 * (colors.get(0).getBlue()));
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }

            for (int i = 0; i < positions.size() - 1; ++i) {
                if (positions.getDouble(i) <= value && value <= positions.getDouble(i + 1)) {
                    double normalValue = MathUtil.normalize(value, positions.getDouble(i), positions.getDouble(i + 1));

                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (255*(colors.get(i).getRed() + (colors.get(i + 1).getRed() - colors.get(i).getRed()) * normalValue));
                    green = (int)(255* (colors.get(i).getGreen() + (colors.get(i + 1).getGreen() - colors.get(i).getGreen()) * normalValue));
                    blue = (int) (255*(colors.get(i).getBlue() + (colors.get(i + 1).getBlue() - colors.get(i).getBlue()) * normalValue));
                }
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
