/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
        drawBackground();
        calculateLabels();
        calculateGraphArea();        
        drawGraphArea();
            
        colorScheme = ValueColorSchemes.grayScale(RangeUtil.range(0,data.size()));
        
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
    }
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new MultilineGraph2DRendererUpdate();
    }
}
