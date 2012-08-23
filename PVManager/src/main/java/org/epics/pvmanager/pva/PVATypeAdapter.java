/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.pva;

import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmanager.DataSourceTypeAdapter;
import org.epics.pvmanager.ValueCache;

/**
 * Type adapter for PVA data source. Will match a channel based on the value
 * type provided and the array flag. Will match the cache based on the type class.
 *
 * @author carcassi
 */
public abstract class PVATypeAdapter implements DataSourceTypeAdapter<PVAChannelHandler, PVStructure> {

	// e.g. VDouble.class
    private final Class<?> typeClass;
    
    private final Field pvValueType;

    /**
     * Creates a new type adapter.
     * 
     * @param typeClass the java type this adapter will create
     * @param pvType <code>Field</code> instance this this adapter will convert
     */
    public PVATypeAdapter(Class<?> typeClass, Field pvValueType) {
        this.typeClass = typeClass;
        this.pvValueType = pvValueType;
    }

    @Override
    public int match(ValueCache<?> cache, PVAChannelHandler channel) {
        
        // If the generated type can't be put in the cache, no match
        if (!cache.getType().isAssignableFrom(typeClass))
            return 0;
        
        // If the type of the channel does not match, no match
        if (!pvValueType.equals(channel.getChannelValueType()))
            return 0;

        // Everything matches
        return 1;
    }
    
    @Override
    public Object getSubscriptionParameter(ValueCache<?> cache, PVAChannelHandler channel) {
        throw new UnsupportedOperationException("Not implemented: PVAChannelHandler is multiplexed, will not use this method");
    }

    @Override
    public boolean updateCache(ValueCache cache, PVAChannelHandler channel, PVStructure message) {
    	return false;
    }
}
