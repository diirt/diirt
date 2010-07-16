/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Basic type for statistical information of numeric types. The methods never return
 * null, even if no connection was ever made. One <b>must always look</b>
 * at the alarm severity to be able to correctly interpret the value.
 * <p>
 * This type can be used regardless of the method used to calculate the average
 * (instances: &Sigma;<i>x<sub>i</sub>/N</i>,
 * time: &Sigma;<i>x<sub>i</sub>&Delta;t<sub>i</sub>/&Delta;t</i>,
 * time with linear interpolation, exponential backoff, ...).
 * <p>
 * Coding to {@code Statistics<T extends Number>} allows to create a client that works on statistics,
 * regardless of the type.
 *
 * @param <T> a {@link java.lang.Number} type
 * @author carcassi
 */
public interface Statistics<T extends Number> {

    /**
     * The average. Never null.
     * 
     * @return the average
     */
    T getAverage();

    /**
     * The standard deviation. Never null.
     * 
     * @return the standard deviation
     */
    T getStdDev();

    /**
     * The minimum value.
     * 
     * @return the minimum
     */
    T getMin();

    /**
     * The maximum value
     *
     * @return the maximum
     */
    T getMax();
}
