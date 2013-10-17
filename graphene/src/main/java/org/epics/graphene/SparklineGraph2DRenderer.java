/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Collections;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;


/**
 *
 * @authors asbarber, jkfeng, sjdallst
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
    private double  maxValue = 0, 
                    minValue = 0,
                    lastValue = 0;    

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
        super.calculateRanges(data.getXStatistics(), data.getYStatistics());
        super.calculateGraphAreaNoLabels();
        super.drawBackground();
        
        g.setColor(Color.BLACK);
        
        //Calculates data values
        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.getXValues());
        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.getYValues(), xValues.getIndexes());        
        setClip(g);
        drawValueExplicitLine(xValues, yValues, interpolation, ReductionScheme.FIRST_MAX_MIN_LAST);
        //Draws a circle at the max, min, and current value
        if(drawCircles){
            drawCircle(g, data, xValues, yValues, minIndex, minValueColor);
            drawCircle(g, data, xValues, yValues, maxIndex, maxValueColor);                
            drawCircle(g, data, xValues, yValues, lastIndex, lastValueColor);
        }
    }

    /**
     * Sets the rendering hint to render with antialiasing.
     */
    @Override 
    protected void drawGraphArea(){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    } 
    
    /**
     * Draws a circle at the corresponding index.
     * @param g Graphics
     * @param data Collection of points
     * @param xValues x values
     * @param yValues y values
     * @param index Position to draw the circle
     * @param color Color of the circle
     */
    //See bubble graph for reimplementation
    public void drawCircle(Graphics2D g, Point2DDataset data, SortedListView xValues, ListNumber yValues, int index, Color color){
        int x = (int) (scaledX(xValues.getDouble(index)) - .5*circleDiameter);
        int y = (int) (scaledY(yValues.getDouble(index)) - .5*circleDiameter);
        
        g.setColor(color);
        g.fillOval(x, y, circleDiameter, circleDiameter);
        g.setColor(Color.BLACK);
    }
    private Shape createShape(double x, double y, double size) {
        double halfSize = size / 2;
        Ellipse2D.Double circle = new Ellipse2D.Double(x-halfSize, y-halfSize, size, size);
        return circle;
    }       
    
    
    /**
     * 
     * @param index
     * @param valueX
     * @param valueY
     * @param scaledX
     * @param scaledY 
     */
    @Override
    protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
        //Checks if new value is the new min or the new max
        
        //Base Case
        if (index == 0){
            maxValue = valueY;
            minValue = valueY;
        }
        else{
            //Max
            if (maxValue <= valueY){
                maxValue = valueY;
                maxIndex = index;
            }
            //Min
            if (minValue >= valueY){
                minValue = valueY;
                minIndex = index;
            }  
        }
        
        //New point is always last point
        lastValue = valueY;
        lastIndex = index;
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
        return maxValue;
    }
    
    /**
     * The minimum y-value in the list of data.
     * If there are multiple minimum values, the last minimum
     * (determined by the greatest index) is the value returned.
     * @return The data value of the minimum
     */
    public double getMinValue(){
        return minValue;
    }
    
    /**
     * The last y-value in the list of data.
     * @return The data value for the last index
     */
    public double getLastValue(){
        return lastValue;
    }
}