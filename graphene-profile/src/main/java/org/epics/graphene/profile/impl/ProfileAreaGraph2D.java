/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import org.epics.graphene.AreaGraph2DRenderer;
import org.epics.graphene.Cell1DDataset;
import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.profile.ProfileGraph2D;
import org.epics.graphene.profile.utils.DatasetFactory;

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
