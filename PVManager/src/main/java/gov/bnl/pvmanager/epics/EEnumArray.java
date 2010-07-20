/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface EEnumArray extends Array<String>, Enum, Alarm, Time {
    public String[] getArray();
}
