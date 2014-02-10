/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;
import org.epics.util.array.*;
import java.util.*;
/**
 *
 * @author sjdallst
 */
public class NLineGraphs2DRendererUpdate extends Graph2DRendererUpdate<NLineGraphs2DRendererUpdate>{
    private ArrayList<Double> graphBoundaries;
    private ArrayList<Double> graphBoundaryRatios;
    private HashMap<Integer, Range> IndexToRangeMap = new HashMap<Integer, Range>();
    private HashMap<Integer, Boolean> IndexToForceMap = new HashMap<Integer, Boolean>();
    private Integer marginBetweenGraphs;
    
   
    public NLineGraphs2DRendererUpdate graphBoundaries(ArrayList<Double> graphBoundaries){
        this.graphBoundaries = graphBoundaries;
        return this.self();
    }
    
    public NLineGraphs2DRendererUpdate graphBoundaryRatios(ArrayList<Double> graphBoundaryRatios){
        this.graphBoundaryRatios = graphBoundaryRatios;
        return this.self();
    }
    
    public NLineGraphs2DRendererUpdate setRanges(List<Integer> indices, List<Range> ranges){
        if(indices.size() != ranges.size()){
            throw new IllegalArgumentException("Index list is not as long as range list");
        }
        for(int i = 0; i < indices.size(); i++){
            IndexToRangeMap.put(indices.get(i),ranges.get(i));
        }
        return this.self();
    }
    
    public NLineGraphs2DRendererUpdate setForce(List<Integer> indices, List<Boolean> force){
        if(indices.size() != force.size()){
            throw new IllegalArgumentException("Index list is not as long as boolean list");
        }
        for(int i = 0; i < indices.size(); i++){
            IndexToForceMap.put(indices.get(i),force.get(i));
        }
        return this.self();
    }
    
    public NLineGraphs2DRendererUpdate marginBetweenGraphs(Integer margin){
        marginBetweenGraphs = margin;
        return this.self();
    }
    
    public ArrayList<Double> getGraphBoundaries(){
        return this.graphBoundaries;
    }
    
    public ArrayList<Double> getGraphBoundaryRatios(){
        return this.graphBoundaryRatios;
    }
    
    public HashMap<Integer, Range> getIndexToRange(){
        return IndexToRangeMap;
    }
    
    public HashMap<Integer, Boolean> getIndexToForce(){
        return IndexToForceMap;
    }
    
    public Integer getMarginBetweenGraphs(){
        return marginBetweenGraphs;
    }
}
