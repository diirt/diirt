/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.profile.ProfileGraph2D;

/**
 * Collection of settings associated with rendering a graph for a profiler.
 *
 * @author asbarber
 */
public class RenderSettings implements Settings{

    //Data Members
    //--------------------------------------------------------------------------
    private List<Graph2DRendererUpdate>   updates;
    private List<String>                  updateDescriptions;

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

        updates = new ArrayList<>();
        updateDescriptions = new ArrayList<>();
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
     * Looks for a corresponding update based on the description,
     * and does nothing if no update is found, else updates.
     * @param description update description to search for to apply
     */
    public void setUpdate(String description){
        setUpdate(Arrays.asList(new String[]{description}));
    }

    /**
     * Looks for corresponding updates based on the descriptions,
     * and does nothing if no update is found, else updates.
     * @param descriptions update descriptions to search for to apply
     */
    public void setUpdate(List<String> descriptions){
        if (descriptions == null){
            throw new IllegalArgumentException("Invalid descriptions");
        }

        this.updateDescriptions = descriptions;

        this.updates.clear();
        for (String description: descriptions){
            Object tmp = profiler.getVariations().get(description);

            //Will not update
            if (tmp == null){
                throw new IllegalArgumentException("Update is not supported!");
            }

            updates.add((Graph2DRendererUpdate)tmp);
        }

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
     * Gets the updates to be applied to the renderer.
     * @return updates to be applied, empty list
     */
    public List<Graph2DRendererUpdate> getUpdates(){
        return updates;
    }

    /**
     * Gets the update of the description.
     * @return update description to be applied, empty string if no description
     */
    public String getUpdateDescription(){
        String tmp = "";

        //Adds
        for (int i = 0; i < this.updateDescriptions.size() - 1; ++i){
            tmp += this.updateDescriptions.get(i) + " & ";
        }
        tmp += this.updateDescriptions.get(this.updateDescriptions.size() - 1);

        return tmp;
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
