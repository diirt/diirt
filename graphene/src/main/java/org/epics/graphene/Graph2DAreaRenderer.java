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
        ListNumber xTicks = data.getXTicks();
        for (int i = 0; i < xTicks.size(); i++) {
            Shape line = new Line2D.Double(xTicks.getDouble(i), data.getStartY(), xTicks.getDouble(i), data.getEndY());
            g.draw(line);
        }
        ListNumber yTicks = data.getYTicks();
        for (int i = 0; i < yTicks.size(); i++) {
            Shape line = new Line2D.Double(data.getStartX(), yTicks.getDouble(i), data.getEndX(), yTicks.getDouble(i));
            g.draw(line);
        }

        // Draw Y labels
        //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setColor(data.getYTicksFontColor());
        g.setFont(data.getYTicksFont());
        FontMetrics metrics = g.getFontMetrics();
        for (int i = 0; i < data.getYTicksLabels().size(); i++) {
            String label = data.getYTicksLabels().get(i);
            int halfHeight = (metrics.getAscent()) / 2 - 1;
            int labelWidth = metrics.stringWidth(label);
            g.drawString(label, (float) (data.getStartX() - labelWidth - data.getYTickMargin() - data.getYTickSize()), (float) Math.floor(yTicks.getDouble(i) + halfHeight));
        }
        
    }

}
