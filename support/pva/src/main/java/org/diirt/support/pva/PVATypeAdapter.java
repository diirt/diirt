/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import java.util.Arrays;

import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Type;
import org.diirt.datasource.DataSourceTypeAdapter;
import org.diirt.datasource.ValueCache;

/**
 * Type adapter for PVA data source. Will match a channel based on the value
 * type provided and the array flag. Will match the cache based on the type class.
 *
 * @author msekoranja
 */
public abstract class PVATypeAdapter implements DataSourceTypeAdapter<PVAChannelHandler, PVStructure> {

        // e.g. VDouble.class
    private final Class<?> typeClass;

    // PVStructure requirements
    private final String[] ntIds;
    private final Field[] valueFieldTypes;

    /**
     * Creates a new type adapter.
     *
     * @param typeClass the java type this adapter will create
     * @param ntIds array of IDs this adapter is able convert, <code>null</code> allowed
     */
    public PVATypeAdapter(Class<?> typeClass, String[] ntIds) {
        this(typeClass, ntIds, (Field[])null);
    }

    /**
     * Creates a new type adapter.
     *
     * @param typeClass the java type this adapter will create
     * @param ntIds array of IDs this adapter is able convert, <code>null</code> allowed
     * @param fieldType <code>Field</code> instance this adapter is able convert
     */
    public PVATypeAdapter(Class<?> typeClass, String[] ntIds, Field fieldType) {
        this(typeClass, ntIds, new Field[] { fieldType });
    }

    /**
     * Creates a new type adapter.
     *
     * @param typeClass the java type this adapter will create
     * @param ntIds array of IDs this adapter is able convert, <code>null</code> allowed
     * @param fieldTypes <code>Field</code> instances this adapter is able convert, <code>null</code> allowed
     */
    public PVATypeAdapter(Class<?> typeClass, String[] ntIds, Field[] fieldTypes) {
        this.typeClass = typeClass;
        this.ntIds = ntIds;
        this.valueFieldTypes = fieldTypes;
    }

    public boolean match(Structure structure) {
        // If one of the IDs does not match, no match
        if (ntIds != null)
        {
                boolean match = false;
                String ntId = structure.getID();
                // TODO "structure" ID ??
                for (String id : ntIds)
                        if (ntId.startsWith(id))        // ignore minor version
                        {
                                match = true;
                                break;
                        }

                if (!match)
                        return false;
        }

        // If the type of the channel does not match, no match
        if (valueFieldTypes != null)
        {
                boolean match = false;
                // we assume Structure here
                Field channelValueType = structure.getField("value");
                if (channelValueType != null)
                {
                for (Field vf : valueFieldTypes)
                        if (channelValueType.equals(vf))
                        {
                                match = true;
                                break;
                        }

                if (!match)
                        return false;
                }
        }

        // Everything matches
        return true;
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
                // TODO "structure" ID ??
                for (String id : ntIds)
                        if (ntId.startsWith(id))        // ignore minor version
                        {
                                match = true;
                                break;
                        }

                if (!match)
                        return 0;
        }

        // If the type of the channel does not match, no match
        if (valueFieldTypes != null)
        {
                boolean match = false;
                // we assume Structure here
                Field channelType = channel.getChannelType();
                Field channelValueType = (channelType.getType() == Type.structure) ?
                                ((Structure)channelType).getField("value") : channelType;
                if (channelValueType != null)
                {
                for (Field vf : valueFieldTypes)
                        if (channelValueType.equals(vf))
                        {
                                match = true;
                                break;
                        }

                if (!match)
                        return 0;
                }
        }

        // Everything matches
        return 1;
    }

    @Override
    public Object getSubscriptionParameter(ValueCache<?> cache, PVAChannelHandler channel) {
        throw new UnsupportedOperationException("Not implemented: PVAChannelHandler is multiplexed, will not use this method");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean updateCache(@SuppressWarnings("rawtypes") ValueCache cache, PVAChannelHandler channel, PVStructure message) {

        PVField valueField = null;
        String extractFieldName = channel.getExtractFieldName();
        if (extractFieldName != null)
        {
                if (channel.getChannelType().getType() == Type.structure)
                        message = message.getStructureField(extractFieldName);
                else
                        // this avoids problem when scalars/scalar arrays needs to be passed as PVStructure message
                        valueField = message.getSubField(extractFieldName);

        }

        Object value = createValue(message, valueField, !channel.isConnected());
        cache.writeValue(value);
        return true;
    }

    /**
     * Given the value create the new value.
     *
     * @param message the value taken from the monitor
     * @param valueField the value field data, optional
     * @param disconnected true if the value should report the channel is currently disconnected
     * @return the new value
     */
    public abstract Object createValue(PVStructure message, PVField valueField, boolean disconnected);

        @Override
        public String toString() {
                return "PVATypeAdapter [typeClass=" + typeClass + ", ntIds="
                                + Arrays.toString(ntIds) + ", valueFieldTypes="
                                + Arrays.toString(valueFieldTypes) + "]";
        }

}
