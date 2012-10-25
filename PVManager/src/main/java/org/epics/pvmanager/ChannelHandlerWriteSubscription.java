/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

/**
 * Groups all the parameters required to add a writer to a ChannelHandler.
 *
 * @author carcassi
 */
public class ChannelHandlerWriteSubscription {

    public ChannelHandlerWriteSubscription(WriteCache<?> cache, ExceptionHandler handler, Function<Boolean> connectionCache, Collector<Boolean> connectionCollector) {
        this.cache = cache;
        this.handler = handler;
        this.connectionCache = connectionCache;
        this.connectionCollector = connectionCollector;
    }
    
    private final WriteCache<?> cache;
    private final ExceptionHandler handler;
    private final Function<Boolean> connectionCache;
    private final Collector<Boolean> connectionCollector;

    /**
     * The cache to get the value to write.
     * 
     * @return the write cache
     */
    public WriteCache<?> getCache() {
        return cache;
    }

    /**
     * The exception handler for connection/disconnection errors.
     * 
     * @return the exception handler
     */
    public ExceptionHandler getHandler() {
        return handler;
    }

    /**
     * The cache to hold the connection flag.
     * 
     * @return the cache
     */
    public Function<Boolean> getConnectionCache() {
        return connectionCache;
    }

    /**
     * The collector to notify when the connection changes.
     * 
     * @return the collector
     */
    public Collector<Boolean> getConnectionCollector() {
        return connectionCollector;
    }
    
}
