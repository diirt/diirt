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
    
    //UPDATE FUNCTIONS
    
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
  
    
    //GET FUNCTIONS
    
    /**
     * The current interpolation used for the line.
     * 
     * @return the current interpolation
     */
    public InterpolationScheme getInterpolation() {
        return interpolation;
    }  
    
    public int getMaxIndex(){
        return maxIndex;
    }
    
    public int getMinIndex(){
        return minIndex;
    }
    
    public int getLastIndex(){
        return lastIndex;
    }
    
    public double getMaxValue(){
        return maxValue;
    }
    
    public double getMinValue(){
        return minValue;
    }
    
    public double getLastValue(){
        return lastValue;
    }

}



///**
// * Copyright (C) 2012 Brookhaven National Laboratory
// * All rights reserved. Use is subject to license terms.
// */
//package org.epics.graphene;
//
//import java.awt.*;
//import java.awt.geom.Ellipse2D;
//import java.util.Arrays;
//import java.util.Collections;
//import org.epics.util.array.ArrayDouble;
//import org.epics.util.array.ListNumber;
//import org.epics.util.array.SortedListView;
//
//
///**
// *
// * @authors asbarber, jkfeng, sjdallst
// */
//public class SparklineGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{
//
//    /**
//     * Creates a new sparkline graph renderer.
//     * Default Colors: blue is min, red is max, green is current
//     * Default Diameter: 10
//     * Defaults to not draw circles
//     * 
//     * @param imageWidth the graph width
//     * @param imageHeight the graph height
//     */    
//    public SparklineGraph2DRenderer(int imageWidth, int imageHeight){
//        this(imageWidth, imageHeight, 7);
//        drawCircles = false;
//    }
//    
//    /**
//     * Creates a new sparkline graph renderer.
//     * Default Colors: blue is min, red is max, green is current
//     * Default Diameter: 10
//     * 
//     * @param imageWidth the graph width
//     * @param imageHeight the graph height
//     * @param includeCircles Whether to draw circle at min/max/current
//     */     
//    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, boolean includeCircles){
//        this(imageWidth, imageHeight);
//        drawCircles = includeCircles;
//    }
//    
//    /**
//     * Creates a new sparkline graph renderer.
//     * Default Colors: blue is min, red is max, green is current
//     * Defaults to draw circles
//     * 
//     * @param imageWidth the graph width
//     * @param imageHeight the graph height
//     * @param diameter Size of circle for min/max/current
//     */     
//    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, int diameter){
//        this(imageWidth, imageHeight, Color.BLUE, Color.RED, Color.GREEN, diameter);
//    }
//    
//    /**
//     * Creates a new sparkline graph renderer.
//     * 
//     * @param imageWidth the graph width
//     * @param imageHeight the graph height
//     * @param min Color for minimum value
//     * @param max Color for maximum value
//     * @param current Color for current value
//     * @param diameter Size of circle for min/max/current
//     */     
//    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, Color min, Color max, Color current, int diameter){
//        super(imageWidth, imageHeight); 
//        circleDiameter = diameter;
//        minColor = min;
//        maxColor = max;
//        currentValueColor = current;
//        drawCircles = true;     
//        
//        bottomMargin = 5;
//        topMargin = 5;
//        leftMargin = 5;
//        rightMargin = 5;
//    }
//    
//    //Options for the min/max/current of the data
//    private int circleDiameter;
//    private Color minColor, maxColor, currentValueColor;
//    private boolean drawCircles;
//    
//    //Min and Max Values
//    private int[] maxIndices = {0}, minIndices = {0};
//    private double maxValue = 0, minValue = 0;    
//
//    //Scaling Schemes    
//    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
//    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 
//
//    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOUR;
//    private ReductionScheme reduction = ReductionScheme.FIRST_MAX_MIN_LAST;
//    
//    //Focus Value
//    private Integer focusPixelX;
//    private boolean highlightFocusValue = false;
//    private int focusValueIndex = -1;
//    
//    //Current Value
//    private int currentIndex;
//    private double currentScaledDiff;
//    
//    
//    /**
//     * Applies the update to the renderer.
//     * 
//     * @param update the update to apply
//     */    
//    public void update(LineGraph2DRendererUpdate update) {
//        //Necessary Graph2DRendered Update call
//        super.update(update);
//        
//        //Appropriate scaling methods
//        if (update.getInterpolation() != null) {
//            interpolation = update.getInterpolation();
//        }
//        if (update.getDataReduction() != null) {
//            reduction = update.getDataReduction();
//        }
//        if (update.getFocusPixelX()!= null) {
//            focusPixelX = update.getFocusPixelX();
//        }
//        if (update.getHighlightFocusValue()!= null) {
//            highlightFocusValue = update.getHighlightFocusValue();
//        }
//    }
//
//    /**
//     * Draws the graph on the given graphics context.
//     * 
//     * @param g the graphics on which to display the data
//     * @param data the data to display
//     */
//    public void draw(Graphics2D g, Point2DDataset data) {
//        this.g = g;
//        
//        //General Rendering
//        calculateRanges(data.getXStatistics(), data.getYStatistics());
//        calculateGraphArea();
//        drawBackground();
//        //drawGraphArea();
//        
//        g.setColor(Color.BLACK);
//        
//        //Calculates data values
//        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.getXValues());
//        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.getYValues(), xValues.getIndexes());        
//        setClip(g);
//        
//        //Focus Value
//        currentIndex = 0;
//        currentScaledDiff = getImageWidth();
//        drawValueExplicitLine(xValues, yValues, interpolation, reduction);
//        if (focusPixelX != null) {
//            focusValueIndex = xValues.getIndexes().getInt(currentIndex);
//            if (highlightFocusValue) {
//                g.setColor(new Color(0, 0, 0, 128));
//                int x = (int) scaledX(xValues.getDouble(currentIndex));
//                g.drawLine(x, yAreaStart, x, yAreaEnd);
//            }
//        } else {
//            focusValueIndex = -1;
//        }
//        
//        //Draws a circle at the max, min, and current value
//        if(drawCircles){
//            for (int index: minIndices){
//                drawCircle(g, data, xValues, yValues, index, minColor);
//            }
//            for (int index: maxIndices){
//                drawCircle(g, data, xValues, yValues, index, maxColor);                
//            }
//            drawCircle(g, data, xValues, yValues, data.getCount()-1, currentValueColor);
//        }
//    }
//
//    @Override
//    protected void calculateGraphArea() {
//        int areaFromBottom = bottomMargin;
//        int areaFromLeft = leftMargin;
//
//        xPlotValueStart = getXPlotRange().getMinimum().doubleValue();
//        xPlotValueEnd = getXPlotRange().getMaximum().doubleValue();
//        if (xPlotValueStart == xPlotValueEnd) {
//            // If range is zero, fake a range
//            xPlotValueStart -= 1.0;
//            xPlotValueEnd += 1.0;
//        }
//        xAreaStart = areaFromLeft;
//        xAreaEnd = getImageWidth() - rightMargin - 1;
//        
//        //Adjusts for size of circle
//        xPlotCoordStart = xAreaStart + topAreaMargin + 1 + circleDiameter;
//        xPlotCoordEnd = xAreaEnd - bottomAreaMargin - 0.5 - circleDiameter;
//        xPlotCoordWidth = xPlotCoordEnd - xPlotCoordStart;
//        
//        yPlotValueStart = getYPlotRange().getMinimum().doubleValue();
//        yPlotValueEnd = getYPlotRange().getMaximum().doubleValue();
//        if (yPlotValueStart == yPlotValueEnd) {
//            // If range is zero, fake a range
//            yPlotValueStart -= 1.0;
//            yPlotValueEnd += 1.0;
//        }
//        yAreaStart = topMargin;
//        yAreaEnd = getImageHeight() - areaFromBottom - 1;
//        
//        //Adjusts for size of circle
//        yPlotCoordStart = yAreaStart + leftAreaMargin + 1 + circleDiameter;
//        yPlotCoordEnd = yAreaEnd - rightAreaMargin - 1 - circleDiameter;
//        yPlotCoordHeight = yPlotCoordEnd - yPlotCoordStart;
//    }
//    
//    @Override 
//    protected void drawGraphArea(){
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//    } 
//    
//    /**
//     * A new update object for a Sparkline graph.
//     * @return Update of the Sparkline
//     */
//    @Override
//    public Graph2DRendererUpdate newUpdate() {
//        return new SparklineGraph2DRendererUpdate();
//    }
//    
//    /**
//     * The current interpolation used for the line.
//     * 
//     * @return the current interpolation
//     */
//    public InterpolationScheme getInterpolation() {
//        return interpolation;
//    }  
//    
//    /**
//     * The current design for highlighting the focus value or not.
//     * 
//     * @return Whether to highlight the focus value
//     */
//    public boolean isHighlightFocusValue() {
//        return highlightFocusValue;
//    }    
//    
//    /**
//     * The current focus value index. Returns -1 if no focus value.
//     * 
//     * @return Index of focus value
//     */
//    public int getFocusValueIndex() {
//        return focusValueIndex;
//    }    
//    
//    /**
//     * The current x pixel of the focus value.
//     * @return x pixel of focus value
//     */
//    public Integer getFocusPixelX() {
//        return focusPixelX;
//    }
//    
//    /**
//     * @param data Points to find the minimum of
//     * @return The index of the min y-value
//     */
//    public int getMinIndex(Point2DDataset data){
//        double min = data.getYStatistics().getMinimum().doubleValue();
//        for(int i = 0; i < data.getCount(); i++){
//            if(data.getYValues().getDouble(i) == min)
//                return i;
//        }
//        return 0;
//    }
//    
//    /**
//     * @param data Points to find the maximum of
//     * @return The index of the max y-value
//     */
//    public int getMaxIndex(Point2DDataset data){
//        double max = data.getYStatistics().getMaximum().doubleValue();
//        for(int i = 0; i < data.getCount(); i++){
//            if(data.getYValues().getDouble(i) == max)
//                return i;
//        }
//        return 0;
//    }    
//    
//    /**
//     * Draws a circle at the corresponding index.
//     * @param g Graphics
//     * @param data Collection of points
//     * @param xValues x values
//     * @param yValues y values
//     * @param index Position to draw the circle
//     * @param color Color of the circle
//     */
//    public void drawCircle(Graphics2D g, Point2DDataset data, SortedListView xValues, ListNumber yValues, int index, Color color){
//        int x = (int) (scaledX(xValues.getDouble(index)) - .5*circleDiameter);
//        int y = (int) (scaledY(yValues.getDouble(index)) - .5*circleDiameter);
//        g.setColor(color);
//        g.fillOval(x, y, circleDiameter, circleDiameter);
//        g.setColor(Color.BLACK);
//    }
//    
//    /**
//     * Updates the color of the circle drawn at the minimum.
//     * @param color Color of circle
//     */
//    public void setMinColor(Color color){
//        minColor = color;
//    }
//    
//    /**
//     * Updates the color of the circle drawn at the maximum.
//     * @param color Color of circle
//     */    
//    public void setMaxColor(Color color){
//        maxColor = color;
//    }
//    
//    /**
//     * Updates the color of the circle drawn at the current value.
//     * @param color Color of circle
//     */    
//    public void setCurrentValueColor(Color color){
//        currentValueColor = color;
//    }
//    
//    /**
//     * Updates the diameter of the circle
//     * @param diameter Diameter size in pixels
//     */
//    public void setDiameter(int diameter){
//        circleDiameter = diameter;
//    }
//    
//    /**
//     * Updates the option to draw circles at max/min/current
//     * @param decision Whether to draw the circles
//     */
//    public void setDrawCircles(boolean decision){
//        drawCircles = decision;
//    }
//    
//    @Override
//    protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
//        //If there is a focus value with a pixel location
//        if (focusPixelX != null) {
//            
//            //Scaled value
//            double scaledDiff = Math.abs(scaledX - focusPixelX);
//            
//            //Rescale if necessary
//            if (scaledDiff < currentScaledDiff) {
//                currentIndex = index;
//                currentScaledDiff = scaledDiff;
//            }
//        }
//
//        
//        //Checks if new value is the new min or the new max
//        
//        //Base Case
//        if (index == 0){
//            maxValue = valueY;
//            minValue = valueY;
//            
//            maxIndices[0] = 0;
//            minIndices[0] = 0;
//        }
//        else{
//            //Max
//            if (maxValue < valueY){
//                maxValue = valueY;
//                
//                maxIndices = new int[]{0};  //Clears
//                maxIndices[0] = index;
//            }
//            //Another index equal to current max value
//            else if (maxValue == valueY){
//                maxIndices = addElementToArray(maxIndices, index);
//            }
//
//            //Min
//            if (minValue > valueY){
//                minValue = valueY;
//                
//                minIndices = new int[]{0};  //Clears               
//                minIndices[0] = index;
//            }
//            //Another index equal to current max value
//            else if (minValue == valueY){
//                minIndices = addElementToArray(minIndices, index);
//            }    
//        }
//    }
//
//    /**
//     * Adds an element to an array
//     * @param array Collection of values
//     * @param newElement Value to add to the collection
//     * @return The array with the new element inserted in the last position
//     */
//    private int[] addElementToArray(int[] array, int newElement){
//            //Makes room in array for the new element
//            int[] tmp = new int[array.length + 1];
//            
//            //Copies Array            
//            for (int i = 0; i < array.length; i++){
//                tmp[i] = array[i];
//            }
//           
//            //New Element
//            tmp[tmp.length -1] = newElement;
//            
//            return tmp;       
//    }
//
//}
