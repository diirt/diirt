/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * Various utility methods for runtime handling of the types defined in
 * this package.
 *
 * @author carcassi
 */
public class Utils {

    private static Collection<Class<?>> types = Arrays.<Class<?>>asList(VByteArray.class, VDouble.class,
            VDoubleArray.class, VEnum.class, VEnumArray.class, VFloatArray.class,
            VInt.class, VIntArray.class, VMultiDouble.class, VMultiEnum.class,
            VMultiInt.class, VMultiString.class, VShortArray.class,
            VStatistics.class, VString.class, VStringArray.class, VTable.class);

    /**
     * Returns the type of the object by returning the class object of one
     * of the VXxx interfaces. The getClass() methods returns the
     * concrete implementation type, which is of little use. If no
     * super-interface is found, Object.class is used.
     * 
     * @param obj an object implementing a standard type
     * @return the type is implementing
     */
    public static Class<?> typeOf(Object obj) {
        return typeOf(obj.getClass());
    }

    private static Class<?> typeOf(Class<?> clazz) {
        if (clazz.equals(Object.class))
            return Object.class;

        for (int i = 0; i < clazz.getInterfaces().length; i++) {
            Class<?> interf = clazz.getInterfaces()[i];
            if (types.contains(interf))
                return interf;
        }

        return typeOf(clazz.getSuperclass());
    }
}
