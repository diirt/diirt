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
     * returns the matching factory to create the value at each monitor.
     * 
     * @param desiredType
     * @param pvField
     * @return 
     */
    public <T> PVATypeFactory<? extends T> findType(Class<T> desiredType, PVField pvField);
}
