/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.Collections;
import java.util.List;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.TypeSupport;

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

    public static void printArray(VMultiDouble array) {
        printArray(array.getValues());
    }

    public static void printArray(List<VDouble> array) {
        if (array == null)
            return;
        System.out.print("Array [");
        boolean first = true;
        for (VDouble value : array) {
            if (!first)
                System.out.print(",");
            first = false;
            if (value == null)
                System.out.print("null");
            else
                System.out.print(TypeSupport.timestampOf(value));// + " - " + value.getDoubleValue()[0]);
        }
        System.out.println("]");
    }
}
