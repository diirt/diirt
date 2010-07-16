/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

import java.util.List;

/**
 * Multi channel array.
 * <p>
 * This same type can be used in multiple circumstances where data is collected,
 * regardless of how and what of data. The number of values in the multi channel
 * array never changes.
 * <ul>
 *   <li>Synchronized array: {@link Time} returns the reference
 * time of the collected data); each element may or may not retain
 * its time information.</li>
 *   <li>Multi channel: {@link Time} returns the time of the data generation;
 * each element may or may not retain its time information</li>
 * </ul>
 *
 * @param <T> the type for the multi channel values
 * @author carcassi
 */
public interface MultiChannel<T> {

    /**
     * The list of values for all the different channels. Never null.
     *
     * @return a {@link List} of values
     */
    List<T> getValues();
}
