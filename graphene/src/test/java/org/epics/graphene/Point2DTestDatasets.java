/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.graphene;

import org.epics.util.array.ArrayDouble;

/**
 *
 * @author carcassi
 */
public class Point2DTestDatasets {
    public static Point2DDataset sineDataset(int nSamples, int wavelengthInSamples, double initialAngleInRad, double amplitude, double average, Range xRange) {
        double[] data = new double[nSamples];
        for(int j = 0; j < nSamples; j++){
            data[j] = amplitude * Math.sin(j * 2.0 * Math.PI / wavelengthInSamples + initialAngleInRad) + average;
        }
        
        return Point2DDatasets.lineData(xRange, new ArrayDouble(data));
    }
}
