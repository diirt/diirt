/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.image.BufferedImage;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class ScatterGraph2DRenderer extends Graph2DRenderer<Graph2DRendererUpdate> {

    private int width = 300;
    private int height = 200;

    public ScatterGraph2DRenderer(int width, int height) {
        super(width, height);
    }

    public ScatterGraph2DRenderer() {
        this(300, 200);
    }

    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new Graph2DRendererUpdate();
    }

    public void draw(Graphics2D g, Point2DDataset data) {
        int dataCount = data.getCount();
        
        // Retain the integrated min/max
        calculateRanges(data.getXStatistics(), data.getYStatistics());
        
        // Determine range of the plot.
        // If no range is set, use the one from the dataset
        double startXPlot = getXPlotRange().getMinimum().doubleValue();
        double startYPlot = getYPlotRange().getMinimum().doubleValue();
        double endXPlot = getXPlotRange().getMaximum().doubleValue();
        double endYPlot = getYPlotRange().getMaximum().doubleValue();
        int margin = 3;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);

        // Compute axis
        ValueAxis xAxis = ValueAxis.createAutoAxis(startXPlot, endXPlot, Math.max(2, width / 60));
        ValueAxis yAxis = ValueAxis.createAutoAxis(startYPlot, endYPlot, Math.max(2, height / 60));
        HorizontalAxisRenderer xAxisRenderer = new HorizontalAxisRenderer(xAxis, margin, g);
        VerticalAxisRenderer yAxisRenderer = new VerticalAxisRenderer(yAxis, margin, g);

        // Compute graph area
        int xStartGraph = yAxisRenderer.getAxisWidth();
        int xEndGraph = width - margin;
        int yStartGraph = margin;
        int yEndGraph = height - xAxisRenderer.getAxisHeight();
        int plotWidth = xEndGraph - xStartGraph;
        int plotHeight = yEndGraph - yStartGraph;

        // Draw axis
        xAxisRenderer.draw(g, 0, xStartGraph, xEndGraph, width, yEndGraph);
        yAxisRenderer.draw(g, 0, yStartGraph, yEndGraph, height, xStartGraph);


        double rangeX = endXPlot - startXPlot;
        double rangeY = endYPlot - startYPlot;

        // Scale data
        int[] scaledX = new int[dataCount];
        int[] scaledY = new int[dataCount];
        ListNumber xValues = data.getXValues();
        ListNumber yValues = data.getYValues();
        for (int i = 0; i < scaledY.length; i++) {
            scaledX[i] = (int) (xStartGraph + NumberUtil.scale(xValues.getDouble(i), startXPlot, endXPlot, plotWidth));
            scaledY[i] = (int) (height - xAxisRenderer.getAxisHeight() - NumberUtil.scale(yValues.getDouble(i), startYPlot, endYPlot, plotHeight));
        }
        
        // Draw reference lines
        g.setColor(new Color(240, 240, 240));
        int[] xTicks = xAxisRenderer.horizontalTickPositions();
        for (int xTick : xTicks) {
            g.drawLine(xTick, yStartGraph, xTick, yEndGraph);
        }
        int[] yTicks = yAxisRenderer.verticalTickPositions();
        for (int yTick : yTicks) {
            g.drawLine(xStartGraph, height - yTick, xEndGraph, height - yTick);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(Color.BLACK);
        // Make sure that the line does not go ouside the chart
        g.setClip(xStartGraph - 1, yStartGraph - 1, plotWidth + 2, plotHeight + 2);
        for (int i = 0; i < scaledY.length; i++) {
            g.draw(createShape(scaledX[i], scaledY[i]));
        }

    }
    
    private Shape createShape(double x, double y) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x-2, y);
        path.lineTo(x+2, y);
        path.moveTo(x, y-2);
        path.lineTo(x, y+2);
        return path;
    }
}
