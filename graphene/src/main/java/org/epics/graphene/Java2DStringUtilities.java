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
    
    public enum Alignment {
        TOP_RIGHT, TOP, TOP_LEFT,
        RIGHT, CENTER, LEFT,
        BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT;
        
        public int stringRightSide(Rectangle2D stringBounds, int x) {
            switch(this) {
                case BOTTOM:
                case CENTER:
                case TOP:
                    return x - (int) Math.floor(stringBounds.getCenterX() - 0.5);
                case BOTTOM_RIGHT:
                case RIGHT:
                case TOP_RIGHT:
                    return x - (int) Math.floor(stringBounds.getWidth() - 1.5);
            }
            throw new UnsupportedOperationException("Unsupported case");
        }
        
        public int stringBaseline(Rectangle2D stringBounds, int y) {
            switch(this) {
                case RIGHT:
                case CENTER:
                case LEFT:
                    return y - (int) Math.ceil(stringBounds.getCenterY()) + 1;
            }
            throw new UnsupportedOperationException("Unsupported case");
        }
    }
    
    public static void drawString(Graphics2D g, Alignment alignment, int x, int y, String text) {
        Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(text, g);
        g.drawString(text, alignment.stringRightSide(stringBounds, x), alignment.stringBaseline(stringBounds, y));
    }
}
