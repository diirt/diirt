/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.epics.pvmanager.util.TimeStamp;

/**
 *
 * @author carcassi
 */
public abstract class TimedTypeSupport<T> extends TypeSupport<T> {

    private static Map<Class<?>, TypeSupport<?>> typeSupports = 
        new ConcurrentHashMap<Class<?>, TypeSupport<?>>();
    private static Map<Class<?>, TypeSupport<?>> calculatedTypeSupports = 
        new ConcurrentHashMap<Class<?>, TypeSupport<?>>();
    
    public static <T> void addTypeSupport(final Class<T> typeClass, 
                                          final TypeSupport<T> typeSupport) {
        typeSupports.put(typeClass, typeSupport);
        calculatedTypeSupports.remove(typeClass);
        // TODO (carcassi) : On adding a new type support for 'typeClass', all other calculated ones are removed?
        // calculatedTypeSupport.clear(); 
    }
  
    
    /**
     * Extracts the TimeStamp of the value using the appropriate type support.
     *
     * @param <T> the type of the value
     * @param value the value from which to extract the timestamp
     * @return the extracted timestamp
     */
    public static <T> TimeStamp timestampOf(final T value) {
        @SuppressWarnings("unchecked")
        Class<T> typeClass = (Class<T>) value.getClass();
        TimedTypeSupport<T> support = (TimedTypeSupport<T>) cachedTypeSupportFor(typeClass, 
                                                                                 typeSupports,
                                                                                 calculatedTypeSupports);
        return support.extractTimestamp(value);
    }
    
    protected abstract TimeStamp extractTimestamp(T object);
}
