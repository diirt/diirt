/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

/**
 * Multi channel array of EDouble.
 *
 * @author carcassi
 */
public interface EMultiDouble extends MultiScalar<EDouble>, Alarm, Time, Display<Double> {

}
