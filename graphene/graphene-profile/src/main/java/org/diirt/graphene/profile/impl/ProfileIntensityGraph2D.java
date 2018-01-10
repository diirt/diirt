/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import org.diirt.graphene.IntensityGraph2DRenderer;
import org.diirt.graphene.IntensityGraph2DRendererUpdate;
import org.diirt.graphene.Cell2DDataset;
import org.diirt.graphene.GraphBuffer;
import org.diirt.graphene.Graph2DRendererUpdate;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.graphene.profile.utils.DatasetFactory;

/**
 * Handles profiling for <code>IntensityGraph2DRenderer</code>.
 * Takes a <code>Cell2DDataset</code> dataset and repeatedly renders through a <code>IntensityGraph2DRenderer</code>.
 *
 * @author asbarber
 * @author jkfeng
 * @author sjdallst
 */
public class ProfileIntensityGraph2D extends ProfileGraph2D<IntensityGraph2DRenderer, Cell2DDataset>{
    private int numXData = 100,
                numYData = 100;

    /**
     * The data member containing the image being rendered.
     */
    protected GraphBuffer graphBuffer = null;


    //Getters (Dataset Size)
    //--------------------------------------------------------------------------

    /**
     * Gets the size of the data determined by the size of x data and y data.
     * Found by treating the cells as a matrix with dimension x data size times y data size.
     * @return size of x data * size of y data
     */
    @Override
    public int getNumDataPoints(){
        return getNumXDataPoints() * getNumYDataPoints();
    }

    /**
     * Gets number of x-value data points.
     * @return number of x-value data points
     */
    public int getNumXDataPoints(){
        return numXData;
    }

    /**
     * Gets number of y-value data points.
     * @return number of y-value data points
     */
    public int getNumYDataPoints(){
        return numYData;
    }

    //--------------------------------------------------------------------------


    //Setters (Dataset Size)
    //--------------------------------------------------------------------------

    /**
     * Sets the number of x and y data points.
     * The size of and x and y is determined as the square root of the total
     * number of data points.
     * @param numData total number of data points
     */
    @Override
    public void setNumDataPoints(int numData){
        numXData = (int) Math.sqrt(numData);
        numYData = (int) Math.sqrt(numData);

        //Updates to make accurate representation of num data
        super.setNumDataPoints(numXData * numYData);

        this.createDatasetMessage();
    }

    /**
     * Sets number of x-value data points.
     * @param numXData number of x-value data points
     */
    public void setNumXDataPoints(int numXData){
        this.numXData = numXData;
        this.createDatasetMessage();
    }

    /**
     * Sets number of y-value data points.
     * @param numYData number of y-value data points
     */
    public void setNumYDataPoints(int numYData){
        this.numYData = numYData;
        this.createDatasetMessage();
    }

    //--------------------------------------------------------------------------


    //Handling 2D Dataset
    //--------------------------------------------------------------------------

    /**
     * Creates a message about the x by y dimension of the cell data.
     * This message is saved in the log file as the comment about the data set.
     */
    public final void createDatasetMessage(){
        super.getSaveSettings().setDatasetMessage(getNumXDataPoints() + "x" + getNumYDataPoints());
    }

    //--------------------------------------------------------------------------


    //Handling GraphBuffer
    //--------------------------------------------------------------------------

    @Override
    protected void preLoopAction(){
        //Data and Render Objects (Implemented in subclasses)
        data = getDataset();
        renderer = getRenderer(getResolution().getWidth(), getResolution().getHeight());

        for (Graph2DRendererUpdate u: getRenderSettings().getUpdates()){
            renderer.update((IntensityGraph2DRendererUpdate)u);
        }

        //Creates the image buffer if parameter says to set it ouside of render loop
        if (!getRenderSettings().getBufferInLoop()){
            graphBuffer = new GraphBuffer(renderer);
        }
    }

    @Override
    protected void iterationAction(){
        //Create Image if necessary
        if (getRenderSettings().getBufferInLoop()){
            graphBuffer = new GraphBuffer(renderer);
        }

        //Subclass render
        render(null, renderer, data);
    }

    /**
     * Draws the cell data in an intensity graph.
     * Primary method in the render loop.
     * @param graphics where image draws to (WILL BE PASSED AS <b>NULL</b>)
     * @param renderer what draws the image
     * @param data the cell data being drawn
     */
    @Override
    protected void render(Graphics2D graphics, IntensityGraph2DRenderer renderer, Cell2DDataset data) {
        renderer.draw(graphBuffer, data);
    }

    //--------------------------------------------------------------------------


    //IntensityGraph Specifics (Getters)
    //--------------------------------------------------------------------------

    /**
     * Gets a set of random Gaussian 2D cell data.
     * @return the appropriate <code>Cell2DDataset</code> data
     */
    @Override
    protected Cell2DDataset getDataset() {
        return DatasetFactory.makeCell2DGaussianRandomData(getNumXDataPoints(), getNumYDataPoints());
    }

    /**
     * Returns the renderer used in the render loop.
     * The cell data is rendered by a <code>IntensityGraph2DRenderer</code>.
     * @param imageWidth width of rendered image in pixels
     * @param imageHeight height of rendered image in pixels
     * @return an intensity graph to draw the data
     */
    @Override
    protected IntensityGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        IntensityGraph2DRenderer renderer = new IntensityGraph2DRenderer(imageWidth, imageHeight);

        return renderer;
    }

    /**
     * Returns the name of the graph being profiled.
     * @return <code>IntensityGraph2DRenderer</code> title
     */
    @Override
    public String getGraphTitle() {
        return "IntensityGraph2D";
    }

    @Override
    public LinkedHashMap<String, Graph2DRendererUpdate> getVariations() {
        LinkedHashMap<String, Graph2DRendererUpdate> map = new LinkedHashMap<>();

        map.put("None", null);
        map.put("Not Draw Legend", new IntensityGraph2DRendererUpdate().drawLegend(false));

        return map;
    }

    //--------------------------------------------------------------------------
}