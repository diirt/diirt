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
    
    private String dataType;
    protected int dataHeight;
    
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
        labelHeight = imageHeight/3;
        dataHeight = imageHeight/3+20;
        this.dataType = dataType;       
    }
    
    /**
     * The type of data on the y-axis of the Sparkline.
     * @return The type of data represented in the Sparkline.
     */
    public String getDataType(){
        return this.dataType;
    }
        
    /**
     * Sets the height of the data rendered to the image in pixels.
     * @param newHeight The new height of the data in pixels.
     */
    public void setDataHeight(int newHeight){
        this.dataHeight = newHeight;
    }
    
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
        
        //Determines Font Size
        int fontSize = 18;
        int pixelsOfString = 0;       
        boolean headerIsLonger = dataType.length() < "Data TypeA".length();
        
        //Continually decreases font size while size (in pixels) is too large
        do{
            fontSize--;
            g.setFont(new Font("Serif",Font.BOLD, fontSize)); 
            
            //Base max font size from the header "Data Type"
            if (headerIsLonger){
                pixelsOfString = g.getFontMetrics().stringWidth("Data Type");                
            }
            //Base max font size from the data type text
            else{
                pixelsOfString = g.getFontMetrics().stringWidth(dataType);                
            }
        }while(pixelsOfString > (topAreaMargin - 25));     

        //Updates font size
        g.setFont(new Font("Serif",Font.PLAIN, fontSize));
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
        
                
        //TODO: find a way to make this more thread friendly/faster.
        if (yValues.size() < 2){
            return; //Data size validation
        }
        
        //Data values to be displayed in margins
        double lastNum = (yValues.getDouble(yValues.size()-1));
        double secondLastNum = (yValues.getDouble(yValues.size()-2));
        double rawChange = (lastNum-secondLastNum);
        double percentChange = rawChange/secondLastNum*100;

        String rawChangeString = convertToReadable(rawChange);
        String lastNumString = convertToReadable(lastNum);
        String percentChangeString = convertToReadable(percentChange);
        
        //Updates size of render area
        setDataHeight(this.labelHeight+(int)(fontSize*1.15));
              
        //Sparkline Margins//
        //Alignments
        Java2DStringUtilities.Alignment leftAlignment = Java2DStringUtilities.Alignment.BOTTOM_LEFT;
        Java2DStringUtilities.Alignment rightAlignment = Java2DStringUtilities.Alignment.BOTTOM_RIGHT;                
        
        //Value
        Java2DStringUtilities.drawString(g, leftAlignment, getImageWidth()-this.bottomAreaMargin, this.dataHeight, lastNumString);
        
        //Change
        Java2DStringUtilities.drawString(g, leftAlignment, getImageWidth()-this.bottomAreaMargin + (int)((fontSize)*3.8), this.dataHeight, rawChangeString);
        Java2DStringUtilities.drawString(g, leftAlignment, getImageWidth()-this.bottomAreaMargin + (int)((fontSize)*3.2), this.dataHeight+(int)((fontSize)*1.15), " (" + percentChangeString + "%)");
        
        //Data Type
        Java2DStringUtilities.drawString(g, rightAlignment, this.topAreaMargin-3, this.dataHeight, getDataType());
        
        //Data Headers
        g.setFont(new Font("Serif",Font.BOLD, fontSize));            
        Java2DStringUtilities.drawString(g, leftAlignment, getImageWidth()-this.bottomAreaMargin, this.labelHeight, "Value");
        Java2DStringUtilities.drawString(g, leftAlignment, getImageWidth()-this.bottomAreaMargin + (int)((fontSize)*3.8), this.labelHeight, "Change");
        Java2DStringUtilities.drawString(g, rightAlignment, this.topAreaMargin-3, this.labelHeight, "Data Type");        
    }
    
    /**
     * Converts the number to scientific notation if it is not between 10^-3 and 10^4.
     * 
     * @param originalNumber Number to format
     * @return Number in scientific notation
     */
    protected String convertToReadable(double originalNumber){
        int power = 0;
        
        //When number does not need formatting:  between 10^-3 and 10^4
        if(Math.abs(originalNumber) < 10000 && Math.abs(originalNumber) > .001){
            //Truncates after 4 decimal points
            int converter = (int)(originalNumber*1000);
            originalNumber = (converter/1000.0);
            return Double.toString(originalNumber);
        }
        //originalNumber is above 10^4
        else if (Math.abs(originalNumber) > 10000){
            //Minimum of 10^4
            power = 4;
            
            //Finds value x in E^x of scientific notation of originalNumber
            while(Math.abs(originalNumber)/(Math.pow(10,power)) >= 1){
                power+=1;
            }
            power-=1;
            
            //Truncates after 3 decimal points
            double readableNumber = originalNumber/(Math.pow(10,power));
            int converter = (int)(100*readableNumber);
            readableNumber = converter/100.0;
            
            //Converts to scientific notation
            return Double.toString(readableNumber) + "E" + Integer.toString(power);
        }
        //originalNumber is below 10^-3
        else{
            //Maximum of 10^-4
            power = -4;
            
            //Finds value x in E^x of scientific notation of originalNumber
            while(Math.abs(originalNumber)/(Math.pow(10,power)) <= 1){
                power-=1;
            }
            
            //Truncates after 3 decimal points
            double readableNumber = originalNumber/(Math.pow(10,power));
            int converter = (int)(10*readableNumber);
            readableNumber = converter/10.0;
            
            //Converts to scientific notation
            return Double.toString(readableNumber) + "E" + Integer.toString(power);            
        }
    }
    
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
    protected int labelHeight;
    
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