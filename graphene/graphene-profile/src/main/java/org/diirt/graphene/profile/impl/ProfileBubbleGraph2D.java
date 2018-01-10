/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.impl;

import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import org.diirt.graphene.BubbleGraph2DRenderer;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.Point3DWithLabelDataset;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.graphene.profile.utils.DatasetFactory;

/**
 * Handles profiling for <code>BubbleGraph2DRenderer</code>.
 * Takes a <code>Point3DWithLabelDataset</code> dataset and repeatedly renders
 * through a <code>Point3DWithLabelDataset</code>.
 *
 * @author asbarber
 */
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
