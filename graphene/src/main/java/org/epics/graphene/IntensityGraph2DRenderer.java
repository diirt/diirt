/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.math.*;
import java.util.Arrays;
import org.epics.util.array.ListNumbers;
import org.epics.util.array.*;
/**
 *
 * @author carcassi
 */
public class IntensityGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{

    private ValueColorScheme colorScheme;

    public IntensityGraph2DRenderer(int imageWidth, int imageHeight) {
        super(imageWidth, imageHeight); 
    }

    public IntensityGraph2DRenderer() {
        this(300, 200);
    }
      
    public void update(IntensityGraph2DRendererUpdate update) {
        super.update(update);
        if(update.getDrawLegend() != null){
            drawLegend = update.getDrawLegend();
        }
    }
    
    private int legendWidth = 10,
                legendMarginToGraph = 10,
                legendMarginToEdge = 7;
    private boolean drawLegend = false;
    //Working on: Making the drawing of cells more generic / able to draw with large quantities of data.
    public void draw(Graphics2D g, Cell2DDataset data) {
        //Use super class to draw basics of graph.
        this.g = g;
        if(drawLegend){
            rightMargin = legendMarginToEdge+legendWidth+legendMarginToGraph+1;
        }
        calculateRanges(data.getXRange(), data.getYRange());
        drawBackground();
        calculateLabels();
        calculateGraphArea();        
        drawGraphArea();
        
        //Set color scheme
        
        //TODO: Incorporate a mechanism for choosing a color scheme.
        colorScheme = ValueColorSchemes.grayScale(data.getStatistics());

        double xStartGraph = super.xPlotCoordStart;
        double yEndGraph = super.yPlotCoordEnd;

        //Get graph width and height from super class.
        double xWidthTotal = super.xPlotCoordWidth;
        double yHeightTotal = super.yPlotCoordHeight;
        
        //Get range of both x and y coordinates.
        double xRange = data.getXBoundaries().getInt(data.getXCount()) - data.getXBoundaries().getInt(0);
        double yRange = data.getYBoundaries().getInt(data.getYCount()) - data.getYBoundaries().getInt(0);
        
        //Set width and height of cells to be colored in by finding the width and height for the first cell.
        double cellHeight = (yHeightTotal)/data.getYCount();
        double cellWidth = (xWidthTotal)/data.getXCount();
        
        //Draw the cells of data by filling rectangles, if the width and height are greater than one pixel.
        if(cellWidth >= 1 && cellHeight >= 1){
            drawRectangles(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
        }
        
        //Draw graph when cell width or height is smaller than one pixel.
        if(cellWidth < 1 || cellHeight < 1){
            if(cellHeight > 1){
                drawRectanglesSmallX(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
            }
            if(cellWidth > 1){
                drawRectanglesSmallY(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
            }
            if(cellWidth < 1 && cellHeight < 1){
                drawRectanglesSmallXAndY(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
            }
            
        }
        //Draw a legend, given the current data set.
        //TODO: find a way to add in labels. Preferably using methods from Graph2DRenderer (Looking at them, I don't think that's going to be possible though.)
        if(drawLegend){
            Range zRange = RangeUtil.range(0, (int)yHeightTotal);
            ListNumber dataList = ListNumbers.linearListFromRange(data.getStatistics().getMinimum().doubleValue(), data.getStatistics().getMaximum().doubleValue(), (int)yHeightTotal);
            Cell2DDataset legendData = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 1), 1, RangeUtil.range(0, (int)yHeightTotal), (int)yHeightTotal);
            drawRectangles(g,colorScheme,legendData,xStartGraph + xWidthTotal+legendMarginToGraph+1,yEndGraph,legendWidth,yHeightTotal,1,1,1, legendWidth);
            //calculateRanges(legendData.getXRange(), RangeUtil.range(data.getStatistics().getMinimum().doubleValue(), data.getStatistics().getMaximum().doubleValue()));
            //calculateLabels();
            //calculateGraphArea();
            //xAreaCoordStart = getImageWidth();
            //drawYLabels();
        }
    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new IntensityGraph2DRendererUpdate();
    }
    
    public void drawRectangles(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        int countY = 0;
        int countX;
        double yPosition = yEndGraph-yHeightTotal;
        int yPositionInt = (int)(yEndGraph-yHeightTotal);
        while (countY < data.getYCount()){
                countX = 0;
                double xPosition = xStartGraph;
                int xPositionInt = (int)xStartGraph;
                while (countX < data.getXCount()){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, data.getYCount()-1-countY))));
                    Rectangle2D.Double currentRectangle = new Rectangle2D.Double(xPositionInt, yPositionInt, (int)cellWidth+1, (int)cellHeight+1);
                    g.fill(currentRectangle);
                    xPosition = xPosition + cellWidth;
                    xPositionInt = (int)xPosition;
                    countX++;
                }
                yPosition = yPosition + cellHeight;
                yPositionInt = (int)yPosition;
                countY++;
            }
    }

    //Draws rectangles for the case when there are more x values than pixels, but no more y values than pixels.
    //Uses the first value within each pixel to choose a color.
    public void drawRectanglesSmallX(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        int countY = 0;
        double countX;
        double xDataPerBox = xWidthTotal/data.getXCount();
        double yPosition = yEndGraph-yHeightTotal;
        int yPositionInt = (int)(yEndGraph-yHeightTotal);
        while (countY < data.getYCount()){
                countX = 0;
                double xPosition = xStartGraph;
                int xPositionInt = (int)xStartGraph;
                while (xPositionInt < (int)(xStartGraph+xWidthTotal)+1){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue((int)countX, data.getYCount()-1-countY))));
                    Rectangle2D.Double rect;
                    if((yPositionInt+((int)cellHeight)+1)-(yPosition+cellHeight) < 1)
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt ,1,((int)cellHeight)+1);
                    else
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt ,1,((int)cellHeight)+2);
                    g.fill(rect);
                    countX+=xDataPerBox;
                    xPositionInt+=1;   
                }
                yPosition = yPosition + cellHeight;
                yPositionInt = (int)(yPosition);
                countY++;
            }
    }
//Same logic as drawRectanglesSmallX, but for when there are more y values than pixels.
public void drawRectanglesSmallY(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        double countY;
        int countX = 0;
        double yDataPerBox = yHeightTotal/data.getYCount();
        double xPosition = xStartGraph;
        int xPositionInt = (int)xStartGraph;
        while (countX < data.getXCount()){
                countY = 0;
                double yPosition = yEndGraph-yHeightTotal;
                int yPositionInt = (int)(yEndGraph-yHeightTotal);
                while (yPositionInt < (int)yEndGraph+1){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, data.getYCount()-1-((int)countY)))));
                    Rectangle2D.Double rect;
                    if((xPositionInt+(int)cellWidth+1)-(xPosition+cellWidth) < 1)
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt,(int)cellWidth+1,1);
                    else
                        rect = new Rectangle2D.Double(xPositionInt,yPositionInt,(int)cellWidth+2,1);
                    g.fill(rect);
                    countY+=yDataPerBox;
                    yPositionInt+=1;
                }
                xPosition = xPosition + cellWidth;
                xPositionInt = (int)xPosition;
                countX++;
            }
    }
//Draws for the case when there are both more x values and y values than pixels.
//Picks the value at approximately the top left of each pixel to set color. Skips other values within the pixel. 
public void drawRectanglesSmallXAndY(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
    double countY = 0;
    double countX;
    int yPositionInt = (int)(yEndGraph-yHeightTotal);
    double yDataPerBox = yHeightTotal/data.getYCount();
    double xDataPerBox = xWidthTotal/data.getXCount();
    int xPositionInt;
    while (yPositionInt < (int)yEndGraph+1){
        countX = 0;
        xPositionInt = (int) xStartGraph;
        while (xPositionInt < (int)(xStartGraph+xWidthTotal)+1){
            g.setColor(new Color(colorScheme.colorFor(data.getValue((int)countX, data.getYCount()-1-(int)countY))));
            Rectangle2D.Double rect;
            rect = new Rectangle2D.Double(xPositionInt,yPositionInt,1,1);
            g.fill(rect);
            countX+=xDataPerBox;
            xPositionInt+=1;
        }
        countY+=yDataPerBox;
        yPositionInt+=1;
    }
}

/*
private void calculateLabelsLegend(Range zRange){
    ValueScale zValueScale = ValueScales.linearScale();
    ValueAxis yAxis = zValueScale.references(zRange, 2, Math.max(2, getImageHeight() / 60));
    yReferenceLabels = Arrays.asList(yAxis.getTickLabels());
    yReferenceValues = new ArrayDouble(yAxis.getTickValues()); 
}*/
}