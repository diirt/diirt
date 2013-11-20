/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public class IntensityGraph2DRendererUpdate extends Graph2DRendererUpdate<IntensityGraph2DRendererUpdate> {

    private InterpolationScheme interpolation;
    
    private Double startX;
    private Double endX;
    private Double startY;
    private Double endY;
    
    private Boolean rangeFromDataset,
                    drawLegend;
    
    private ColorScheme valueColorScheme;
    
    public IntensityGraph2DRendererUpdate startX(double startX) {
        this.startX = startX;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate startY(double startY) {
        this.startY = startY;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate endX(double endX) {
        this.endX = endX;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate endY(double endY) {
        this.endY = endY;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate rangeFromDataset(boolean rangeFromDataset) {
        this.rangeFromDataset = rangeFromDataset;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate drawLegend(boolean drawLegend) {
        this.drawLegend = drawLegend;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate interpolation(InterpolationScheme scheme) {
        this.interpolation = scheme;
        return self();
    }
    
    public IntensityGraph2DRendererUpdate valueColorScheme(ColorScheme scheme) {
        this.valueColorScheme = scheme;
        return self();
    }
    
    public InterpolationScheme getInterpolation() {
        return interpolation;
    }

    public Double getStartX() {
        return startX;
    }

    public Double getStartY() {
        return startY;
    }

    public Double getEndX() {
        return endX;
    }

    public Double getEndY() {
        return endY;
    }

    public Boolean isRangeFromDataset() {
        return rangeFromDataset;
    }
    
    public Boolean getDrawLegend() {
        return drawLegend;
    }
    
    public ColorScheme getValueColorScheme() {
        return valueColorScheme;
    }
}
