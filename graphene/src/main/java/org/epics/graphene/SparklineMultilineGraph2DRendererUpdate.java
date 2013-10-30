/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author asbarber
 * @author jkfeng
 * @author sjdallst
 */
public class SparklineMultilineGraph2DRendererUpdate extends Graph2DRendererUpdate<SparklineMultilineGraph2DRendererUpdate>{
    private List<Color> colorArray;
    
    public List<Color> getColorArray(){
        return colorArray;
    }
    
    public SparklineMultilineGraph2DRendererUpdate colorArray (List<Color> newColorArray){
        colorArray = newColorArray;
        return self();
    }
    
    public SparklineMultilineGraph2DRendererUpdate setColor (List<Color> originalArray, int index, Color color){
        originalArray.set(index, color);
        colorArray = originalArray;
        return self();
    }
}
