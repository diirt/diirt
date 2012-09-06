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
import org.epics.pvdata.pv.Structure;
import org.epics.pvmanager.DataSourceTypeAdapter;
import org.epics.pvmanager.ValueCache;

/**
 * Type adapter for PVA data source. Will match a channel based on the value
 * type provided and the array flag. Will match the cache based on the type class.
 *
 * @author msekoranja
 */
public abstract class PVATypeAdapter implements DataSourceTypeAdapter<PVAChannelHandler, PVStructure> {

	// e.g. VDouble.class
    private final Class<?> typeClass;
    
    private final String[] ntIds;
    private final Field pvValueType;

    /**
     * Creates a new type adapter.
     * 
     * @param typeClass the java type this adapter will create
     * @param ntIds optional array of IDs this instance supports
     * @param pvType <code>Field</code> instance this this adapter will convert
     */
    public PVATypeAdapter(Class<?> typeClass, String[] ntIds, Field pvValueType) {
        this.typeClass = typeClass;
        this.ntIds = ntIds;
        this.pvValueType = pvValueType;
    }

    @Override
    public int match(ValueCache<?> cache, PVAChannelHandler channel) {

    	// If the generated type can't be put in the cache, no match
        if (!cache.getType().isAssignableFrom(typeClass))
            return 0;

        // If one of the IDs does not match, no match
        if (ntIds != null)
        {
        	boolean match = false;
        	String ntId = channel.getChannelType().getID();
        	for (String id : ntIds)
        		if (ntId.equals(id))
        		{
        			match = true;
        			break;
        		}
        	
        	if (!match)
        		return 0;
        }
        
        // If the type of the channel does not match, no match
        if (pvValueType != null)
        {
        	// we assume Structure here
        	Field channelValueType = ((Structure)channel.getChannelType()).getField("value");
        	if (channelValueType == null || !pvValueType.equals(channelValueType))
        		return 0;
        }
        
        // Everything matches
        return 1;
    }
    
    @Override
    public Object getSubscriptionParameter(ValueCache<?> cache, PVAChannelHandler channel) {
        throw new UnsupportedOperationException("Not implemented: PVAChannelHandler is multiplexed, will not use this method");
    }

    @Override
    public boolean updateCache(ValueCache cache, PVAChannelHandler channel, PVStructure message) {
        Object value = createValue(message, channel.getChannelType(), !channel.isConnected());
        cache.setValue(value);
        return true;
    }

    /**
     * Given the value and the (optional) metadata, will create the new value.
     * 
     * @param value the value taken from the monitor
     * @param metadata the value taken as metadata
     * @param disconnected true if the value should report the channel is currently disconnected
     * @return the new value
     */
    public abstract Object createValue(PVStructure message, Field valueType, boolean disconnected);
}
