/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.settings;

import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.profile.ProfileGraph2D;
import org.epics.graphene.profile.utils.StopWatch;
import org.epics.graphene.profile.utils.StopWatch.TimeType;

public class RenderSettings implements Settings{
    
    private Graph2DRendererUpdate update;
    private String updateDescription;
    
    private boolean     bufferInLoop = false;

    private ProfileGraph2D profiler;
    
    
    public RenderSettings(ProfileGraph2D profiler){
        if (profiler == null){
            throw new IllegalArgumentException("Use a non-null profiler");
        }
        
        this.profiler = profiler;
    }
    

    
    /**
     * Sets whether the image buffer is created within the render loop or beforehand.
     * Used in saving statistics to the CSV log file.
     * 
     * @param bufferInLoop whether the image buffer is created in the render loop
     */    
    public void setBufferInLoop(boolean bufferInLoop){
        this.bufferInLoop = bufferInLoop;
    }
    


    public void setUpdate(String updateDescription, Graph2DRendererUpdate update){
        if (update == null){
            throw new IllegalArgumentException("Invalid update");
        }
        
        this.update = update;
        this.updateDescription = updateDescription;
    }
    
    public void setUpdate(String updateDescription){
        Object tmp = profiler.getVariations().get(updateDescription);

        //Will not update
        if (tmp == null){
            return;
        }
        
        setUpdate(updateDescription, (Graph2DRendererUpdate) tmp);
    }    
    
    //Getters
    

    
    /**
     * Gets whether the image buffer is created within the render loop or beforehand.
     * Used in saving statistics to the CSV log file.
     * 
     * @return whether the image buffer is created in the render loop
     */
    public boolean getBufferInLoop(){
        return this.bufferInLoop;
    }
    

    
    public Graph2DRendererUpdate getUpdate(){
        return update;
    }
    
    public String getUpdateDescription(){
        if (updateDescription == null) return "";
        
        return this.updateDescription;
    }    

    
    //FORMATS FOR OUTPUT FILES
    
    @Override
    public Object[] getTitle() {
        return new Object[]{
            "Update Applied"
        };
    }

    @Override
    public Object[] getOutput() {
        return new Object[]{
            getUpdateDescription()
        };
    }
}
