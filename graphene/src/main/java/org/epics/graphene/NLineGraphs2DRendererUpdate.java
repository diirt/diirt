/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;
import org.epics.util.array.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/**
 *
 * @author sjdallst
 */
public class NLineGraphs2DRendererUpdate extends Graph2DRendererUpdate<NLineGraphs2DRendererUpdate>{
    private ArrayList<Double> graphBoundaries;
    private ArrayList<Double> graphBoundaryRatios;
    
    public NLineGraphs2DRendererUpdate GraphBoundaries(ArrayList<Double> graphBoundaries){
        this.graphBoundaries = graphBoundaries;
        return this.self();
    }
    
    public NLineGraphs2DRendererUpdate GraphBoundaryRatios(ArrayList<Double> graphBoundaryRatios){
        this.graphBoundaryRatios = graphBoundaryRatios;
        return this.self();
    }
    
    public ArrayList<Double> getGraphBoundaries(){
        return this.graphBoundaries;
    }
    
    public ArrayList<Double> getGraphBoundaryRatios(){
        return this.graphBoundaryRatios;
    }
}
