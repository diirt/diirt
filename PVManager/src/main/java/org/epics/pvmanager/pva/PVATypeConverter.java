/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import org.epics.pvData.pv.PVField;
import org.epics.pvData.pv.PVStructure;
import org.epics.pvmanager.data.VDouble;

/**
 * Converts pvStructures into normative types and vice-versa. An instance
 * of this interface will convert a particular normative type (e.g. {@link VDouble})
 * to its pvAccess representation on the wire.
 *
 * @author carcassi
 */
public interface PVATypeConverter<T> {
    
    /**
     * Creates an instance of Value from the data in pvStructure. The returned
     * value should hold no mutable references taken from pvStructure, as
     * it will change after the call. The previousValue is given so that
     * data that did not change can be taken from there instead of creating
     * another fresh copy.
     * <p>
     * The previousValue passed should be of type T, but it's declared as Object
     * to avoid generics warnings/problems.
     * 
     * @param pvStructure object to wrap/copy
     * @param previousValue previous value; can be null
     * @param disconnected whether the channel is connected
     * @return a new Value
     */
    T convertValue(final PVStructure pvStructure, final Object previousValue, boolean disconnected);

    /**
     * Fills the given pvStructure with the information taken from the given value.
     * <p>
     * The value passed should be of type T, but it's declares as Object to
     * avoid generics warnings/problems.
     * 
     * @param pvStructure
     * @param value 
     */
    void fillValue(final PVStructure pvStructure, final Object value);
    
    /**
     * The normative type for this type factory.
     * 
     * @return the type of the value converted; can't be null
     */
    Class<T> getValueType();
    
    /**
     * The introspection information for the pvData serialization for this
     * type factory.
     * 
     * @return the introspection information
     */
    PVField getPVField();
    
}
