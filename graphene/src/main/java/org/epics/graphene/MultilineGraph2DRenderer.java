/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import static org.epics.graphene.ColorScheme.*;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 *
 * @author asbarber 
 * @author jkfeng 
 * @author sjdallst
 */
public class MultilineGraph2DRenderer extends Graph2DRenderer{
    
    public MultilineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight);       
    }
    
    private ValueColorScheme colorScheme;
    private ColorScheme valueColorScheme = ColorScheme.GRAY_SCALE;
    //Scaling Schemes    
    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 

    private InterpolationScheme interpolation = InterpolationScheme.LINEAR;
    
    public void draw(Graphics2D g, List<Point2DDataset> data) {
        this.g = g;
        
        //Calculate range, range will end up being from the lowest point to highest in all of the given data.
        for(Point2DDataset dataPiece: data){
          super.calculateRanges(dataPiece.getXStatistics(), dataPiece.getYStatistics());  
        }
        calculateLabels();
        calculateGraphArea();
        drawBackground();
        drawGraphArea();
        
        Range datasetRange = RangeUtil.range(0,data.size());
        //Set color scheme
        switch(valueColorScheme){
            case GRAY_SCALE:
                colorScheme = ValueColorSchemes.grayScale(datasetRange);
                break;
            case JET:
                colorScheme = ValueColorSchemes.jetScale(datasetRange);
                break;
            case HOT:
                colorScheme = ValueColorSchemes.hotScale(datasetRange);
                break;
            case COOL:
                colorScheme = ValueColorSchemes.coolScale(datasetRange);
                break;
            case SPRING:
                colorScheme = ValueColorSchemes.springScale(datasetRange);
                break;          
            case BONE:
                colorScheme = ValueColorSchemes.boneScale(datasetRange);
                break;   
            case COPPER:
                colorScheme = ValueColorSchemes.copperScale(datasetRange);
                break;                 
            case PINK:
                colorScheme = ValueColorSchemes.pinkScale(datasetRange);
                break;                  
        }
        
        //Draw a line for each set of data in the data array.
        for(int datasetNumber = 0; datasetNumber < data.size(); datasetNumber++){
            SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.get(datasetNumber).getXValues());
            ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.get(datasetNumber).getYValues(), xValues.getIndexes());        
            setClip(g);
            g.setColor(new Color(colorScheme.colorFor((double)datasetNumber)));
            drawValueExplicitLine(xValues, yValues, interpolation, ReductionScheme.FIRST_MAX_MIN_LAST);
        }
    }
    

    
    public void update(MultilineGraph2DRendererUpdate update) {
        super.update(update);
        
        if(update.getValueColorScheme() != null){
            valueColorScheme = update.getValueColorScheme();
        }
    }
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new MultilineGraph2DRendererUpdate();
    }
}
