/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Type;
import org.diirt.datasource.DataSourceTypeSupport;
import org.diirt.datasource.ValueCache;
import org.diirt.support.pva.adapters.PVAPVStructure;

/**
 *
 * Given a set of {@link PVATypeAdapter} prepares type support for the
 * JCA data source.
 *
 * @author carcassi
 */
public class PVATypeSupport extends DataSourceTypeSupport {

    private final PVATypeAdapterSet adapters;

    /**
     * A new type support for the pva type support.
     *
     * @param adapters a set of pva adapters
     */
    public PVATypeSupport(PVATypeAdapterSet adapters) {
        this.adapters = adapters;
    }

    final static PVATypeAdapter ToPVAPVStructure = new PVATypeAdapter(
                PVAPVStructure.class,
                null)
        {
            @Override
            public PVAPVStructure createValue(final PVStructure message, PVField valueType, boolean disconnected) {
                return new PVAPVStructure(message, disconnected);
            }
        };

    /**
     * Returns a matching type adapter for the given
     * cache and channel.
     *
     * @param cache the cache that will store the data
     * @param channel the pva channel
     * @return the matched type adapter
     */
    protected PVATypeAdapter find(ValueCache<?> cache, PVAChannelHandler channel) {
        try
        {
                return find(adapters.getAdapters(), cache, channel);
        } catch (IllegalStateException ise) {
                // TODO ultra-ugly
                if (ise.getMessage().indexOf("no match found") != -1)
                {
                        if (channel.getChannelType().getType() == Type.structure)
                        return ToPVAPVStructure;
                }
                throw ise;
        }
    }

}
