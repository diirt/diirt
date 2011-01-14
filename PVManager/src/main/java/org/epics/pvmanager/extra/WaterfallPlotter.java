/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.Display;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.data.ValueFactory;

/**
 *
 * @author carcassi
 */
class WaterfallPlotter extends Function<VImage> {

    private final Function<List<VDoubleArray>> function;
    private final WaterfallPlotParameters parameters = new WaterfallPlotParameters();
    private BufferedImage previousBuffer;
    private VImage previousImage;

    public WaterfallPlotter(Function<List<VDoubleArray>> function) {
        this.function = function;
    }

    @Override
    public VImage getValue() {
        List<VDoubleArray> newArrays = function.getValue();
        if (newArrays.isEmpty())
            return previousImage;

        int newWidth = 0;
        for (VDoubleArray vDoubleArray : newArrays) {
            newWidth = Math.max(vDoubleArray.getArray().length, newWidth);
        }
        if (previousImage != null)
            newWidth = Math.max(previousImage.getWidth(), newWidth);

        BufferedImage image = new BufferedImage(newWidth, parameters.maxHeight, BufferedImage.TYPE_3BYTE_BGR);
        if (previousImage != null && newArrays.size() < parameters.maxHeight) {
            Graphics2D gc = image.createGraphics();
            gc.drawImage(previousBuffer, 0, newArrays.size(), null);
        }

        int line = newArrays.size();
        for (VDoubleArray vDoubleArray : newArrays) {
            line--;
            if (line < parameters.maxHeight)
                fillLine(line, vDoubleArray.getArray(), vDoubleArray, parameters.colorScheme, image);
        }

        byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        previousImage = ValueFactory.newVImage(image.getHeight(), image.getWidth(), buffer);
        previousBuffer = image;
        return previousImage;
    }

    private static void fillLine(int y, double[] array, Display display, ColorScheme colorScheme, BufferedImage image) {
        for (int i = 0; i < array.length; i++) {
            image.setRGB(i, y, colorScheme.color(array[i], display));
        }
    }

}
