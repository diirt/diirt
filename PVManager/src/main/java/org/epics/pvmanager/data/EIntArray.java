/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

/**
 *
 * @author carcassi
 */
public interface EIntArray extends Array<Integer>, Alarm, Time, Display<Integer> {
    public int[] getArray();
}
