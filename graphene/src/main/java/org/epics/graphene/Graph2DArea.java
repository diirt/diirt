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
 * Represents the graphic details for drawing the graph area.
 * <p>
 * The graph are consists of:
 * <ul>
 *    <li>the reference lines: these lines go through the graph area, so
 *        that one can compare the values to the reference lines</li>
 *    <li>the reference labels: these labels are next to the graph area
 *        and identify which value the reference lines correspond to</li>
 * </ul>
 * <p>
 * The graph area does not currently have ticks (they are just part of the
 * reference lines) or axis lines.
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

    // For each axis, I may want to draw:
    // * lines only
    // * labels only
    // * labels + lines
    
    ListNumber getXReferences();
    ListNumber getXReferenceValues();
    boolean isXReferenceLineShown();
    boolean isXReferenceLabelShown();
    List<String> getXReferenceLabels();
    Font getXReferenceLabelFont();
    Color getXReferenceLabelColor();
    double getXReferenceLabelMargin();
    
    ListNumber getYReferences();
    ListNumber getYReferenceValues();
    List<String> getYReferenceLabels();
    Font getYReferenceLabelFont();
    Color getYReferenceLabelColor();
    int getYReferenceLabelMargin();
     
}
