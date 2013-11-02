/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 *
 * @author asbarber
 * @author jkfeng
 * @author sjdallst
 */
public class SparklineGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{

    /**
     * Creates a new sparkline graph renderer.
     * Will draw a circle at the max value, min value, and last value.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight);
        super.xLabelMargin = 0;
        super.yLabelMargin = 0;        
    }
    
    //Parameters
    private int     circleDiameter = 7;
    private Color   minValueColor = Color.BLUE,
                    maxValueColor = Color.RED,
                    lastValueColor = Color.GREEN;
    private boolean drawCircles = true;
    
    //Min, Max, Last Values and Indices
    private int     maxIndex, 
                    minIndex,
                    lastIndex;
    private double  maxValueY = 0, 
                    minValueY = 0,
                    lastValueY = 0;    

    //Scaling Schemes    
    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 

    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOUR;

    
    //DRAWING FUNCTIONS
    
    /**
     * Draws the graph on the given graphics context.
     * 
     * @param g the graphics on which to display the data
     * @param data the data to display
     */
    public void draw(Graphics2D g, Point2DDataset data) {
        this.g = g;
        
        //General Rendering
        calculateRanges(data.getXStatistics(), data.getYStatistics());
        calculateGraphArea();
        drawBackground();
        
        g.setColor(Color.BLACK);        
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Calculates data values
        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.getXValues());
        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.getYValues(), xValues.getIndexes());        
        setClip(g);
        
        setMaxIndex(data);
        setMinIndex(data);
        setLastIndex(data);
        
        //Draws a circle at the max, min, and last value
        if(drawCircles){
            //Min
            double x = scaledX(data.getXValues().getDouble(minIndex));
            double y = scaledY(data.getYValues().getDouble(minIndex));
            g.setColor(minValueColor);
            Shape circle = createShape(x, y, circleDiameter);
            g.fill(circle);
            g.draw(circle);
            
            //Max
            x = scaledX(data.getXValues().getDouble(maxIndex));
            y = scaledY(data.getYValues().getDouble(maxIndex));
            g.setColor(maxValueColor);
            circle = createShape(x, y, circleDiameter);
            g.fill(circle);
            g.draw(circle);
            
            //Last
            x = scaledX(data.getXValues().getDouble(lastIndex));
            y = scaledY(data.getYValues().getDouble(lastIndex));
            g.setColor(lastValueColor);
            circle = createShape(x, y, circleDiameter);
            g.fill(circle); 
            g.draw(circle);
            
            g.setColor(Color.BLACK);
        }
        
        //Draws Line
        drawValueExplicitLine(xValues, yValues, interpolation, ReductionScheme.FIRST_MAX_MIN_LAST);      
    }
    
    /**
     * Creates a circle shape at the given position with given size.
     * @param x x position of shape
     * @param y y position of shape
     * @param size Diameter of circle
     * @return Ellipse (circle) shape
     */
    protected Shape createShape(double x, double y, double size) {
        double halfSize = size / 2;
        Ellipse2D.Double circle = new Ellipse2D.Double(x-halfSize, y-halfSize, size, size);
        return circle;
    } 
    
    private void setLastIndex(Point2DDataset data){
        lastIndex = data.getCount()-1;
    }
    private void setMaxIndex(Point2DDataset data){
        double maxValue = Double.MIN_VALUE;
        for(int i = 0; i < data.getCount();i++){
            if(data.getYValues().getDouble(i) >= maxValue){
                maxValue = data.getYValues().getDouble(i);
                maxIndex = i;
            }
        }
    }
    private void setMinIndex(Point2DDataset data){
        double minValue = Double.MAX_VALUE;
        for(int i = 0; i < data.getCount();i++){
            if(data.getYValues().getDouble(i) <= minValue){
                minValue = data.getYValues().getDouble(i);
                minIndex = i;
            }
        }
    }

    /**
     * Applies the update to the renderer.
     * 
     * @param update the update to apply
     */    
    public void update(SparklineGraph2DRendererUpdate update) {
        super.update(update);

        //Applies updates to members of this class
        if (update.getMinValueColor() != null){
            minValueColor = update.getMinValueColor();
        }
        if (update.getMaxValueColor() != null){
            maxValueColor = update.getMaxValueColor();
        }
        if (update.getCircleDiameter() != null){
            circleDiameter = update.getCircleDiameter();
        }
        if (update.getDrawCircles() != null){
            drawCircles = update.getDrawCircles();
        }
        if (update.getInterpolation() != null) {
            interpolation = update.getInterpolation();
        }        
    }
    
    /**
     * A new update object for a Sparkline graph.
     * @return Update of the Sparkline
     */
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new SparklineGraph2DRendererUpdate();
    }
    
    /**
     * The current interpolation used for the line.
     * 
     * @return the current interpolation
     */
    public InterpolationScheme getInterpolation() {
        return interpolation;
    }  
    
    /**
     * The index corresponding to the maximum value in the data set.
     * If there are multiple maximums, the greatest index is returned.
     * @return The index of the maximum value
     */
    public int getMaxIndex(){
        return maxIndex;
    }
    
    /**
     * The index corresponding to the minimum value in the data set.
     * If there are multiple minimums, the greatest index is returned.
     * @return The index of the minimum value
     */
    public int getMinIndex(){
        return minIndex;
    }
    
    /**
     * The index corresponding to the last value in the data set.
     * @return The index of the last value
     */
    public int getLastIndex(){
        return lastIndex;
    }
    
    /**
     * The maximum y-value in the list of data.
     * If there are multiple maximum values, the last maximum
     * (determined by the greatest index) is the value returned.
     * @return The data value of the maximum
     */
    public double getMaxValue(){
        return maxValueY;
    }
    
    /**
     * The minimum y-value in the list of data.
     * If there are multiple minimum values, the last minimum
     * (determined by the greatest index) is the value returned.
     * @return The data value of the minimum
     */
    public double getMinValue(){
        return minValueY;
    }
    
    /**
     * The last y-value in the list of data.
     * @return The data value for the last index
     */
    public double getLastValue(){
        return lastValueY;
    }
    
    public boolean getDrawCircles(){
        return drawCircles;
    }
    
    public Color getMinValueColor(){
        return minValueColor;
    }
    
    public Color getMaxValueColor(){
        return maxValueColor;
    }
    
    public Color getLastValueColor(){
        return lastValueColor;
    }
    
    public int getCircleDiameter(){
        return circleDiameter;
    }
}