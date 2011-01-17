/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.epics.pvmanager.data.Array;
import org.epics.pvmanager.data.MultiScalar;
//import org.epics.pvmanager.data.Notification;
import org.epics.pvmanager.data.Scalar;
import org.epics.pvmanager.data.Statistics;
import org.epics.pvmanager.data.Time;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.util.TimeStamp;

/**
 *
 * @author carcassi
 */
public abstract class TimedTypeSupport<T> extends TypeSupport<T> {

    private static Map<Class<?>, TypeSupport<?>> typeSupports = new ConcurrentHashMap<Class<?>, TypeSupport<?>>();
    private static Map<Class<?>, TypeSupport<?>> calculatedTypeSupports = new ConcurrentHashMap<Class<?>, TypeSupport<?>>();
    
    private static boolean installed = false;

    /**
     * Installs type support. This should only be called by either DataSources
     * or ExpressionLanguage libraries that require support for these types.
     */
    public static void install() {
        // Install only once
        if (installed)
            return;

        addScalar();
        addMultiScalar();
        addStatistics();
        addArray();
        addImage();

        installed = true;
    }
    

    private static void addScalar() {
        // Add support for all scalars: simply return the new value
        addTypeSupport(Scalar.class, 
                       immutableTypeSupport(Scalar.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addMultiScalar() {
        // Add support for all multi scalars: simply return the new value
        addTypeSupport(MultiScalar.class, 
                       immutableTypeSupport(MultiScalar.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addArray() {
        // Add support for all arrays: simply return the new value
        addTypeSupport(Array.class, 
                       immutableTypeSupport(Array.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addStatistics() {
        // Add support for statistics: simply return the new value
        addTypeSupport(Statistics.class, 
                       immutableTypeSupport(Statistics.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addImage() {
        // Add support for statistics: simply return the new value
        addTypeSupport(VImage.class, 
                       immutableTypeSupport(VImage.class),
                       typeSupports,
                       calculatedTypeSupports);
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
    
    
    // TODO (carcassi) : As any object's type support does just hardly cast the value to {@link Time}, 
    //                   why not use the 'object.getTimeStamp' method directly instead of 'TimedTypeSupport.timestampOf(object)'?
    //                   The pattern is great, but in this special case it doesn't seem necessary.
    private static <T> TimedTypeSupport<T> immutableTypeSupport(final Class<T> clazz) {
        return new TimedTypeSupport<T>() {
            @Override
            public TimeStamp extractTimestamp(final T object) {
                return ((Time) object).getTimeStamp();
            }
        };
    }
}
