/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 *
 * @author asbarber 
 * @author jkfeng 
 * @author sjdallst
 */
public class SparklineMultilineGraph2DRenderer extends SparklineGraph2DRenderer{
    
    public SparklineMultilineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight);
        
        colorArray = new ArrayList<>();        
    }
    
    private List<Color> colorArray;
    private int MAX_COLOR = 255;
    
    public void draw(Graphics2D g, List<Point2DDataset> data) {
        this.g = g;
        
        //Calculate range, range will end up being from the lowest point to highest in all of the given data.
        for(Point2DDataset dataPiece: data){
          super.calculateRanges(dataPiece.getXStatistics(), dataPiece.getYStatistics());  
        }
        super.calculateGraphArea();
        super.drawBackground();
            
        //If more colors are needed to represent the different data lines, auto generate colors
        if(colorArray.size() < data.size()){
            for(int datasetNumber = 0; datasetNumber < data.size(); datasetNumber++){
                colorArray.set(datasetNumber, getDefaultColor(datasetNumber, data.size()));
            }
        }
        
        //Draw a line for each set of data in the data array.
        for(int datasetNumber = 0; datasetNumber < data.size(); datasetNumber++){
            SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.get(datasetNumber).getXValues());
            ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.get(datasetNumber).getYValues(), xValues.getIndexes());        
            setClip(g);
            g.setColor(colorArray.get(datasetNumber));
            drawValueExplicitLine(xValues, yValues, getInterpolation(), ReductionScheme.FIRST_MAX_MIN_LAST);
        }
    }
    
    private Color getDefaultColor(int indexValue, int sizeConstant){
        int cValue = (int)(MAX_COLOR*(((double)indexValue)/sizeConstant));
        return new Color (cValue, cValue, cValue);
    }
    public List<Color> getColorArray(){
        return colorArray;
    }
    
    public void update(SparklineMultilineGraph2DRendererUpdate update) {
        super.update(update);
        if(update.getColorArray() != null){
            colorArray = update.getColorArray();
        }
    }
}
