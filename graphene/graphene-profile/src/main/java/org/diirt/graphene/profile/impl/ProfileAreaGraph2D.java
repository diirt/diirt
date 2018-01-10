/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import org.diirt.graphene.AreaGraph2DRenderer;
import org.diirt.graphene.Cell1DDataset;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.graphene.profile.utils.DatasetFactory;

/**
 * Handles profiling for <code>AreaGraph2DRenderer</code>.
 * Takes a <code>Cell1DDataset</code> dataset and repeatedly renders
 * through a <code>Cell1DDataset</code>.
 *
 * @author asbarber
 */
public class ProfileAreaGraph2D extends ProfileGraph2D<AreaGraph2DRenderer, Cell1DDataset>{

    @Override
    protected Cell1DDataset getDataset() {
        return DatasetFactory.makeCell1DGaussianRandomData(getNumDataPoints());
    }

    @Override
    protected AreaGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new AreaGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(Graphics2D graphics, AreaGraph2DRenderer renderer, Cell1DDataset data) {
        renderer.draw(graphics, data);
    }

    @Override
    public LinkedHashMap<String, Graph2DRendererUpdate> getVariations() {
        LinkedHashMap<String, Graph2DRendererUpdate> map = new LinkedHashMap<>();

        map.put("None", null);

        return map;
    }

    @Override
    public String getGraphTitle() {
        return "AreaGraph2D";
    }

}
