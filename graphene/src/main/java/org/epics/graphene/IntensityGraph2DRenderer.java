/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;
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
    }

    //Working on: Making the drawing of cells more generic / able to draw with large quantities of data.
    public void draw(Graphics2D g, Cell2DDataset data) {
        //Use super class to draw basics of graph.
        this.g = g;
        calculateRanges(data.getXRange(), data.getYRange());
        drawBackground();
        calculateLabels();
        calculateGraphArea();        
        drawGraphArea();
        
        //Set color scheme
        colorScheme = ValueColorSchemes.grayScale(data.getStatistics());

        double xStartGraph = super.xPlotCoordStart;
        double yEndGraph = super.yPlotCoordEnd;
        
        int countY = 0;
        int countX;

        //Get graph width and height from super class.
        double xWidthTotal = super.xPlotCoordWidth;
        double yHeightTotal = super.yPlotCoordHeight;
        
        //Get range of both x and y coordinates.
        double xRange = data.getXBoundaries().getInt(data.getXCount()) - data.getXBoundaries().getInt(0);
        double yRange = data.getYBoundaries().getInt(data.getYCount()) - data.getYBoundaries().getInt(0);
        
        //Set width and height of cells to be colored in by finding the width and height for the first cell.
        double cellHeight = ((data.getYBoundaries().getDouble(1) - data.getYBoundaries().getDouble(0))*yHeightTotal)/yRange;
        double cellWidth = ((data.getXBoundaries().getDouble(1)-data.getXBoundaries().getDouble(0))*xWidthTotal)/xRange;
        double yPosition = yEndGraph - cellHeight;
        
        //Draw the cells of data by filling rectangles, if the width and height are greater than one pixel.
        if(cellWidth >= 1 && cellHeight >= 1){
            drawRectangles(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, xRange, yRange, cellHeight, cellWidth);
        }
        
        //Draw graph when cell width or height is smaller than one pixel.
        if(cellWidth < 1 || cellHeight < 1){
            while (countY <= data.getYBoundaries().getDouble(data.getYCount()-1))
            {
                countX = 0;
                double xPosition = xStartGraph;
                while (countX <= data.getXBoundaries().getDouble(data.getXCount()-1))
                {
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                    Rectangle2D.Double currentRectangle = new Rectangle2D.Double(xPosition, yPosition, cellWidth, cellHeight);
                    g.fill(currentRectangle);
                    xPosition = xPosition + cellWidth;
                    countX++;
                }
                yPosition = yPosition - cellHeight;
                countY++;
            }
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
        double yPosition = yEndGraph - cellHeight;
        while (countY <= data.getYBoundaries().getDouble(data.getYCount()-1))
            {
                countX = 0;
                double xPosition = xStartGraph;
                while (countX <= data.getXBoundaries().getDouble(data.getXCount()-1))
                {
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                    Rectangle2D.Double currentRectangle = new Rectangle2D.Double(xPosition, yPosition, cellWidth, cellHeight);
                    g.fill(currentRectangle);
                    xPosition = xPosition + cellWidth;
                    countX++;
                }
                yPosition = yPosition - cellHeight;
                countY++;
            }
    }
    //WORK IN PROGRESS. DOES NOT WORK.
    //Working on: drawing graph when cell widths are less than 1.  Current plan is to set color by adding all values contained within a pixel.
    //Cells that lie on two pixels should be added to those pixels based on what percentage of the cell lies in each pixel.
    public void drawRectanglesSmallX(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double xRange, double yRange, double cellHeight, double cellWidth){
        
        int countY = 0;
        int countX;
        double yPosition = yEndGraph - cellHeight;
        while (countY <= data.getYBoundaries().getDouble(data.getYCount()-1))
            {
                countX = 0;
                double xPosition = xStartGraph;
                while (countX <= data.getXBoundaries().getDouble(data.getXCount()-1))
                {
                    double pixelValue = 0;
                    double xPositionInitial = xPosition;
                    while(xPosition <= xPositionInitial +1){
                        if(xPosition + cellWidth <= xPositionInitial +1){
                            pixelValue+=data.getValue(countX, countY);
                        }
                        else{
                            
                        }
                    }
                    g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                    
                    xPosition = xPosition + cellWidth;
                    countX++;
                }
                yPosition = yPosition - cellHeight;
                countY++;
            }
    }
}
