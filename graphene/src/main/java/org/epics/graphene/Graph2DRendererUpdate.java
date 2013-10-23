/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author carcassi
 */
public class Graph2DRendererUpdate<T extends Graph2DRendererUpdate<T>> {
    
    private Integer imageHeight;
    private Integer imageWidth;
    private AxisRange xAxisRange;
    private AxisRange yAxisRange;
    private ValueScale xValueScale;
    private ValueScale yValueScale;
    
    private Color backgroundColor;
    private Color labelColor;
    private Color referenceLineColor;
    private Font  labelFont;
    
    private Integer bottomMargin;
    private Integer topMargin;
    private Integer leftMargin;
    private Integer rightMargin;
    
    private Integer bottomAreaMargin;
    private Integer topAreaMargin;
    private Integer leftAreaMargin;
    private Integer rightAreaMargin;
    
    private Integer xLabelMargin;
    private Integer yLabelMargin;
    
    /**
     * Gets the update casted appropriately.
     * For all subclasses, casts the objects as the subclass object (not Graph2DRendererUpdate).
     * @return This casted as the appropriate object type
     */
    protected T self() {
        return (T) this;
    }
    
    /**
     * Updates the parameter for imageHeight.
     * @param height Size for height
     * @return Self with parameter imageHeight changed
     */
    public T imageHeight(int height) {
        this.imageHeight = height;
        return self();
    }
    
    /**
     * Updates the parameter for imageWidth.
     * @param width Size for width
     * @return Self with parameter imageWidth changed
     */    
    public T imageWidth(int width) {
        this.imageWidth = width;
        return self();
    }
    
    /**
     * Updates the parameter for xAxisRange.
     * @param xAxisRange Range of x axis
     * @return Self with parameter xAxisRange changed
     */    
    public T xAxisRange(AxisRange xAxisRange) {
        this.xAxisRange = xAxisRange;
        return self();
    }
    
    /**
     * Updates the parameter for yAxisRange.
     * @param yAxisRange Range of y axis
     * @return Self with parameter yAxisRange changed
     */     
    public T yAxisRange(AxisRange yAxisRange) {
        this.yAxisRange = yAxisRange;
        return self();
    }
    
    /**
     * Updates the parameter for xValueScale
     * @param xValueScale Scale value for x axis
     * @return Self with parameter xValueScale changed
     */
    public T xValueScale(ValueScale xValueScale) {
        this.xValueScale = xValueScale;
        return self();
    }
    
    /**
     * Updates the parameter for yValueScale
     * @param yValueScale Scale value for y axis
     * @return Self with parameter yValueScale changed
     */
    public T yValueScale(ValueScale yValueScale) {
        this.yValueScale = yValueScale;
        return self();
    }
    
    /**
     * Updates the parameter for backgroundColor
     * @param backgroundColor Color of image background
     * @return Self with parameter backgroundColor changed
     */
    public T backgroundColor(Color backgroundColor){
        this.backgroundColor = backgroundColor;
        return self();
    }
    
    /**
     * Updates the parameter for labelColor
     * @param labelColor Color of labels
     * @return Self with parameter labelColor changed
     */
    public T labelColor(Color labelColor){
        this.labelColor = labelColor;
        return self();        
    }
    
    /**
     * Updates the parameter for referenceLineColor
     * @param referenceLineColor Color of reference lines
     * @return Self with parameter referenceLineColor changed
     */
    public T referenceLineColor(Color referenceLineColor){
        this.referenceLineColor = referenceLineColor;
        return self();        
    }
    
    /**
     * Updates the parameter for labelFont
     * @param labelFont Font used for labels
     * @return Self with parameter labelFont changed
     */
    public T labelFont(Font labelFont){
        this.labelFont = labelFont;
        return self();        
    }
    
    /**
     * Updates the parameter for bottomMargin
     * @param bottomMargin Margin at bottom of image
     * @return Self with parameter bottomMargin changed
     */
    public T bottomMargin(Integer bottomMargin){
        this.bottomMargin = bottomMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for topMargin
     * @param topMargin Margin at top of image
     * @return Self with parameter topMargin changed
     */
    public T topMargin(Integer topMargin){
        this.topMargin = topMargin;        
        return self();        
    }
    
    /**
     * Changes the left margin between the border of the 
     * image and the vertical axis.
     * 
     * @param leftMargin number of pixels; can't be negative
     * @return this
     */
    public T leftMargin(Integer leftMargin){
        if (leftMargin < 0) {
            throw new IllegalArgumentException("Left margin must be positive");
        }
        this.leftMargin = leftMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for rightMargin
     * @param rightMargin Margin at top of image
     * @return Self with parameter topMargin changed
     */
    public T rightMargin(Integer rightMargin){
        this.rightMargin = rightMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for bottomAreaMargin
     * @param bottomAreaMargin Margin at bottom of area
     * @return Self with parameter bottomAreaMargin changed
     */    
    public T bottomAreaMargin(Integer bottomAreaMargin){
        this.bottomAreaMargin = bottomAreaMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for topAreaMargin
     * @param topAreaMargin Margin at top of area
     * @return Self with parameter topAreaMargin changed
     */      
    public T topAreaMargin(Integer topAreaMargin){
        this.topAreaMargin = topAreaMargin;        
        return self();        
    }
    
    /**
     * Updates the parameter for leftAreaMargin
     * @param leftAreaMargin Margin at left of area
     * @return Self with parameter leftAreaMargin changed
     */      
    public T leftAreaMargin(Integer leftAreaMargin){
        this.leftAreaMargin = leftAreaMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for rightAreaMargin
     * @param rightAreaMargin Margin at right of area
     * @return Self with parameter rightAreaMargin changed
     */      
    public T rightAreaMargin(Integer rightAreaMargin){
        this.rightAreaMargin = rightAreaMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for xLabelMargin
     * @param xLabelMargin Margin for the x axis labels
     * @return Self with parameter xLabelMargin changed
     */
    public T xLabelMargin(Integer xLabelMargin){
        this.xLabelMargin = xLabelMargin;
        return self();        
    }
    
    /**
     * Updates the parameter for yLabelMargin
     * @param yLabelMargin Margin for the y axis labels
     * @return Self with parameter yLabelMargin changed
     */    
    public T yLabelMargin(Integer yLabelMargin){
        this.yLabelMargin = yLabelMargin;
        return self();        
    }    
    
    /**
     * Gets height of image
     * @return imageHeight
     */
    public Integer getImageHeight() {
        return imageHeight;
    }

    /**
     * Gets width of image
     * @return imageWidth
     */
    public Integer getImageWidth() {
        return imageWidth;
    }

    /**
     * Gets x axis range
     * @return xAxisRange
     */
    public AxisRange getXAxisRange() {
        return xAxisRange;
    }

    /**
     * Gets y axis range
     * @return yAxisRange
     */
    public AxisRange getYAxisRange() {
        return yAxisRange;
    }

    /**
     * Gets scale value for x axis
     * @return xValueScale
     */
    public ValueScale getXValueScale() {
        return xValueScale;
    }

    /**
     * Gets scale value for y axis
     * @return yValueScale
     */
    public ValueScale getYValueScale() {
        return yValueScale;
    }
  
    /**
     * Gets background color of image
     * @return backgroundColor
     */
    public Color getBackgroundColor(){
        return this.backgroundColor;
    }
    
    /**
     * Gets color of labels
     * @return labelColor
     */
    public Color getLabelColor(){
        return this.labelColor;
    }
    
    /**
     * Gets color of reference lines
     * @return referenceLineColor
     */
    public Color getReferenceLineColor(){
        return this.referenceLineColor;
    }
    
    /**
     * Gets font of labels
     * @return labelFont
     */
    public Font getLabelFont(){
        return this.labelFont;
    }
    
    /**
     * Gets bottom margin
     * @return bottomMargin
     */
    public Integer getBottomMargin(){
        return this.bottomMargin;
    }
    
    /**
     * Gets top margin
     * @return topMargin
     */
    public Integer getTopMargin(){
        return this.topMargin;
    }
    
    /**
     * Gets left margin
     * @return leftMargin
     */
    public Integer getLeftMargin(){
        return this.leftMargin;
    }
    
    /**
     * Gets right margin
     * @return rightMargin
     */
    public Integer getRightMargin(){
        return this.rightMargin;
    }
    
    /**
     * Gets bottom area margin
     * @return bottomAreaMargin
     */
    public Integer getBottomAreaMargin(){
        return this.bottomAreaMargin;
    }
    
    /**
     * Gets top area margin
     * @return topAreaMargin
     */
    public Integer getTopAreaMargin(){
        return this.topAreaMargin;
    }
    
    /**
     * Gets left area margin
     * @return leftAreaMargin
     */
    public Integer getLeftAreaMargin(){
        return this.leftAreaMargin;
    }
    
    /**
     * Gets right area margin
     * @return rightAreaMargin
     */
    public Integer getRightAreaMargin(){
        return this.rightAreaMargin;
    }
    
    /**
     * Gets margin for x label
     * @return xLabelMargin
     */
    public Integer getXLabelMargin(){
        return this.xLabelMargin;
    }
    
    /**
     * Gets margin for y label
     * @return yLabelMargin
     */
    public Integer getYLabelMargin(){
        return this.yLabelMargin;
    }
}