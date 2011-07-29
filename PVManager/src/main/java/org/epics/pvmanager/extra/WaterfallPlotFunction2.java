/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.extra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.data.Display;
import org.epics.pvmanager.data.ValueUtil;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.data.ValueFactory;
import org.epics.pvmanager.extra.WaterfallPlotParameters.InternalCopy;
import org.epics.pvmanager.util.TimeDuration;
import org.epics.pvmanager.util.TimeStamp;

/**
 * Implements the image calculation.
 *
 * @author carcassi
 */
class WaterfallPlotFunction2 extends Function<VImage> {

    private volatile WaterfallPlotParameters.InternalCopy mutableParameters;
    private WaterfallPlotParameters.InternalCopy previousParameters;
    private BufferedImage previousBuffer;
    private VImage previousImage;
    private TimeStamp previousPlotEnd;
    private AdaptiveRange adaptiveRange;
    private List<VDoubleArray> previousValues = new LinkedList<VDoubleArray>();
    
    private DoubleArrayTimeCache doubleArrayTimeCache;

    public WaterfallPlotFunction2(DoubleArrayTimeCache doubleArrayTimeCache, WaterfallPlotParameters.InternalCopy parameters) {
        this.doubleArrayTimeCache = doubleArrayTimeCache;
        this.mutableParameters = parameters;
    }

    public InternalCopy getParameters() {
        return mutableParameters;
    }

    public void setParameters(InternalCopy parameters) {
        this.mutableParameters = parameters;
    }
    
    private VImage drawImage() {
        // Make a safe copy of the parameters
        InternalCopy parameters = mutableParameters;
        if (parameters == null)
            return null;
        
        // If parameters changed, redraw all
        boolean redrawAll = true;// parameters != previousParameters;
        
        // Calculate new end time for the plot, and how many pixels
        // should the plot scroll
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
        TimeStamp plotStart = plotEnd.minus(parameters.pixelDuration.multiplyBy(parameters.height));
        
        List<DoubleArrayTimeCache.Data> dataToPlot;
        if (redrawAll) {
            DoubleArrayTimeCache.Data data = doubleArrayTimeCache.getData(plotStart, plotEnd);
            if (data != null && data.getNArrays() != 0) {
                dataToPlot = Collections.singletonList(data);
            } else {
                dataToPlot = new ArrayList<DoubleArrayTimeCache.Data>();
            }
        } else {
            dataToPlot = doubleArrayTimeCache.newData(plotStart, previousPlotEnd, previousPlotEnd, plotEnd);
        }
        
        // If we already have an image, no new data, and the plot did not move,
        // just return the same plot!
        if (previousImage != null && nNewPixels == 0 && dataToPlot.isEmpty()) {
            return previousImage;
        }

        // Initialize adaptiveRange
        if (parameters.adaptiveRange) {
            if (adaptiveRange == null) {
                adaptiveRange = new AdaptiveRange();
            }
        } else {
            adaptiveRange = null;
        }
        
        // Scan new values
        // Should only scan if adaptive range is on and if parameters do not
        // have a fixed width
        int newMaxArraySize = 0;
        for (DoubleArrayTimeCache.Data data : dataToPlot) {
            for (int n = 0; n < data.getNArrays(); n++) {
                double[] array = data.getArray(n);
                newMaxArraySize = Math.max(newMaxArraySize, array.length);
                if (adaptiveRange != null)
                    adaptiveRange.considerValues(array);
            }
        }
        
        // TODO if adaptiveRange has changed, should redraw all!
        
        int newWidth = calculateNewWidth(previousBuffer, parameters, newMaxArraySize);
        
        
        // Create new image. Copy the old image if needed.
        BufferedImage image = new BufferedImage(newWidth, parameters.height, BufferedImage.TYPE_3BYTE_BGR);
        if (previousImage != null && !redrawAll) {
            drawOldImage(image, previousBuffer, nNewPixels, parameters);
        } else if (parameters.backgroundColor != null) {
            Graphics2D gc = image.createGraphics();
            Color background = new Color(parameters.backgroundColor);
            gc.setColor(background);
            gc.fillRect(0, 0, newWidth, parameters.height);
            gc.dispose();
        }
        
        for (DoubleArrayTimeCache.Data data : dataToPlot) {
            int pixelsFromStart = 0;
            if (data.getBegin().compareTo(plotStart) > 0) {
                pixelsFromStart = (int) (data.getBegin().durationFrom(plotStart).getNanoSec() / parameters.pixelDuration.getNanoSec());
            }
            int y = image.getHeight() - pixelsFromStart - 1;
            TimeStamp pixelStart = plotStart.plus(parameters.pixelDuration.multiplyBy(pixelsFromStart));
            if (parameters.adaptiveRange) {
                drawSection(image, parameters, null, adaptiveRange, parameters.colorScheme, data, pixelStart, parameters.pixelDuration, y);
            } else {
                drawSection(image, parameters, null, doubleArrayTimeCache.getDisplay(), parameters.colorScheme, data, pixelStart, parameters.pixelDuration, y);
            }
        }
        
        previousImage = ValueUtil.toVImage(image);
        previousBuffer = image;
        previousPlotEnd = plotEnd;
        previousParameters = parameters;
        return previousImage;
    }
    
    private static void drawSection(BufferedImage image, InternalCopy parameters,
            double[] positions, Display display, ColorScheme colorScheme, DoubleArrayTimeCache.Data data,
            TimeStamp pixelStart, TimeDuration pixelDuration, int y) {
        int usedArrays = 0;
        TimeStamp pixelEnd = pixelStart.plus(pixelDuration);
        
        // Loop until the pixel starts before the range end
        while (pixelStart.compareTo(data.getEnd()) <= 0) {
            // Get all the values in the pixel
            List<double[]> pixelValues = valuesInPixel(pixelStart, pixelEnd, data, usedArrays);
            // Determine the data to print on screen
            double[] dataToDisplay = aggregate(pixelValues);
            if (dataToDisplay == null) {
                copyPreviousLine(image, y, parameters);
            } else {
                drawLine(y, dataToDisplay, positions, display, colorScheme, image, parameters);
            }
            
            y--;
            pixelStart = pixelStart.plus(pixelDuration);
            pixelEnd = pixelStart.plus(pixelDuration);
        }
    }
    
    private static int calculateNewWidth(BufferedImage previousBuffer, InternalCopy parameters, int maxArraySize) {
        if (previousBuffer == null)
            return maxArraySize;
        
        return Math.max(previousBuffer.getWidth(), maxArraySize);
    }

    private static void copyPreviousLine(BufferedImage image, int y, InternalCopy parameters) {
        if (y < 0 || y >= image.getHeight())
            return;
        
        int previousY = y + 1;
        if (previousY < 0 || previousY >= image.getHeight())
            return;
        if (!parameters.scrollDown) {
            y = parameters.height - y - 1;
            previousY = parameters.height - previousY - 1;
        }
        if (y >= 0 && y < image.getHeight()) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, image.getRGB(x, previousY));
            }
        }
    }
    
    private static double[] aggregate(List<double[]> values) {
        if (values.isEmpty())
            return null;
        
        return values.get(values.size() - 1);
    }
    
    private static List<double[]> valuesInPixel(TimeStamp pixelStart, TimeStamp pixelEnd, DoubleArrayTimeCache.Data data, int usedArrays) {
        List<double[]> pixelValues = new ArrayList<double[]>();
        int currentArray = usedArrays;
        while (currentArray < data.getNArrays() && data.getTimeStamp(currentArray).compareTo(pixelEnd) <= 0) {
            pixelValues.add(data.getArray(currentArray));
            currentArray++;
        }
        return pixelValues;
    }
    
    private static void drawLine(int y, double[] data, double[] positions, Display display, ColorScheme colorScheme, BufferedImage image, InternalCopy parameters) {
        if (positions != null)
            throw new RuntimeException("Positions not supported yet");
        
        if (y < 0 || y >= image.getHeight())
            return;
            
        if (!parameters.scrollDown) {
            y = parameters.height - y - 1;
        }
        for (int i = 0; i < data.length; i++) {
            image.setRGB(i, y, colorScheme.color(data[i], display));
        }
    }

    @Override
    public VImage getValue() {
        return drawImage();
//        // Make a safe copy of the parameters
//        InternalCopy parameters = mutableParameters;
//        if (parameters == null)
//            return null;
//        
//        // If parameters changed, redraw all
//        boolean redrawAll = parameters != previousParameters;
//        
//        // Take new values, add them and reorder by time
//        List<VDoubleArray> newArrays = function.getValue();
//        previousValues.addAll(newArrays);
//        Collections.sort(previousValues, ValueUtil.timeComparator());
//        
//        // If no values at all, return null
//        if (previousValues.isEmpty())
//            return null;
//
//        // Initialize adaptive range
//        if (parameters.adaptiveRange) {
//            if (adaptiveRange == null) {
//                adaptiveRange = new AdaptiveRange();
//                adaptiveRange.considerValues(previousValues);
//            }
//            adaptiveRange.considerValues(newArrays);
//        } else {
//            adaptiveRange = null;
//        }
//        
//        // Calculate new image width (max from all data)
//        int newWidth = 0;
//        for (VDoubleArray vDoubleArray : newArrays) {
//            newWidth = Math.max(vDoubleArray.getArray().length, newWidth);
//        }
//        if (previousImage != null)
//            newWidth = Math.max(previousImage.getWidth(), newWidth);
//        
//        // Calculate new end time for the plot, and how many pixels
//        // should the plot scroll
//        TimeStamp plotEnd;
//        int nNewPixels;
//        if (previousPlotEnd != null) {
//            nNewPixels = (int) (TimeStamp.now().durationFrom(previousPlotEnd).getNanoSec() / parameters.pixelDuration.getNanoSec());
//            plotEnd = previousPlotEnd.plus(parameters.pixelDuration.multiplyBy(nNewPixels));
//        } else {
//            plotEnd = TimeStamp.now();
//            nNewPixels = 0;
//            redrawAll = true;
//        }
//        
//        // If we already have an image, no new data, and the plot did not move,
//        // just return the same plot!
//        if (previousImage != null && nNewPixels == 0 && newArrays.isEmpty()) {
//            return previousImage;
//        }
//        
//        
//        // Create new image. Copy the old image if needed.
//        BufferedImage image = new BufferedImage(newWidth, parameters.height, BufferedImage.TYPE_3BYTE_BGR);
//        if (previousImage != null && !redrawAll) {
//            drawOldImage(image, previousBuffer, nNewPixels, parameters);
//        } else if (parameters.backgroundColor != null) {
//            Graphics2D gc = image.createGraphics();
//            Color background = new Color(parameters.backgroundColor);
//            gc.setColor(background);
//            gc.fillRect(0, 0, newWidth, parameters.height);
//            gc.dispose();
//        }
//        
//        // Calculate the rest of the time range
//        TimeStamp plotStart = plotEnd.minus(parameters.pixelDuration.multiplyBy(parameters.height));
//        TimeStamp pixelStart = plotStart;
//        TimeStamp pixelEnd = pixelStart.plus(parameters.pixelDuration);
//        
//        // Remove old values, but keep one
//        List<VDoubleArray> oldValues = new ArrayList<VDoubleArray>();
//        for (VDoubleArray vDoubleArray : previousValues) {
//            if (vDoubleArray.getTimeStamp().compareTo(plotStart) <= 0) {
//                oldValues.add(vDoubleArray);
//            } else {
//                break;
//            }
//        }
//        if (oldValues.size() > 1) {
//            oldValues.remove(oldValues.size() - 1);
//            previousValues.removeAll(oldValues);
//        }
//        
//        // Initialize iterator. CurrentValue will hold the last value
//        // taken from the iterator, that was not within the time intervale
//        // of the current line. PreviousDisplayed holds the latest value
//        // so that it can be redisplayed in case there are no new valued.
//        Iterator<VDoubleArray> iter = previousValues.iterator();
//        VDoubleArray currentValue = null;
//        VDoubleArray previousDisplayed = null;
//        if (!oldValues.isEmpty()) {
//            currentValue = oldValues.get(oldValues.size() - 1);
//            previousDisplayed = currentValue;
//        } else {
//            currentValue = iter.next();
//        }
//        
//        // The values that fall within the pixel time range
//        List<VDoubleArray> pixelValues = new ArrayList<VDoubleArray>();
//        
//        // Loop over all lines
//        for (int line = parameters.height - 1; line >= 0; line--) {
//            // Wether this line needs to be drawn. If everything needs
//            // to be redrawn or if it's a new pixel, always draw.
//            boolean drawLine = redrawAll || line < nNewPixels;
//
//            // Accumulate values in pixel
//            pixelValues.clear();
//            while (currentValue != null) {
//                // current value is past the pixel range
//                if (currentValue.getTimeStamp().compareTo(pixelEnd) > 0) {
//                    break;
//                }
//                
//                // current value in pixel range, add
//                pixelValues.add(currentValue);
//                
//                // If it is a new value, you must draw the line again
//                drawLine = drawLine || newArrays.contains(currentValue);
//                
//                // Get new value if exists
//                if (iter.hasNext()) {
//                    currentValue = iter.next();
//                } else {
//                    currentValue = null;
//                }
//            }
//            
//            // Decide what to draw
//            VDoubleArray toDraw = aggregate(pixelValues, newWidth);
//            if (toDraw == null) {
//                toDraw = previousDisplayed;
//                drawLine = drawLine || newArrays.contains(previousDisplayed);
//            }
//            
//            // Draw only if we have a line to draw and if it needs to be drawn
//            if (toDraw != null && drawLine) {
//                if (parameters.adaptiveRange) {
//                    fillLine(line, toDraw.getArray(), adaptiveRange, parameters.colorScheme, image, parameters);
//                } else {
//                    fillLine(line, toDraw.getArray(), toDraw, parameters.colorScheme, image, parameters);
//                }
//            }
//            
//            // Get the latest value in case it should be displayed in the
//            // next line
//            if (!pixelValues.isEmpty())
//                previousDisplayed = pixelValues.get(pixelValues.size() - 1);
//            
//            // Increase pixel range
//            pixelEnd = pixelEnd.plus(parameters.pixelDuration);
//        }
//
//        previousImage = ValueUtil.toVImage(image);
//        previousBuffer = image;
//        previousPlotEnd = plotEnd;
//        previousParameters = parameters;
//        return previousImage;
    }
    
    private static VDoubleArray aggregate(List<VDoubleArray> values, int width) {
        // TODO: averaging/aggregation of arrays should be implemented somewhere else
        // for general use
        if (values.isEmpty())
            return null;
        
        if (values.size() == 1)
            return values.get(0);
        
        double[] average = new double[width];
        
        for (VDoubleArray value : values) {
            for (int i = 0; i < value.getArray().length; i++) {
                average[i] += value.getArray()[i];
            }
        }
        
        for (int i = 0; i < average.length; i++) {
            average[i] = average[i] / values.size();
        }
        
        VDoubleArray template = values.get(values.size() - 1);
        
        return ValueFactory.newVDoubleArray(average, template.getSizes(),
                template.getAlarmSeverity(), template.getAlarmStatus(),
                template.getTimeStamp(), template.getTimeUserTag(),
                template.getLowerDisplayLimit(), template.getLowerAlarmLimit(),
                template.getLowerWarningLimit(), template.getUnits(), template.getFormat(),
                template.getUpperWarningLimit(), template.getUpperAlarmLimit(),
                template.getUpperDisplayLimit(), template.getLowerCtrlLimit(), template.getUpperCtrlLimit());
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
        gc.dispose();
    }

}
