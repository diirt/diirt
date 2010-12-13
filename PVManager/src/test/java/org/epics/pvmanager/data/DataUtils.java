/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.Collections;
import java.util.List;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.TypeSupport;
import org.epics.pvmanager.util.NumberFormats;

/**
 *
 * @author carcassi
 */
public class DataUtils {

    public static VDouble createValue(TimeStamp time, double aValue) {
        return ValueFactory.newVDouble(aValue, AlarmSeverity.NONE, AlarmStatus.NONE,
                time, null, Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(3), Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static VDoubleArray createValue(TimeStamp time, double[] values) {
        return ValueFactory.newVDoubleArray(values, Collections.singletonList(values.length), AlarmSeverity.NONE, AlarmStatus.NONE,
                time, null, Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(3), Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static VIntArray createValue(TimeStamp time, int[] values) {
        return ValueFactory.newVIntArray(values, Collections.singletonList(values.length), AlarmSeverity.NONE, AlarmStatus.NONE,
                time, null, Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", NumberFormats.format(0), Double.MAX_VALUE,
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
