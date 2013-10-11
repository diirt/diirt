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
    
    private int circleDiameter;
    private Color minColor, maxColor, currentValueColor;
    private boolean drawCircles;
    
    public SparklineGraph2DRendererUpdate minColor(Color color){
        minColor = color;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate maxColor(Color color){
        maxColor = color;
        return self();
    }
    
    public SparklineGraph2DRendererUpdate currentValueColor(Color color){
        currentValueColor = color;
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
}
