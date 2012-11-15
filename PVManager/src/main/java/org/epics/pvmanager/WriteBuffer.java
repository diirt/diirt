/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.epics.pvmanager.WriteCache;

/**
 * Represents all the values, channel names and ordering information needed
 * for writing
 *
 * @author carcassi
 */
public class WriteBuffer {
    private final Collection<ChannelWriteBuffer> channelWriteBuffers;

    WriteBuffer(Collection<ChannelWriteBuffer> channelWriteBuffers) {
        this.channelWriteBuffers = channelWriteBuffers;
    }

    public Collection<ChannelWriteBuffer> getChannelWriteBuffers() {
        return channelWriteBuffers;
    }
    
}
