/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

public class SparklineMultilineGraph2DRenderer extends SparklineGraph2DRenderer{
    
    public SparklineMultilineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight);
    }
    
    private Color [] colorArray;
    public void draw(Graphics2D g, Point2DDataset[] data) {
        this.g = g;
        
        //General Rendering
        for(Point2DDataset dataPiece: data){
          super.calculateRanges(dataPiece.getXStatistics(), dataPiece.getYStatistics());  
        }
        super.calculateGraphAreaNoLabels();
        super.drawBackground();
    
        int MAXCOLOR = 255;
        
        if(colorArray == null){
            colorArray = new Color[data.length];
            for(int datasetNumber = 0; datasetNumber < data.length; datasetNumber++){
                colorArray[datasetNumber] = new Color((int)(MAXCOLOR*(((double)datasetNumber)/data.length)),(int)(MAXCOLOR*(((double)datasetNumber)/data.length)), (int)(MAXCOLOR*(((double)datasetNumber)/data.length)));
            }
        }
        for(int datasetNumber = 0; datasetNumber < data.length; datasetNumber++){
        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data[datasetNumber].getXValues());
        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data[datasetNumber].getYValues(), xValues.getIndexes());        
        setClip(g);
        if(datasetNumber > 0){
            g.setColor(colorArray[datasetNumber]);
        }
        else{
            g.setColor(Color.BLACK);
        }
            drawValueExplicitLine(xValues, yValues, getInterpolation(), ReductionScheme.FIRST_MAX_MIN_LAST);
        }
    }
    public Color [] getColorArray(){
        return colorArray;
    }
    
    public void setColor(Color color, int index){
        colorArray[index] = color;
    }
    
    public void update(SparklineMultilineGraph2DRendererUpdate update) {
        super.update(update);
        if(update.getColorArray() != null){
            colorArray = update.getColorArray();
        }
    }
}
