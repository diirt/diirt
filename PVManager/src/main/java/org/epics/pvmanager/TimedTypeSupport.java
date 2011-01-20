/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.epics.pvmanager.util.TimeStamp;

/**
 *
 * @author carcassi
 */
public abstract class TimedTypeSupport<T> extends TypeSupport<T> {
  
    
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
        TimedTypeSupport<T> support = (TimedTypeSupport<T>) cachedTypeSupportFor(TimedTypeSupport.class,
                                                                                 typeClass);
        return support.extractTimestamp(value);
    }
    
    protected abstract TimeStamp extractTimestamp(T object);
    
    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends TypeSupport<T>> getTypeSupportFamily() {
        return (Class<? extends TypeSupport<T>>) TimedTypeSupport.class;
    }
}
