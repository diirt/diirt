/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.Graphics2D;
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
class WaterfallPlotFunction extends Function<VImage> {

    private final Function<List<VDoubleArray>> function;
    private volatile WaterfallPlotParameters.InternalCopy mutableParameters;
    private WaterfallPlotParameters.InternalCopy previousParameters;
    private BufferedImage previousBuffer;
    private VImage previousImage;
    private TimeStamp previousPlotStart;
    private TimeStamp previousPlotEnd;
    private AdaptiveRange adaptiveRange;
    private List<VDoubleArray> previousValues = new LinkedList<VDoubleArray>();

    public WaterfallPlotFunction(Function<List<VDoubleArray>> function, WaterfallPlotParameters.InternalCopy parameters) {
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
        
        // If parameters changed, redraw all
        boolean redrawAll = parameters != previousParameters;
        
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
        
        TimeStamp plotEnd;
        int nNewPixels;
        if (previousPlotEnd != null) {
            nNewPixels = (int) (TimeStamp.now().durationFrom(previousPlotEnd).getNanoSec() / parameters.pixelDuration.getNanoSec());
            plotEnd = previousPlotEnd.plus(parameters.pixelDuration.multiplyBy(nNewPixels));
        } else {
            plotEnd = TimeStamp.now();
            nNewPixels = 0;
            redrawAll = true;
        }
        
        // If we already have an image, no new data, and the plot did not move,
        // just return the same plot!
        if (previousImage != null && nNewPixels == 0 && newArrays.isEmpty()) {
            return previousImage;
        }
        
        BufferedImage image = new BufferedImage(newWidth, parameters.height, BufferedImage.TYPE_3BYTE_BGR);
        if (previousImage != null && !redrawAll) {
            drawOldImage(image, previousBuffer, nNewPixels, parameters);
        }
        
        TimeStamp plotStart = plotEnd.minus(parameters.pixelDuration.multiplyBy(parameters.height));
        
        TimeStamp pixelStart = plotStart;
        TimeStamp pixelEnd = plotStart.plus(parameters.pixelDuration);
        
        // Remove old values, but keep one
        List<VDoubleArray> oldValues = new ArrayList<VDoubleArray>();
        for (VDoubleArray vDoubleArray : previousValues) {
            if (vDoubleArray.getTimeStamp().compareTo(plotStart) <= 0) {
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
        VDoubleArray previousDisplayed = null;
        if (!oldValues.isEmpty()) {
            currentValue = oldValues.get(oldValues.size() - 1);
            previousDisplayed = currentValue;
        } else {
            currentValue = iter.next();
        }
        
        List<VDoubleArray> pixelValues = new ArrayList<VDoubleArray>();
        for (int line = parameters.height - 1; line >= 0; line--) {
            // Accumulate values in pixel
            boolean drawLine = redrawAll || line < nNewPixels;
            pixelValues.clear();
            while (currentValue != null) {
                // current value is past the pixel range
                if (currentValue.getTimeStamp().compareTo(pixelEnd) > 0) {
                    break;
                }
                
                // current value in pixel range, add
                pixelValues.add(currentValue);
                
                // If it is a new value, you must draw the line again
                drawLine = drawLine || newArrays.contains(currentValue);
                
                // Get new value if exists
                if (iter.hasNext()) {
                    currentValue = iter.next();
                } else {
                    currentValue = null;
                }
            }
            
            VDoubleArray toDisplay = aggregate(pixelValues);
            if (toDisplay == null) {
                toDisplay = previousDisplayed;
                drawLine = drawLine || newArrays.contains(previousDisplayed);
            }
            if (toDisplay != null && drawLine) {
                if (parameters.adaptiveRange) {
                    fillLine(line, toDisplay.getArray(), adaptiveRange, parameters.colorScheme, image, parameters);
                } else {
                    fillLine(line, toDisplay.getArray(), toDisplay, parameters.colorScheme, image, parameters);
                }
            }
            
            if (!pixelValues.isEmpty())
                previousDisplayed = pixelValues.get(pixelValues.size() - 1);
            pixelEnd = pixelEnd.plus(parameters.pixelDuration);
        }

        previousImage = Util.toVImage(image);
        previousBuffer = image;
        previousPlotStart = plotStart;
        previousPlotEnd = plotEnd;
        previousParameters = parameters;
        return previousImage;
    }
    
    private static VDoubleArray aggregate(List<VDoubleArray> values) {
        if (values.isEmpty())
            return null;
        
        return values.get(values.size() - 1);
    }

    private static void fillLine(int y, double[] array, Display display, ColorScheme colorScheme, BufferedImage image, InternalCopy parameters) {
        if (!parameters.scrollDown) {
            y = parameters.height - y - 1;
        }
        for (int i = 0; i < array.length; i++) {
            image.setRGB(i, y, colorScheme.color(array[i], display));
        }
    }

    private void drawOldImage(BufferedImage image, BufferedImage previousBuffer, int nNewPixels, InternalCopy parameters) {
        Graphics2D gc = image.createGraphics();
        if (parameters.scrollDown) {
            gc.drawImage(previousBuffer, 0, nNewPixels, null);
        } else {
            gc.drawImage(previousBuffer, 0, -nNewPixels, null);
        }
    }

}
