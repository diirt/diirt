/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.Display;
import org.epics.pvmanager.data.Util;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.extra.WaterfallPlotParameters.InternalCopy;
import org.epics.pvmanager.util.TimeStamp;

/**
 *
 * @author carcassi
 */
class WaterfallPlotter extends Function<VImage> {

    private final Function<List<VDoubleArray>> function;
    private volatile WaterfallPlotParameters.InternalCopy mutableParameters;
    private BufferedImage previousBuffer;
    private VImage previousImage;
    private TimeStamp previousInitialTime;
    private TimeStamp previousFinalTime;
    private AdaptiveRange adaptiveRange;
    private List<VDoubleArray> previousValues = new LinkedList<VDoubleArray>();

    public WaterfallPlotter(Function<List<VDoubleArray>> function, WaterfallPlotParameters.InternalCopy parameters) {
        this.function = function;
        this.mutableParameters = parameters;
    }

    public InternalCopy getParameters() {
        return mutableParameters;
    }

    public void setParameters(InternalCopy parameters) {
        this.mutableParameters = parameters;
    }

    @Override
    public VImage getValue() {
        // Make a safe copy of the parameters
        InternalCopy parameters = mutableParameters;
        if (parameters == null)
            return null;
        
        // Take new values and sort them
        List<VDoubleArray> newArrays = function.getValue();

        // Initialize adaptive range
        if (parameters.adaptiveRange) {
            if (adaptiveRange == null) {
                adaptiveRange = new AdaptiveRange();
                adaptiveRange.considerValues(previousValues);
            }
            adaptiveRange.considerValues(newArrays);
        } else {
            adaptiveRange = null;
        }
        
        // XXX for now, just repaint everything
        
        // Calculate new image width (max from all data)
        int newWidth = 0;
        for (VDoubleArray vDoubleArray : newArrays) {
            newWidth = Math.max(vDoubleArray.getArray().length, newWidth);
        }
        if (previousImage != null)
            newWidth = Math.max(previousImage.getWidth(), newWidth);
        
        // Add new samples and reorder
        previousValues.addAll(newArrays);
        Collections.sort(previousValues, Util.timeComparator());
        if (previousValues.isEmpty())
            return null;

        BufferedImage image = new BufferedImage(newWidth, parameters.height, BufferedImage.TYPE_3BYTE_BGR);
        // Don't use the old image for now
//        if (previousImage != null && newArrays.size() < parameters.height) {
//            Graphics2D gc = image.createGraphics();
//            gc.drawImage(previousBuffer, 0, newArrays.size(), null);
//        }
        
        TimeStamp finalTime;
        if (previousFinalTime != null) {
            int nNewPixels = (int) (TimeStamp.now().durationFrom(previousFinalTime).getNanoSec() / parameters.pixelDuration.getNanoSec());
            finalTime = previousFinalTime.plus(parameters.pixelDuration.multiplyBy(nNewPixels));
        } else {
            finalTime = TimeStamp.now();
        }
        TimeStamp initialTime = finalTime.minus(parameters.pixelDuration.multiplyBy(parameters.height));
        
        TimeStamp pixelStart = initialTime;
        TimeStamp pixelEnd = initialTime.plus(parameters.pixelDuration);
        
        // Remove old values, but keep one
        List<VDoubleArray> oldValues = new ArrayList<VDoubleArray>();
        for (VDoubleArray vDoubleArray : previousValues) {
            if (vDoubleArray.getTimeStamp().compareTo(initialTime) <= 0) {
                oldValues.add(vDoubleArray);
            } else {
                break;
            }
        }
        if (oldValues.size() > 1) {
            oldValues.remove(oldValues.size() - 1);
            previousValues.removeAll(oldValues);
        }
        
        Iterator<VDoubleArray> iter = previousValues.iterator();
        VDoubleArray currentValue = null;
        if (!oldValues.isEmpty())
            currentValue = oldValues.get(oldValues.size() - 1);
        
        List<VDoubleArray> pixelValues = new ArrayList<VDoubleArray>();
        VDoubleArray previousDisplayed = currentValue;
        for (int line = 0; line < parameters.height; line++) {
            // Accumulate values in pixel
            pixelValues.clear();
            while (iter.hasNext() && (currentValue == null || currentValue.getTimeStamp().compareTo(pixelEnd) <= 0)) {
                pixelValues.add(currentValue);
                currentValue = iter.next();
            }
            
            VDoubleArray toDisplay = aggregate(pixelValues);
            if (toDisplay == null)
                toDisplay = previousDisplayed;
            if (toDisplay != null) {
                if (parameters.adaptiveRange) {
                    fillLine(line, toDisplay.getArray(), adaptiveRange, parameters.colorScheme, image);
                } else {
                    fillLine(line, toDisplay.getArray(), toDisplay, parameters.colorScheme, image);
                }
            }
            
            if (!pixelValues.isEmpty())
                previousDisplayed = pixelValues.get(pixelValues.size() - 1);
            pixelEnd = pixelEnd.plus(parameters.pixelDuration);
        }

        previousImage = Util.toVImage(image);
        previousBuffer = image;
        previousInitialTime = initialTime;
        previousFinalTime = finalTime;
        return previousImage;
    }
    
    private static VDoubleArray aggregate(List<VDoubleArray> values) {
        if (values.isEmpty())
            return null;
        
        return values.get(values.size() - 1);
    }

    private static void fillLine(int y, double[] array, Display display, ColorScheme colorScheme, BufferedImage image) {
        for (int i = 0; i < array.length; i++) {
            image.setRGB(i, y, colorScheme.color(array[i], display));
        }
    }

}
