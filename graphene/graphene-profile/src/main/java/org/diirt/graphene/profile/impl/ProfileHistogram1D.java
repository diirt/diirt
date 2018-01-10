/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import org.diirt.graphene.AreaGraph2DRenderer;
import org.diirt.graphene.Cell1DDatasets;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.Cell1DDataset;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Random;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumbers;

/**
 * Handles profiling for <code>Histogram1D</code>.
 * Takes a <code>Histogram1D</code> dataset and repeatedly renders through a <code>AreaGraph2DRenderer</code>.
 *
 * @author asbarber
 */
public class ProfileHistogram1D extends ProfileGraph2D<AreaGraph2DRenderer, Cell1DDataset> {

    /**
     * Generates <code>Histogram1D</code> data that can be used in rendering.
     * The data is Gaussian and random between 0 and 1.
     * @return data as a histogram
     */
    @Override
    protected Cell1DDataset getDataset() {
        int nSamples = getNumDataPoints();

        //Creates data
        Random rand = new Random(1);
        double[] data = new double[nSamples];
        for (int i = 0; i < nSamples; i++) {
            data[i] = rand.nextGaussian();
        }

        return Cell1DDatasets.datasetFrom(new ArrayDouble(data), ListNumbers.linearList(0, 1, nSamples));
    }

    /**
     * Returns the renderer used in the render loop.
     * The histogram data is rendered by a <code>AreaGraph2DRenderer</code>.
     * @param imageWidth width of rendered image in pixels
     * @param imageHeight height of rendered image in pixels
     * @return a <code>AreaGraph2DRenderer</code> associated with <code>Histogram1D</code> data
     */
    @Override
    protected AreaGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new AreaGraph2DRenderer(imageWidth, imageHeight);
    }

    /**
     * Draws the histogram in an area graph.
     * Primary method in the render loop.
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data the histogram being drawn
     */
    @Override
    protected void render(Graphics2D graphics, AreaGraph2DRenderer renderer, Cell1DDataset data) {
        renderer.draw(graphics, data);
    }

    /**
     * Returns the name of the graph being profiled.
     * @return <code>Histogram1D</code> title
     */
    @Override
    public String getGraphTitle() {
        return "Histogram1D";
    }

    /**
     * Gets the updates associated with the renderer in a map, linking a
     * description of the update to the update object.
     * @return map with description of update paired with an update
     */
    @Override
    public LinkedHashMap<String, Graph2DRendererUpdate> getVariations() {
        LinkedHashMap<String, Graph2DRendererUpdate> map = new LinkedHashMap<>();

        map.put("None", null);

        return map;
    }
}
