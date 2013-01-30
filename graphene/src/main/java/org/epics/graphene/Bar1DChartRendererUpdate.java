/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class Bar1DChartRendererUpdate {

    private Integer imageHeight;
    private Integer imageWidth;
    private AxisRange xAxisRange;
    private AxisRange yAxisRange;
    
    public Bar1DChartRendererUpdate imageHeight(int height) {
        this.imageHeight = height;
        return this;
    }
    
    public Bar1DChartRendererUpdate imageWidth(int width) {
        this.imageWidth = width;
        return this;
    }
    
    public Bar1DChartRendererUpdate xAxisRange(AxisRange xAxisRange) {
        this.xAxisRange = xAxisRange;
        return this;
    }
    
    public Bar1DChartRendererUpdate yAxisRange(AxisRange yAxisRange) {
        this.yAxisRange = yAxisRange;
        return this;
    }
    
    public Integer getImageHeight() {
        return imageHeight;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public AxisRange getXAxisRange() {
        return xAxisRange;
    }

    public AxisRange getYAxisRange() {
        return yAxisRange;
    }
    
    
    
}
