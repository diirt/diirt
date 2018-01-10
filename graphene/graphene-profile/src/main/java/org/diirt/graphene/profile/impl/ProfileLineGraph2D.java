/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.LineGraph2DRenderer;
import org.diirt.graphene.LineGraph2DRendererUpdate;
import org.diirt.graphene.ReductionScheme;
import org.diirt.graphene.Point2DDataset;
import org.diirt.graphene.Graph2DRendererUpdate;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.graphene.profile.utils.DatasetFactory;

/**
 * Handles profiling for <code>LineGraph2DRenderer</code>.
 * Takes a <code>Point2DDataset</code> dataset and repeatedly renders through a <code>LineGraph2DRenderer</code>.
 *
 * @author asbarber
 */
public class ProfileLineGraph2D extends ProfileGraph2D<LineGraph2DRenderer, Point2DDataset>{

    /**
     * Gets a set of random Gaussian 2D point data.
     * @return the appropriate <code>Point2DDataset</code> data
     */
    @Override
    protected Point2DDataset getDataset() {
        return DatasetFactory.makePoint2DGaussianRandomData(getNumDataPoints());
    }

    /**
     * Returns the renderer used in the render loop.
     * The 2D point is rendered by a <code>LineGraph2DRenderer</code>.
     * @param imageWidth width of rendered image in pixels
     * @param imageHeight height of rendered image in pixels
     * @return a line graph to draw the data
     */
    @Override
    protected LineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        LineGraph2DRenderer renderer = new LineGraph2DRenderer(imageWidth, imageHeight);

        return renderer;
    }

    /**
     * Draws the 2D point data in a line graph.
     * Primary method in the render loop.
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data the 2D point data being drawn
     */
    @Override
    protected void render(Graphics2D graphics, LineGraph2DRenderer renderer, Point2DDataset data) {
        renderer.draw(graphics, data);
    }

    /**
     * Returns the name of the graph being profiled.
     * @return <code>LineGraph2DRenderer</code> title
     */
    @Override
    public String getGraphTitle() {
        return "LineGraph2D";
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
        map.put("Linear Interpolation", new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.LINEAR));
        map.put("Cubic Interpolation", new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.CUBIC));
        map.put("Nearest Neighbor Interpolation", new LineGraph2DRendererUpdate().interpolation(InterpolationScheme.NEAREST_NEIGHBOR));
        map.put("First Max Min Last Reduction", new LineGraph2DRendererUpdate().dataReduction(ReductionScheme.FIRST_MAX_MIN_LAST));
        map.put("No Data Reduction", new LineGraph2DRendererUpdate().dataReduction(ReductionScheme.NONE));

        return map;
    }
}
