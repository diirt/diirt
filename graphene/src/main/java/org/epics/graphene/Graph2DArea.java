/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

/**
 *
 * @author carcassi
 */
public interface Graph2DArea {
    
    int getBackgroundColor();
    
    int getWidth();
    int getHeight();
    
    double getStartX();
    double getEndX();
    double getStartXValue();
    double getEndXValue();
    
    double getStartY();
    double getEndY();
    double getStartYValue();
    double getEndYValue();
}
