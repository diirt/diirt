/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public interface Graph2DArea {
    
    Color getBackgroundColor();
    
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
    
    ListNumber getXTicks();
    
    ListNumber getYTicks();
    ListNumber getYTicksValues();
    List<String> getYTicksLabels();
    Font getYTicksFont();
    Color getYTicksFontColor();
    double getYTickSize();
    double getYTickMargin();
     
}
