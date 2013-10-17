/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;

/**
 *
 * @author Aaron
 */
public class SparklineGraph2DRendererUpdate extends Graph2DRendererUpdate<SparklineGraph2DRendererUpdate>{
    
    private Color minValueColor, maxValueColor, lastValueColor;
    private Integer circleDiameter;   
    private Boolean drawCircles;
    private InterpolationScheme interpolation;
    
    public SparklineGraph2DRendererUpdate minValueColor(Color color){
        minValueColor = color;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate maxValueColor(Color color){
        maxValueColor = color;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate lastValueColor(Color color){
        lastValueColor = color;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate circleDiameter(int diameter){
        circleDiameter = diameter;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate drawCircles(boolean decision){
        this.drawCircles = decision;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate interpolation(InterpolationScheme scheme) {
        if (scheme == null) {
            throw new NullPointerException("Interpolation scheme chan't be null");
        }
        if (!LineTimeGraph2DRenderer.supportedInterpolationScheme.contains(scheme)) {
            throw new IllegalArgumentException("Interpolation " + scheme + " is not supported");
        }
        this.interpolation = scheme;
        return this;
    }    
    
    /**
     * Gets the color of the circle drawn for a minimum value in the data set.
     * @return Color of the circle at the minimum
     */
    public Color getMinValueColor(){
        return minValueColor;
    }
    
    /**
     * Gets the color of the circle drawn for a maximum value in the data set.
     * @return Color of the circle at the maximum
     */
    public Color getMaxValueColor(){
        return maxValueColor;
    }
    
    /**
     * Gets the color of the circle drawn for the last value in the data set.
     * @return Color of the circle at the last index
     */
    public Color getLastValueColor(){
        return lastValueColor;
    }
    
    /**
     * Gets the size of the circle
     * @return Size of the diameter of the circles drawn
     */
    public Integer getCircleDiameter(){
        return circleDiameter;
    }
    
    /**
     * Gets the parameter of whether circles are drawn.
     * @return Parameter of whether to draw circles
     */
    public Boolean getDrawCircles(){
        return drawCircles;
    }
    
    /**
     * Gets the interpolation Scheme
     * @return InterpolationScheme of graph
     */
    public InterpolationScheme getInterpolation() {
        return interpolation;
    }    
    
}
