/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

import java.util.List;

/**
 * Multi dimensional array, which can be used for waveforms or more rich data.
 *
 * @param <T> the type for the multi channel values
 * @author carcassi
 */
public interface Array<T> {

    /**
     * The list of values for all the different channels. Never null.
     *
     * @return a {@link List} of values
     */
    List<T> getValues();
}
