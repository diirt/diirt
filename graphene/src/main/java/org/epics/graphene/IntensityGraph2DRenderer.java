/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
/**
 *
 * @author carcassi
 */
public class IntensityGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate>{

    private ValueColorScheme colorScheme;
    
    private boolean rangeFromDataset = true;
    private double startPlotX = java.lang.Double.MIN_VALUE;
    private double endPlotX = java.lang.Double.MAX_VALUE;
    private double startPlotY = java.lang.Double.MIN_VALUE;
    private double endPlotY = java.lang.Double.MAX_VALUE;
    
    private double integratedMinX = java.lang.Double.MAX_VALUE;
    private double integratedMinY = java.lang.Double.MAX_VALUE;
    private double integratedMaxX = java.lang.Double.MIN_VALUE;
    private double integratedMaxY = java.lang.Double.MIN_VALUE;

    public IntensityGraph2DRenderer(int imageWidth, int imageHeight) {
        super(imageWidth, imageHeight); 
    }

    public IntensityGraph2DRenderer() {
        this(300, 200);
    }
    
    public double getEndPlotX() {
        return endPlotX;
    }

    public double getEndPlotY() {
        return endPlotY;
    }

    public double getIntegratedMaxX() {
        return integratedMaxX;
    }

    public double getIntegratedMaxY() {
        return integratedMaxY;
    }

    public double getIntegratedMinX() {
        return integratedMinX;
    }

    public double getIntegratedMinY() {
        return integratedMinY;
    }

    public double getStartPlotX() {
        return startPlotX;
    }

    public double getStartPlotY() {
        return startPlotY;
    }
    
    public void update(IntensityGraph2DRendererUpdate update) {
        super.update(update);
        if (update.isRangeFromDataset() != null) {
            rangeFromDataset = update.isRangeFromDataset();
        }
        if (update.getStartX() != null) {
            startPlotX = update.getStartX();
        }
        if (update.getStartY() != null) {
            startPlotY = update.getStartY();
        }
        if (update.getEndX() != null) {
            endPlotX = update.getEndX();
        }
        if (update.getEndY() != null) {
            endPlotY = update.getEndY();
        }
        
    }
    
    //Working on: Replacing methods for calculating graph area with super class methods.
    //Working on: Making the drawing of cells more generic / able to draw with large quantities of data.
    public void draw(Graphics2D g, Cell2DDataset data) {
        
        
        // Retain the integrated min/max
        integratedMinX = java.lang.Double.isNaN(data.getXRange().getMinimum().doubleValue()) ? integratedMinX : Math.min(integratedMinX, data.getXRange().getMinimum().doubleValue());
        integratedMinY = java.lang.Double.isNaN(data.getYRange().getMinimum().doubleValue()) ? integratedMinY : Math.min(integratedMinY, data.getYRange().getMinimum().doubleValue());
        integratedMaxX = java.lang.Double.isNaN(data.getXRange().getMaximum().doubleValue()) ? integratedMaxX : Math.max(integratedMaxX, data.getXRange().getMaximum().doubleValue());
        integratedMaxY = java.lang.Double.isNaN(data.getYRange().getMaximum().doubleValue()) ? integratedMaxY : Math.max(integratedMaxY, data.getYRange().getMaximum().doubleValue());
        
        // Determine range of the plot.
        // If no range is set, use the one from the dataset
        double startXPlot;
        double startYPlot;
        double endXPlot;
        double endYPlot;
        if (rangeFromDataset) {
            startXPlot = integratedMinX;
            startYPlot = integratedMinY;
            endXPlot = integratedMaxX;
            endYPlot = integratedMaxY;
        } else {
            startXPlot = startPlotX;
            startYPlot = startPlotY;
            endXPlot = endPlotX;
            endYPlot = endPlotY;
        }
        int margin = 3;

        // Compute axis
        ValueAxis xAxis = ValueAxis.createAutoAxis(startXPlot, endXPlot, Math.max(2, super.getImageWidth() / 60));
        ValueAxis yAxis = ValueAxis.createAutoAxis(startYPlot, endYPlot, Math.max(2, super.getImageHeight() / 60));
        HorizontalAxisRenderer xAxisRenderer = new HorizontalAxisRenderer(xAxis, margin, g);
        VerticalAxisRenderer yAxisRenderer = new VerticalAxisRenderer(yAxis, margin, g);

        // Compute graph area
        int xStartGraph = yAxisRenderer.getAxisWidth();
        int yEndGraph = super.getImageHeight() - xAxisRenderer.getAxisHeight();
       
        
        // Draw reference lines
        
        this.g = g;
        calculateRanges(data.getXRange(), data.getYRange());
        drawBackground();
        calculateLabels();
        calculateGraphArea();        
        drawGraphArea();
        //Set color scheme
        colorScheme = ValueColorSchemes.grayScale(data.getStatistics());

       
        ///////////////////////////////////////////////////////////////////////
        
        int countY = 0;
        int countX = 0;

        int xWidthTotal = (int)super.xPlotCoordWidth;
        int yHeightTotal = (int)super.yPlotCoordHeight;
        int xRange = data.getXBoundaries().getInt(data.getXCount()) - data.getXBoundaries().getInt(0);
        int yRange = data.getYBoundaries().getInt(data.getYCount()) - data.getYBoundaries().getInt(0);
        double initCellHeights = ((data.getYBoundaries().getDouble(1) - data.getYBoundaries().getDouble(0))*yHeightTotal)/yRange;
        int initCellHeight = (int) (Math.floor(initCellHeights));
        if ((initCellHeights - initCellHeight) > 0.5)
        {
            initCellHeight++;
        }
        int yPosition = yEndGraph - initCellHeight;
        while (countY <= data.getYBoundaries().getDouble(data.getYCount()-1))
        {
            countX = 0;
            int xPosition = xStartGraph;
            double cellHeights = ((data.getYBoundaries().getDouble(countY+1) - data.getYBoundaries().getDouble(countY))*yHeightTotal)/yRange;
            int cellHeight = (int) (Math.floor(cellHeights));
            if (cellHeights - cellHeight > 0.5)
            {
                cellHeight++;
            }
            while (countX <= data.getXBoundaries().getDouble(data.getXCount()-1))
            {
                double cellWidths = ((data.getXBoundaries().getDouble(countX+1)-data.getXBoundaries().getDouble(countX))*xWidthTotal)/xRange;
                int cellWidth = (int) (Math.floor(cellWidths));
                if (cellWidths - cellWidth > 0)
                {
                    cellWidth++;
                }
                g.setColor(new Color(colorScheme.colorFor(data.getValue(countX, countY))));
                g.fillRect(xPosition, yPosition, cellWidth, cellHeight);
                xPosition = xPosition + cellWidth;
                countX++;
            }
            yPosition = yPosition - cellHeight;
            countY++;
        }

    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new IntensityGraph2DRendererUpdate();
    }
}
