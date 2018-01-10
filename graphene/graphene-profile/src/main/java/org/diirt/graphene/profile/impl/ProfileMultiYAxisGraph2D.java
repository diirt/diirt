/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.MultiAxisLineGraph2DRenderer;
import org.diirt.graphene.MultiAxisLineGraph2DRendererUpdate;
import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.ReductionScheme;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.graphene.profile.utils.DatasetFactory;

/**
 * Handles profiling for <code>MultiAxisLineGraph2DRenderer</code>.
 * Takes a <code>Point2DDataset</code> dataset and repeatedly renders
 * through a <code>Point2DDataset</code>.
 *
 * @author asbarber
 */
public class ProfileMultiYAxisGraph2D extends ProfileGraph2D<MultiAxisLineGraph2DRenderer, List<Point2DDataset>>{
    private int numGraphs = 3;


    //Dataset Sepcific
    //--------------------------------------------------------------------------

    /**
     * Gets the number of graphs for the renderer.
     * @return number of graphs being rendered
     */
    public int getNumGraphs(){
        return this.numGraphs;
    }

    /**
     * Sets the number of graphs for the renderer.
     * Must be a value greater than zero.
     * @param numGraphs number of graphs being rendered
     */
    public final void setNumGraphs(int numGraphs){
        if (numGraphs <= 0){
            throw new IllegalArgumentException("Invalid number of graphs");
        }

        this.numGraphs = numGraphs;

        this.createDatasetMessage();
    }

    @Override
    public void setNumDataPoints(int numData){
        super.setNumDataPoints(numData);

        this.createDatasetMessage();
    }

    /**
     * Creates a dataset message to output the number of graphs.
     */
    public final void createDatasetMessage(){
        super.getSaveSettings().setDatasetMessage(getNumDataPoints() + " & " + numGraphs + "graphs");
    }

    //--------------------------------------------------------------------------


    //Superclass
    //--------------------------------------------------------------------------

    @Override
    protected List<Point2DDataset> getDataset() {
        List<Point2DDataset> data = new ArrayList<>();

        for (int i = 0; i < numGraphs; ++i){
            data.add(DatasetFactory.makePoint2DGaussianRandomData(super.getNumDataPoints()));
        }

        return data;
    }

    @Override
    protected MultiAxisLineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new MultiAxisLineGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(Graphics2D graphics, MultiAxisLineGraph2DRenderer renderer, List<Point2DDataset> data) {
        renderer.draw(graphics, data);
    }

    @Override
    public LinkedHashMap<String, Graph2DRendererUpdate> getVariations() {
        LinkedHashMap<String, Graph2DRendererUpdate> map = new LinkedHashMap<>();

        map.put("None", null);
        map.put("Nearest Neighbor Interpolation", new MultiAxisLineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        map.put("First Max Min Last Reduction", new MultiAxisLineGraph2DRendererUpdate().dataReduction(ReductionScheme.FIRST_MAX_MIN_LAST));
        map.put("No Data Reduction", new MultiAxisLineGraph2DRendererUpdate().dataReduction(ReductionScheme.NONE));

        return map;
    }

    @Override
    public String getGraphTitle() {
        return "MultiYAxisGraph2D";
    }

    //--------------------------------------------------------------------------
}
