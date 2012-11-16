/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import javax.swing.JLabel;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class Graph2DAreaRenderer {
    public void draw(Graphics2D g, Graph2DArea data) {
        // Draw background
        g.setColor(data.getBackgroundColor());
        g.fillRect(0, 0, data.getWidth(), data.getHeight());
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw reference lines
        // When drawing the reference line, align them to the pixel
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setColor(new Color(240, 240, 240));
        ListNumber xTicks = data.getXReferences();
        for (int i = 0; i < xTicks.size(); i++) {
            Shape line = new Line2D.Double(xTicks.getDouble(i), data.getStartY(), xTicks.getDouble(i), data.getEndY());
            g.draw(line);
        }
        ListNumber yTicks = data.getYReferences();
        for (int i = 0; i < yTicks.size(); i++) {
            Shape line = new Line2D.Double(data.getStartX(), yTicks.getDouble(i), data.getEndX(), yTicks.getDouble(i));
            g.draw(line);
        }

        // Draw Y labels
        if (data.getYReferenceLabels() != null && !data.getYReferenceLabels().isEmpty()) {
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setColor(data.getYReferenceLabelColor());
            g.setFont(data.getYReferenceLabelFont());
            FontMetrics metrics = g.getFontMetrics();

            // Draw first and last label
            int[] drawRange = new int[] {(int) data.getEndY(), (int) data.getStartY()};
            int xRightLabel = (int) (data.getStartX() - data.getYReferenceLabelMargin() - 1);
            drawHorizontalReferencesLabel(g, metrics, data.getYReferenceLabels().get(0), (int) Math.floor(yTicks.getDouble(0)),
                drawRange, xRightLabel, true, false);
            drawHorizontalReferencesLabel(g, metrics, data.getYReferenceLabels().get(data.getYReferenceLabels().size() - 1), (int) Math.floor(yTicks.getDouble(data.getYReferenceLabels().size() - 1)),
                drawRange, xRightLabel, false, false);
            
            for (int i = 1; i < data.getYReferenceLabels().size() - 1; i++) {
                drawHorizontalReferencesLabel(g, metrics, data.getYReferenceLabels().get(i), (int) Math.floor(yTicks.getDouble(i)),
                    drawRange, xRightLabel, true, false);
            }
        }
        
        // Draw X labels
        if (data.getXReferenceLabels() != null && !data.getXReferenceLabels().isEmpty()) {
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setColor(data.getXReferenceLabelColor());
            g.setFont(data.getXReferenceLabelFont());
            FontMetrics metrics = g.getFontMetrics();

            // Draw first and last label
            int[] drawRange = new int[] {(int) data.getStartX(), (int) data.getEndX()};
            int yTop = (int) (data.getStartY() + data.getXReferenceLabelMargin() + 1);
            drawVerticalReferenceLabel(g, metrics, data.getXReferenceLabels().get(0), (int) Math.floor(xTicks.getDouble(0)),
                drawRange, yTop, true, false);
            drawVerticalReferenceLabel(g, metrics, data.getXReferenceLabels().get(data.getXReferenceLabels().size() - 1), (int) Math.floor(xTicks.getDouble(data.getXReferenceLabels().size() - 1)),
                drawRange, yTop, false, false);
            
            for (int i = 1; i < data.getXReferenceLabels().size() - 1; i++) {
                drawVerticalReferenceLabel(g, metrics, data.getXReferenceLabels().get(i), (int) Math.floor(xTicks.getDouble(i)),
                    drawRange, yTop, true, false);
            }
        }
        
    }
    
    private static final int MIN = 0;
    private static final int MAX = 1;
    
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
    
    private static void drawVerticalReferenceLabel(Graphics2D graphics, FontMetrics metrics, String text, int xCenter, int[] drawRange, int yTop, boolean updateMin, boolean centeredOnly) {
        // If the center is not in the range, don't draw anything
        if (drawRange[MAX] < xCenter || drawRange[MIN] > xCenter)
            return;
        
        // If there is no space, don't draw anything
        if (drawRange[MAX] - drawRange[MIN] < metrics.getHeight())
            return;
        
        Java2DStringUtilities.Alignment alignment = Java2DStringUtilities.Alignment.TOP;
        int targetX = xCenter;
        int halfWidth = metrics.stringWidth(text) / 2;
        if (xCenter < drawRange[MIN] + halfWidth) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.TOP_LEFT;
            targetX = drawRange[MIN];
        } else if (xCenter > drawRange[MAX] - halfWidth) {
            // Can't be drawn in the center
            if (centeredOnly)
                return;
            alignment = Java2DStringUtilities.Alignment.BOTTOM_RIGHT;
            targetX = drawRange[MAX];
        }

        Java2DStringUtilities.drawString(graphics, alignment, targetX, yTop, text);
        
        if (updateMin) {
            drawRange[MIN] = targetX + metrics.getHeight();
        } else {
            drawRange[MAX] = targetX - metrics.getHeight();
        }
    }

}
