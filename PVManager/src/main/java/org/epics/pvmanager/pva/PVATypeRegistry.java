/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import org.epics.pvData.pv.PVField;

/**
 *
 * @author carcassi
 */
public interface PVATypeRegistry {
    
    /**
     * Given the introspection information of a pv and the type desired,
     * returns the matching converter to create a desired type from the given pvField.
     * 
     * @param <T> the normative type
     * @param pvField the connection information
     * @param desiredType the desired normative type; can be null
     * @return null if no match is found
     */
    public <T> PVATypeConverter<? extends T> findConverter(PVField pvField, Class<T> desiredType);
    
    /**
     * Given a normative type and the pvData serialization,
     * returns a matching converter to fill pvData structures from the given normative type.
     * 
     * @param <T> the normative type
     * @param type the normative type to convert
     * @param pvField the desired pvData serialization; can be null
     * @return 
     */
    public <T> PVATypeConverter<? extends T> findConverter(Class<T> type, PVField pvField);
}
