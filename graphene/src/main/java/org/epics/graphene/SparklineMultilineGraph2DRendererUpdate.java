/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;

/**
 *
 * @author asbarber, jkfeng, sjdallst
 */
public class SparklineMultilineGraph2DRendererUpdate extends Graph2DRendererUpdate<SparklineMultilineGraph2DRendererUpdate>{
    private Color [] colorArray;
    
    public Color [] getColorArray(){
        return colorArray;
    }
    
    public SparklineMultilineGraph2DRendererUpdate colorArray (Color[] newColorArray){
        colorArray = newColorArray;
        return self();
    }
    
    public SparklineMultilineGraph2DRendererUpdate setColor (Color[] colorArrayOriginal, int index, Color color){
        colorArray = colorArrayOriginal;
        colorArray[index] = color;
        return self();
    }
}
