/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import org.epics.pvData.pv.PVStructure;

/**
 *
 * @author carcassi
 */
public interface PVATypeFactory<T> {
    
    /**
     * Creates an instance of Value from the data in pvStructure. The returned
     * value should hold no mutable references taken from pvStructure, as
     * it will change after the call. The previousValue is given so that
     * data that did not change can be taken from there instead of creating
     * another fresh copy.
     * <p>
     * The previousValue passed should be of type T, but it's declared as Object
     * to avoid generics stupidities.
     * 
     * @param pvStructure object to wrap/copy
     * @param previousValue previous value; can be null
     * @param disconnected whether the channel is connected
     * @return a new Value
     */
    T createValue(final PVStructure pvStructure, final Object previousValue, boolean disconnected);

    /**
     * The type created by the type factory.
     * 
     * @return the type of the value created; can't be null
     */
    Class<T> getValueType();
    
}
