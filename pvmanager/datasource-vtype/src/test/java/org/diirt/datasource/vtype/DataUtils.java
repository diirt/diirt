/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.epics.vtype.VDouble;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class DataUtils {

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
                System.out.print(value.getTime());// + " - " + value.getDoubleValue()[0]);
        }
        System.out.println("]");
    }
}
