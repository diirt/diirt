/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import static org.epics.graphene.ReductionScheme.FIRST_MAX_MIN_LAST;
import static org.epics.graphene.ReductionScheme.NONE;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListMath;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;


/**
 *
 * @authors asbarber, jkfeng, sjdallst
 */
public class SparklineGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{
    
    
    
    /**
     * Creates a new sparkline graph renderer.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */    
    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight); 
        circleDiameter = 10;
        minColor = Color.BLUE;
        maxColor = Color.RED;
        currentValueColor = Color.GREEN;
        drawCircles = false;
    }
    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, boolean includeCircles){
        super(imageWidth, imageHeight); 
        circleDiameter = 10;
        minColor = Color.BLUE;
        maxColor = Color.RED;
        currentValueColor = Color.GREEN;
        drawCircles = includeCircles;
    }
    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, int diameter){
        super(imageWidth, imageHeight); 
        circleDiameter = diameter;
        minColor = Color.BLUE;
        maxColor = Color.RED;
        currentValueColor = Color.GREEN;
        drawCircles = true;
    }
    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, Color min, Color max, Color current, int diameter){
        super(imageWidth, imageHeight); 
        circleDiameter = diameter;
        minColor = min;
        maxColor = max;
        currentValueColor = current;
        drawCircles = true;
    }
    
    private int circleDiameter;
    private Color minColor, maxColor, currentValueColor;
    private boolean drawCircles;
    /**
     * The type of data on the y-axis of the Sparkline.
     * @return The type of data represented in the Sparkline.
     */

        
    /**
     * Sets the height of the data rendered to the image in pixels.
     * @param newHeight The new height of the data in pixels.
     */
    
    /**
     * Applies the update to the renderer.
     * 
     * @param update the update to apply
     */    
    public void update(LineGraph2DRendererUpdate update) {
        //Necessary Graph2DRendered Update call
        super.update(update);
        
        //Appropriate scaling methods
        if (update.getInterpolation() != null) {
            interpolation = update.getInterpolation();
        }
        if (update.getDataReduction() != null) {
            reduction = update.getDataReduction();
        }
        if (update.getFocusPixelX()!= null) {
            focusPixelX = update.getFocusPixelX();
        }
        if (update.getHighlightFocusValue()!= null) {
            highlightFocusValue = update.getHighlightFocusValue();
        }
    }

    /**
     * Draws the graph on the given graphics context.
     * 
     * @param g the graphics on which to display the data
     * @param data the data to display
     */
    public void draw(Graphics2D g, Point2DDataset data) {
        this.g = g;
        
        //General Render
        calculateRanges(data.getXStatistics(), data.getYStatistics());
        calculateGraphArea();
        drawBackground();
        drawGraphArea();
        
        g.setColor(Color.BLACK);
        
        //Calculates data values
        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.getXValues());
        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.getYValues(), xValues.getIndexes());        
        setClip(g);
        
        //Appropriate scaling methods
        currentIndex = 0;
        currentScaledDiff = getImageWidth();
        drawValueExplicitLine(xValues, yValues, interpolation, reduction);
        if (focusPixelX != null) {
            focusValueIndex = xValues.getIndexes().getInt(currentIndex);
            if (highlightFocusValue) {
                g.setColor(new Color(0, 0, 0, 128));
                int x = (int) scaledX(xValues.getDouble(currentIndex));
                g.drawLine(x, yAreaStart, x, yAreaEnd);
            }
        } else {
            focusValueIndex = -1;
        }
        if(drawCircles){
            drawCircle(g, data, xValues, yValues, getMinIndex(data), minColor);
            drawCircle(g, data, xValues, yValues, getMaxIndex(data), maxColor);
            drawCircle(g, data, xValues, yValues, data.getCount()-1, currentValueColor);
        }
    }
    
    /**
     * Converts the number to scientific notation if it is not between 10^-3 and 10^4.
     * 
     * @param originalNumber Number to format
     * @return Number in scientific notation
     */
    
    //Scaling Schemes    
    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 

    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOUR;
    private ReductionScheme reduction = ReductionScheme.FIRST_MAX_MIN_LAST;
    
    //Focus Value
    private Integer focusPixelX;
    private boolean highlightFocusValue = false;
    private int focusValueIndex = -1;
    
    //Current Value
    private int currentIndex;
    private double currentScaledDiff;
    
    @Override 
    protected void drawGraphArea(){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
     * The current design for highlighting the focus value or not.
     * 
     * @return Whether to highlight the focus value
     */
    public boolean isHighlightFocusValue() {
        return highlightFocusValue;
    }    
    
    /**
     * The current focus value index. Returns -1 if no focus value.
     * 
     * @return Index of focus value
     */
    public int getFocusValueIndex() {
        return focusValueIndex;
    }    
    
    /**
     * The current x pixel of the focus value.
     * @return x pixel of focus value
     */
    public Integer getFocusPixelX() {
        return focusPixelX;
    }
    
    public int getMaxIndex(Point2DDataset data){
        double max = data.getYStatistics().getMaximum().doubleValue();
        for(int i = 0; i < data.getCount(); i++){
            if(data.getYValues().getDouble(i) == max)
                return i;
        }
        return 0;
    }
    
    public int getMinIndex(Point2DDataset data){
        double min = data.getYStatistics().getMinimum().doubleValue();
        for(int i = 0; i < data.getCount(); i++){
            if(data.getYValues().getDouble(i) == min)
                return i;
        }
        return 0;
    }
    
    public void drawCircle(Graphics2D g, Point2DDataset data, SortedListView xValues, ListNumber yValues, int index, Color color){
        int x = (int) (scaledX(xValues.getDouble(index)) - .5*circleDiameter);
        int y = (int) (scaledY(yValues.getDouble(index)) - .5*circleDiameter);
        g.setColor(color);
        g.fillOval(x, y, circleDiameter, circleDiameter);
        g.setColor(Color.BLACK);
    }
    
    public void setMinColor(Color color){
        minColor = color;
    }
    
    public void setMaxColor(Color color){
        maxColor = color;
    }
    
    public void setCurrentValueColor(Color color){
        currentValueColor = color;
    }
    
    public void setDiameter(int diameter){
        circleDiameter = diameter;
    }
    
    public void setDrawCircles(boolean decision){
        drawCircles = decision;
    }
    
    @Override
    protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
        //If there is a focus value with a pixel location
        if (focusPixelX != null) {
            
            //Scaled value
            double scaledDiff = Math.abs(scaledX - focusPixelX);
            
            //Rescale if necessary
            if (scaledDiff < currentScaledDiff) {
                currentIndex = index;
                currentScaledDiff = scaledDiff;
            }
        }
    }

}