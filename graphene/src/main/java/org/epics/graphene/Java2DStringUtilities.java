/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author carcassi
 */
public class Java2DStringUtilities {
    
    public static void drawCenteredString(Graphics2D g, int x, int y, String text) {
        Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(text, g);
        int stringRightSide = x - (int) Math.floor(stringBounds.getCenterX() - 0.5);
        int stringBaseline = y - (int) Math.ceil(stringBounds.getCenterY()) + 1;
//        int stringBaseline = y + (int) Math.floor((g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent() ) / 2.0);
        g.drawString(text, stringRightSide, stringBaseline);
    }
}
