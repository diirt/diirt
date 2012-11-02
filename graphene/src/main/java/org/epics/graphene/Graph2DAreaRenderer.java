/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author carcassi
 */
public class Graph2DAreaRenderer {
    public void draw(Graphics2D g, Graph2DArea data) {
        g.setColor(new Color(data.getBackgroundColor(), true));
        g.fillRect(0, 0, data.getWidth(), data.getHeight());
    }

}
