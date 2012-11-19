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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class Graph2DAreaUtil {

    public static Graph2DArea autoArea(final Graphics2D graphics, final double minX, final double maxX, final double minY, final double maxY, final int width, final int height) {
        final double margin = 3.5;
        final double labelMargin = 2;
        final Font labelFont = FontUtil.getLiberationSansRegular();
        FontMetrics metrics = graphics.getFontMetrics(labelFont);
        
        return new Graph2DArea() {

            @Override
            public Color getBackgroundColor() {
                return Color.WHITE;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public double getStartX() {
                return margin;
            }

            @Override
            public double getEndX() {
                return width - margin;
            }

            @Override
            public double getStartXValue() {
                return minX;
            }

            @Override
            public double getEndXValue() {
                return maxX;
            }

            @Override
            public double getStartY() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double getEndY() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double getStartYValue() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public double getEndYValue() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ListNumber getXReferences() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ListNumber getXReferenceValues() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isXReferenceLineShown() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isXReferenceLabelShown() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public List<String> getXReferenceLabels() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Font getXReferenceLabelFont() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Color getXReferenceLabelColor() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getXReferenceLabelMargin() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ListNumber getYReferences() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ListNumber getYReferenceValues() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public List<String> getYReferenceLabels() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Font getYReferenceLabelFont() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Color getYReferenceLabelColor() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getYReferenceLabelMargin() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
