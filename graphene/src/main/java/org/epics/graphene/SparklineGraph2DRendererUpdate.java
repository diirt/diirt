/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;

/**
 *
 * @author asbarber
 * @author jkfeng
 * @author sjdallst
 */
public class SparklineGraph2DRendererUpdate extends Graph2DRendererUpdate<SparklineGraph2DRendererUpdate>{
    //Parameters that can be updated
    private Color   minValueColor, maxValueColor, lastValueColor;               //Circle colors
    private Integer circleDiameter;                                             //Circle size
    private Boolean drawCircles;                                                //Option to not draw circles
    private InterpolationScheme interpolation;                                  //Data interpolation
    
    /**
     * Sets the parameter of the color of the minimum value circle.
     * @param color Color for the minimum value circle
     * @return Self, with modified color for the minimum value circle
     */    
    public SparklineGraph2DRendererUpdate minValueColor(Color color){
        minValueColor = color;
        return self();
    }
    
    /**
     * Sets the parameter of the color of the maximum value circle.
     * @param color Color for the maximum value circle
     * @return Self, with modified color for the maximum value circle
     */    
    public SparklineGraph2DRendererUpdate maxValueColor(Color color){
        maxValueColor = color;
        return self();
    }
    
    /**
     * Sets the parameter of the color of the last value circle.
     * @param color Color for the last value circle
     * @return Self, with modified color for the last value circle
     */
    public SparklineGraph2DRendererUpdate lastValueColor(Color color){
        lastValueColor = color;
        return self();
    }
    
    /**
     * Sets the parameter of the diameter of the drawn circles.
     * @param diameter Size of circle diameter
     * @return Self, with modified circle diameter
     */
    public SparklineGraph2DRendererUpdate circleDiameter(int diameter){
        circleDiameter = diameter;
        return self();
    }
    
    /**
     * Sets the parameter of whether circles are drawn.
     * @param decision Whether circles are drawn
     * @return Self, with modified parameter of deciding whether circles are drawn
     */
    public SparklineGraph2DRendererUpdate drawCircles(boolean decision){
        this.drawCircles = decision;
        return self();
    }
    
    /**
     * Sets the interpolation scheme parameter.
     * @param scheme New interpolation scheme
     * @return Self, with modified interpolation scheme
     */
    public SparklineGraph2DRendererUpdate interpolation(InterpolationScheme scheme) {
        if (scheme == null) {
            throw new NullPointerException("Interpolation scheme chan't be null");
        }
        if (!LineTimeGraph2DRenderer.supportedInterpolationScheme.contains(scheme)) {
            throw new IllegalArgumentException("Interpolation " + scheme + " is not supported");
        }
        this.interpolation = scheme;
        return self();
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
