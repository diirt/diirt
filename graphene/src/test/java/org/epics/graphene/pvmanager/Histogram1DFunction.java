/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.pvmanager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
class Histogram1DFunction extends Function<VImage> {
    
    private Function<List<VDouble>> argument;
    private Dataset1D dataset = new Dataset1DArray(1000000);
    private Histogram1D histogram = Histograms.createHistogram(dataset);
    private Histogram1DRenderer renderer = new Histogram1DRenderer();
    private VImage previousImage;
    private List<Histogram1DUpdate> histogramUpdates = Collections.synchronizedList(new ArrayList<Histogram1DUpdate>());

    public Histogram1DFunction(Function<List<VDouble>> argument) {
        this.argument = argument;
    }
    
    public void update(Histogram1DUpdate update) {
        // Already synchronized
        histogramUpdates.add(update);
    }

    @Override
    public VImage getValue() {
        List<VDouble> newData = argument.getValue();
        if (newData.isEmpty() && previousImage != null)
            return previousImage;
        
        // Update the dataset
        Dataset1DUpdate update = new Dataset1DUpdate();
        for (VDouble vDouble : newData) {
            update.addData(vDouble.getValue());
        }
        dataset.update(update);
        
        // Process all updates
        synchronized(histogramUpdates) {
            for (Histogram1DUpdate histogramUpdate : histogramUpdates) {
                histogram.update(histogramUpdate);
            }
            histogramUpdates.clear();
        }
        histogram.update(new Histogram1DUpdate().recalculateFrom(dataset));
        
        // If no size is set, don't calculate anything
        if (histogram.getImageHeight() == 0 && histogram.getImageWidth() == 0)
            return null;
        
        BufferedImage image = new BufferedImage(histogram.getImageWidth(), histogram.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), histogram);
        
        previousImage = ValueUtil.toVImage(image);
        return previousImage;
    }
    
}
