/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Statistics for double with alarm, timestamp and display information.
 *
 * @author carcassi
 */
public interface EStatistics extends Statistics, Alarm, Time, Display<Double> {
}
