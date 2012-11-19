/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collection;

/**
 * Represents all the values, channel names and ordering information needed
 * for writing
 *
 * @author carcassi
 */
public class WriteRecipe {
    private final Collection<ChannelWriteRecipe> channelWriteBuffers;

    WriteRecipe(Collection<ChannelWriteRecipe> channelWriteBuffers) {
        this.channelWriteBuffers = channelWriteBuffers;
    }

    public Collection<ChannelWriteRecipe> getChannelWriteBuffers() {
        return channelWriteBuffers;
    }
    
}
