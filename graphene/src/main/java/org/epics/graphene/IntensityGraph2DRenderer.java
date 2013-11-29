/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.math.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.epics.graphene.ColorScheme.BONE;
import static org.epics.graphene.ColorScheme.PINK;
import static org.epics.graphene.ColorScheme.SPRING;
import static org.epics.graphene.Graph2DRenderer.aggregateRange;
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
        if(update.getValueColorScheme() != null){
            valueColorScheme = update.getValueColorScheme();
        }
        if(update.getZLabelMargin() != null){
            zLabelMargin = update.getZLabelMargin();
        }
        if(update.getLegendWidth() != null){
            legendWidth = update.getLegendWidth();
        }
        if(update.getLegendMarginToGraph() != null){
            legendMarginToGraph = update.getLegendMarginToGraph();
        }
        if(update.getLegendMarginToEdge() != null){
            legendMarginToEdge = update.getLegendMarginToEdge();
        }
    }
    
    //legendWidth,legendMarginToGraph,legendMarginToEdge, and zLabelMargin are all lengths, in terms of pixels.
    //legendMarginToGraph corresponds to the space between the original graph and the legend.
    //legendMarginToEdge -> the space between the legend labels and the edge of the picture.
    private int legendWidth = 10,
                legendMarginToGraph = 10,
                legendMarginToEdge = 2;
    protected int zLabelMargin = 3;
    private boolean drawLegend = false;
    private Range zRange;
    private Range zAggregatedRange;
    private Range zPlotRange;
    private AxisRange zAxisRange = AxisRanges.integrated();
    private ValueScale zValueScale = ValueScales.linearScale();
    protected ListDouble zReferenceCoords;
    protected ListDouble zReferenceValues;
    protected List<String> zReferenceLabels;
    private int zLabelMaxWidth;
    
    
    private ColorScheme valueColorScheme = ColorScheme.GRAY_SCALE;
   
    public void draw(Graphics2D g, Cell2DDataset data) {
        //Use super class to draw basics of graph.
        this.g = g;
       
        calculateRanges(data.getXRange(), data.getYRange());
        drawBackground();
        calculateLabels();
        
        //Calculate all margins necessary for drawing the legend. 
        //Only do calculations if user says to draw a legend.
        if(drawLegend){
            //Find the range in order to calculate legend labels and their corresponding width.
            zRange = RangeUtil.range(data.getStatistics().getMinimum().doubleValue(),data.getStatistics().getMaximum().doubleValue());
            calculateZRange(zRange);
            calculateZLabels();
            rightMargin = legendMarginToGraph+legendWidth+zLabelMargin+zLabelMaxWidth+legendMarginToEdge;    
        }
        
        calculateGraphArea();
        
        //Wait to calculate the coordinates of the legend labels till after yPlotCoordRange is calculated.
        //Allows for the use of yPlotCoordEnd/start in calculations.
        if(drawLegend){
            if (zReferenceValues != null) {
                double[] zRefCoords = new double[zReferenceValues.size()];
                for (int i = 0; i < zRefCoords.length; i++) {
                    zRefCoords[i] = scaledZ(zReferenceValues.getDouble(i));
                }
                zReferenceCoords = new ArrayDouble(zRefCoords);
            }
        }
        drawGraphArea();
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        
        //Set color scheme
        switch(valueColorScheme){
            case GRAY_SCALE:
                colorScheme = ValueColorSchemes.grayScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;
            case JET:
                colorScheme = ValueColorSchemes.jetScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;
            case HOT:
                colorScheme = ValueColorSchemes.hotScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;
            case COOL:
                colorScheme = ValueColorSchemes.coolScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;
            case SPRING:
                colorScheme = ValueColorSchemes.springScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;          
            case BONE:
                colorScheme = ValueColorSchemes.boneScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;   
            case COPPER:
                colorScheme = ValueColorSchemes.copperScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;                 
            case PINK:
                colorScheme = ValueColorSchemes.pinkScale(RangeUtil.range((double)data.getStatistics().getMinimum(), (double)data.getStatistics().getMaximum()));
                break;                  
        }


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
            drawRectangles(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, cellHeight, cellWidth);
        }
        
        //Draw graph when cell width or height is smaller than one pixel.
        if(cellWidth < 1 || cellHeight < 1){
            if(cellHeight > 1){
                drawRectanglesSmallX(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, cellHeight, cellWidth);
            }
            if(cellWidth > 1){
                drawRectanglesSmallY(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal, cellHeight, cellWidth);
            }
            if(cellWidth < 1 && cellHeight < 1){
                drawRectanglesSmallXAndY(g, colorScheme, data, xStartGraph, yEndGraph, xWidthTotal, yHeightTotal,cellHeight, cellWidth);
            }
            
        }
        //Draw a legend, given the current data set. 
        //Don't draw if the user indicats that no legend should be drawn, or the legend width is invalid.
        if(drawLegend && legendWidth>0){
            //dataList is made by splitting the aggregated range of the z(color) data into a list of the
            //same length as the the height of the graph in pixels.
            ListNumber dataList = ListNumbers.linearListFromRange(zAggregatedRange.getMinimum().doubleValue(),zAggregatedRange.getMaximum().doubleValue(),(int)yHeightTotal);
            //legendData is a Cell2DDataset representation of dataList.
            Cell2DDataset legendData = Cell2DDatasets.linearRange(dataList, RangeUtil.range(0, 1), 1, RangeUtil.range(0, (int)yHeightTotal), (int)yHeightTotal);
            drawRectangles(g,colorScheme,legendData,xStartGraph + xWidthTotal+legendMarginToGraph+1,yEndGraph,legendWidth,yHeightTotal,1, legendWidth);
            drawZLabels();
        }
    }
    
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new IntensityGraph2DRendererUpdate();
    }
    
    public void drawRectangles(Graphics2D g, ValueColorScheme colorScheme, Cell2DDataset data, double xStartGraph, double yEndGraph,
            double xWidthTotal, double yHeightTotal, double cellHeight, double cellWidth){
        
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
            double xWidthTotal, double yHeightTotal, double cellHeight, double cellWidth){
        
        int countY = 0;
        double countX;
        /*DataPerBox is a measure of how many data points fit into each pixel.
         Because of checks in the draw method, DataPerBox is guaranteed to be greater 
         than 1. When drawing each box, xDataPerBox is the amount added to countX, which is then
         rounded down to the nearest integer to be used as an index for a data point.
         countX itself is never rounded.*/
        double xDataPerBox = (data.getXCount()-1)/xWidthTotal;
        //yPosition is never rounded, and keeps track of exactly where each box should start.
        double yPosition = yEndGraph-yHeightTotal;
        //yPositionInt is used for the actual drawing of boxes, since every box needs to be drawn
        //starting from the top left of a pixel. It is based off of yPosition. 
        int yPositionInt = (int)(yEndGraph-yHeightTotal);
        while (countY < data.getYCount()){
                countX = 0;
                int xPositionInt = (int)xStartGraph;
                while (xPositionInt < (int)(xStartGraph+xWidthTotal)+1){
                    g.setColor(new Color(colorScheme.colorFor(data.getValue((int)countX, data.getYCount()-1-countY))));
                    Rectangle2D.Double rect;
                    //check to see how far the end of the drawn box is from the end of the actual data box (due to truncation)
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
            double xWidthTotal, double yHeightTotal, double cellHeight, double cellWidth){
        
        double countY;
        int countX = 0;
        double yDataPerBox = (data.getYCount()-1)/yHeightTotal;
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
            double xWidthTotal, double yHeightTotal, double cellHeight, double cellWidth){
        double countY = 0;
        double countX;
        int yPositionInt = (int)(yEndGraph-yHeightTotal);
        double yDataPerBox = (data.getYCount()-1)/yHeightTotal;
        double xDataPerBox = (data.getXCount()-1)/xWidthTotal;
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

    protected void calculateZRange(Range zDataRange) {
        zAggregatedRange = aggregateRange(zDataRange, zAggregatedRange);
        zPlotRange = zAxisRange.axisRange(zDataRange, zAggregatedRange);
    }
    protected void calculateZLabels() {           
        // Calculate z axis references. If range is zero, use special logic
        if (!zPlotRange.getMinimum().equals(zPlotRange.getMaximum())) {
            ValueAxis zAxis = zValueScale.references(zPlotRange, 2, Math.max(2, getImageHeight() / 60));
            zReferenceLabels = Arrays.asList(zAxis.getTickLabels());
            zReferenceValues = new ArrayDouble(zAxis.getTickValues());            
        } else {
            // TODO: use something better to format the number
            zReferenceLabels = Collections.singletonList(zPlotRange.getMinimum().toString());
            zReferenceValues = new ArrayDouble(zPlotRange.getMinimum().doubleValue());            
        }
        
        // Compute z axis spacing
        int[] zLabelWidths = new int[zReferenceLabels.size()];
        zLabelMaxWidth = 0;
        for (int i = 0; i < zLabelWidths.length; i++) {
            zLabelWidths[i] = labelFontMetrics.stringWidth(zReferenceLabels.get(i));
            zLabelMaxWidth = Math.max(zLabelMaxWidth, zLabelWidths[i]);
        }
    }
    protected void drawZLabels() {
        // Draw Z labels
        ListNumber zTicks = zReferenceCoords;
        if (zReferenceLabels != null && !zReferenceLabels.isEmpty()) {
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setColor(labelColor);
            g.setFont(labelFont);
            FontMetrics metrics = g.getFontMetrics();

            // Draw first and last label
            int[] drawRange = new int[] {yAreaCoordStart, yAreaCoordEnd - 1};
            int xRightLabel = (int) (getImageWidth() - legendMarginToEdge-1);
            drawHorizontalReferencesLabel(g, metrics, zReferenceLabels.get(0), (int) Math.floor(zTicks.getDouble(0)),
                drawRange, xRightLabel, true, false);
            drawHorizontalReferencesLabel(g, metrics, zReferenceLabels.get(zReferenceLabels.size() - 1), (int) Math.floor(zTicks.getDouble(zReferenceLabels.size() - 1)),
                drawRange, xRightLabel, false, false);
            
            for (int i = 1; i < zReferenceLabels.size() - 1; i++) {
                drawHorizontalReferencesLabel(g, metrics, zReferenceLabels.get(i), (int) Math.floor(zTicks.getDouble(i)),
                    drawRange, xRightLabel, true, false);
            }
        }
    }
    private static int MIN = 0;
    private static int MAX = 1;
    private static void drawHorizontalReferencesLabel(Graphics2D graphics, FontMetrics metrics, String text, int yCenter, int[] drawRange, int xRight, boolean updateMin, boolean centeredOnly) {
        // If the center is not in the range, don't draw anything
        if (drawRange[MAX] < yCenter || drawRange[MIN] > yCenter)
            return;
        
        // If there is no space, don't draw anything
        if (drawRange[MAX] - drawRange[MIN] < metrics.getHeight())
            return;
        
        Java2DStringUtilities.Alignment alignment = Java2DStringUtilities.Alignment.RIGHT;
        int targetY = yCenter;
        int halfHeight = metrics.getAscent() / 2;
        if (yCenter < drawRange[MIN] + halfHeight) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.TOP_RIGHT;
            targetY = drawRange[MIN];
        } else if (yCenter > drawRange[MAX] - halfHeight) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.BOTTOM_RIGHT;
            targetY = drawRange[MAX];
        }

        Java2DStringUtilities.drawString(graphics, alignment, xRight, targetY, text);
        
        if (updateMin) {
            drawRange[MAX] = targetY - metrics.getHeight();
        } else {
            drawRange[MIN] = targetY + metrics.getHeight();
        }
    }
    protected final double scaledZ(double value) {
        return zValueScale.scaleValue(value, zPlotRange.getMinimum().doubleValue(), zPlotRange.getMaximum().doubleValue(), yPlotCoordEnd, yPlotCoordStart);
    }
}