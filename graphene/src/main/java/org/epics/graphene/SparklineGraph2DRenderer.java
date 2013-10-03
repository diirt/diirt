/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.util.Arrays;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;


/**
 *
 * @author Samuel
 */
public class SparklineGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{
    /**
     * Creates a new sparkline graph renderer.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight, String dataType){
        super(imageWidth, imageHeight);
        bottomAreaMargin = (int)(imageWidth/5*1.5); //Right Side
        topAreaMargin = imageWidth/5; //Left Side
        rightAreaMargin = imageHeight/8;
        leftAreaMargin = imageHeight/8;
        this.dataType = dataType;
        
    }
    private String dataType;
    
    public String getDataType(){
        return this.dataType;
    }
    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 

    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOUR;
    private ReductionScheme reduction = ReductionScheme.FIRST_MAX_MIN_LAST;
    private Integer focusPixelX;
    private boolean highlightFocusValue = false;
    private int focusValueIndex = -1;
    private int currentIndex;
    private double currentScaledDiff;


    /**
     * Creates a new sparkline graph renderer.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */    
    public SparklineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight);
        super.rightMargin = 120;

    }
    
    public void update(LineGraph2DRendererUpdate update) {
        super.update(update);
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
        
        calculateRanges(data.getXStatistics(), data.getYStatistics());
        calculateGraphArea();
        drawBackground();
        drawGraphArea();
        int fontSize = (int)(getImageWidth()/30.0);
        g.setFont(new Font("Serif",Font.PLAIN,(int)(getImageWidth()/30.0)));//(new Font("Helvetica",2,int(Math.log(double(getImageWidth()))))));      
        SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.getXValues());
        ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.getYValues(), xValues.getIndexes());

        setClip(g);
        g.setColor(Color.BLACK);

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
        //TODO: find a way to make this more thread friendly/faster.
        //TODO: make it so the datatype label moves away from the graph, the longer the string for the datatype.
        //TODO: split this into methods that change text size and stuff, based on the size of the graph.
        double lastNum = (yValues.getDouble(yValues.size()-1));
        double secondLastNum = (yValues.getDouble(yValues.size()-2));
        double percentChange = (lastNum-secondLastNum)/secondLastNum*100;
        int converter = (int)(percentChange*1000);
        percentChange = converter/1000.0;
        Java2DStringUtilities.Alignment alignment = Java2DStringUtilities.Alignment.BOTTOM_LEFT;
        Java2DStringUtilities.drawString(g, alignment, getImageWidth()-this.bottomAreaMargin, getImageHeight()/3, "Value");
        Java2DStringUtilities.drawString(g, alignment, getImageWidth()-this.bottomAreaMargin, getImageHeight()/3+20,
        Double.toString(yValues.getDouble(yValues.size()-1)));
        Java2DStringUtilities.drawString(g, alignment, getImageWidth()-this.bottomAreaMargin + (this.fontSize)*3, getImageHeight()/3, "Change");
        Java2DStringUtilities.drawString(g, alignment, getImageWidth()-this.bottomAreaMargin + (this.fontSize)*3, getImageHeight()/3+20,
        Double.toString(lastNum - secondLastNum)+ " (" + 
        Double.toString(percentChange) + "%)");
        alignment = Java2DStringUtilities.Alignment.BOTTOM_RIGHT;
        Java2DStringUtilities.drawString(g, alignment, this.topAreaMargin-3, getImageHeight()/3, "Data Type");
        Java2DStringUtilities.drawString(g, alignment, this.topAreaMargin-3, getImageHeight()/3 + 20, getDataType());
    }
    
    @Override 
    protected void drawGraphArea(){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
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
    
    public boolean isHighlightFocusValue() {
        return highlightFocusValue;
    }  
    
    public int getFocusValueIndex() {
        return focusValueIndex;
    }  
    
    public Integer getFocusPixelX() {
        return focusPixelX;
    }

    @Override
    protected void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY) {
        if (focusPixelX != null) {
            double scaledDiff = Math.abs(scaledX - focusPixelX);
            if (scaledDiff < currentScaledDiff) {
                currentIndex = index;
                currentScaledDiff = scaledDiff;
            }
        }
    }
    protected void drawCurrentValue(){
        
    }    
}