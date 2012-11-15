/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A builder for {@link WriteBuffer }.
 *
 * @author carcassi
 */
public class WriteBufferBuilder {

    private final Map<String, WriteCache<?>> caches;

    /**
     * A new builder
     */
    public WriteBufferBuilder() {
        caches = new HashMap<>();
    }
    
    public WriteBufferBuilder addChannel(String channelName, WriteCache<?> writeCache) {
        caches.put(channelName, writeCache);
        return this;
    }

    /**
     * Creates a new WriteBuffer.
     * 
     * @return a new WriteBuffer
     */
    public WriteBuffer build(WriteFunction<Exception> exceptionWriteFunction, ConnectionCollector connectionCollector) {
        Set<ChannelWriteBuffer> recipes = new HashSet<>();
        for (Map.Entry<String, WriteCache<?>> entry : caches.entrySet()) {
            String channelName = entry.getKey();
            WriteCache<?> valueCache = entry.getValue();
            recipes.add(new ChannelWriteBuffer(channelName, 
                    new ChannelHandlerWriteSubscription(valueCache, exceptionWriteFunction, connectionCollector.addChannel(channelName))));
        }
        return new WriteBuffer(recipes);
    }
    
}
