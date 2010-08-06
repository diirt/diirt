/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

/**
 *
 * @author carcassi
 */
public interface EDoubleArray extends Array<Double>, Alarm, Time, Display<Double> {
    double[] getArray();
}
