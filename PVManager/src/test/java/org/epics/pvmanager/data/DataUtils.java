/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.Collections;
import org.epics.pvmanager.TimeStamp;

/**
 *
 * @author carcassi
 */
public class DataUtils {

    public static VDouble createValue(TimeStamp time, double aValue) {
        return ValueFactory.newVDouble(aValue, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Collections.<String>emptyList(), time, null, Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", null, Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
    }

}
