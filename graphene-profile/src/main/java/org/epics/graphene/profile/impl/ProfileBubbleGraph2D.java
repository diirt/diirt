/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import org.epics.graphene.BubbleGraph2DRenderer;
import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.Point3DWithLabelDataset;
import org.epics.graphene.profile.ProfileGraph2D;
import org.epics.graphene.profile.utils.DatasetFactory;

public class ProfileBubbleGraph2D extends ProfileGraph2D<BubbleGraph2DRenderer, Point3DWithLabelDataset>{

    @Override
    protected Point3DWithLabelDataset getDataset() {
        return DatasetFactory.makePoint3DWithLabelGaussianRandomData(super.getNumDataPoints());
    }

    @Override
    protected BubbleGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        return new BubbleGraph2DRenderer(imageWidth, imageHeight);
    }

    @Override
    protected void render(Graphics2D graphics, BubbleGraph2DRenderer renderer, Point3DWithLabelDataset data) {
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
        return "BubbleGraph2D";
    }
    
}
