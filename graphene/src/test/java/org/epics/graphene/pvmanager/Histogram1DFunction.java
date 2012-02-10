/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.pvmanager;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import org.epics.graphene.*;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.data.ValueUtil;

/**
 *
 * @author carcassi
 */
public class Histogram1DFunction extends Function<VImage> {
    
    private Function<List<VDouble>> argument;
    private Dataset1D dataset = new Dataset1DArray(1000000);
    private Histogram1D histogram = Histograms.createHistogram(dataset);
    private Histogram1DRenderer renderer = new Histogram1DRenderer();
    private VImage previousImage;

    public Histogram1DFunction(Function<List<VDouble>> argument) {
        this.argument = argument;
    }

    @Override
    public VImage getValue() {
        List<VDouble> newData = argument.getValue();
        if (newData.isEmpty() && previousImage != null)
            return previousImage;
        
        Dataset1DUpdater update = dataset.update();
        for (VDouble vDouble : newData) {
            update.addData(vDouble.getValue());
        }
        update.commit();
        
        histogram = Histograms.createHistogram(dataset);
        histogram.update(new Histogram1DUpdate()
                .imageHeight(300)
                .imageWidth(400));
        
        BufferedImage image = new BufferedImage(histogram.getImageWidth(), histogram.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), histogram);
        
        previousImage = ValueUtil.toVImage(image);
        return previousImage;
    }
    
}
