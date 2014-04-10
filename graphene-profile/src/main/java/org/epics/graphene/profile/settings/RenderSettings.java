/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.settings;

import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.profile.ProfileGraph2D;

/**
 * Collection of settings associated with rendering a graph for a profiler.
 * 
 * @author asbarber
 */
public class RenderSettings implements Settings{
    
    //Data Members
    //--------------------------------------------------------------------------
    private Graph2DRendererUpdate   update;
    private String                  updateDescription;
    
    private boolean                 bufferInLoop = false;
    private ProfileGraph2D          profiler;
    //--------------------------------------------------------------------------
    
    
    /**
     * Constructs a settings objects for rendering.
     * @param profiler object that these are the render settings for
     */
    public RenderSettings(ProfileGraph2D profiler){
        if (profiler == null){
            throw new IllegalArgumentException("Use a non-null profiler");
        }
        
        this.profiler = profiler;
    }
    

    //Setters
    //--------------------------------------------------------------------------
    
    /**
     * Sets whether the image buffer is created within the render loop or beforehand.
     * Used in saving statistics to the CSV log file.
     * 
     * @param bufferInLoop whether the image buffer is created in the render loop
     */    
    public void setBufferInLoop(boolean bufferInLoop){
        this.bufferInLoop = bufferInLoop;
    }

    /**
     * Sets the update to be applied to the renderer with a description
     * corresponding to the update.
     * Requires the update to be non-null.
     * @param updateDescription description of the update
     * @param update update to be applied to the renderer
     */
    public void setUpdate(String updateDescription, Graph2DRendererUpdate update){
        if (update == null){
            throw new IllegalArgumentException("Invalid update");
        }
        
        this.update = update;
        this.updateDescription = updateDescription;
    }
    
    /**
     * Looks for a corresponding update based on the description,
     * and does nothing if no update is found, else updates.
     * @param updateDescription update description to search for to apply
     */
    public void setUpdate(String updateDescription){
        Object tmp = profiler.getVariations().get(updateDescription);

        //Will not update
        if (tmp == null){
            return;
        }
        
        setUpdate(updateDescription, (Graph2DRendererUpdate) tmp);
    }    
    
    //--------------------------------------------------------------------------

    
    //Getters
    //--------------------------------------------------------------------------
    
    /**
     * Gets whether the image buffer is created within the render loop or beforehand.
     * Used in saving statistics to the CSV log file.
     * 
     * @return whether the image buffer is created in the render loop
     */
    public boolean getBufferInLoop(){
        return this.bufferInLoop;
    }

    /**
     * Gets the update to be applied to the renderer.
     * @return update to be applied, null if no update
     */
    public Graph2DRendererUpdate getUpdate(){
        return update;
    }
    
    /**
     * Gets the update of the description.
     * @return update description to be applied, empty string if no description
     */
    public String getUpdateDescription(){
        if (updateDescription == null) return "";
        
        return this.updateDescription;
    }    

    //--------------------------------------------------------------------------

    
    //FORMATS FOR OUTPUT FILES
    //--------------------------------------------------------------------------
    
    /**
     * List of headers for the data members.
     * @return header data fields
     */      
    @Override
    public Object[] getTitle() {
        return new Object[]{
            "Update Applied"
        };
    }

    /**
     * List of headers for the data members.
     * @return header data fields
     */      
    @Override
    public Object[] getOutput() {
        return new Object[]{
            getUpdateDescription()
        };
    }
    
    //--------------------------------------------------------------------------    
}
