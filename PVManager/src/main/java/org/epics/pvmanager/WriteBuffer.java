/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.WriteCache;

/**
 * Represents all the values, channel names and ordering information needed
 * for writing
 *
 * @author carcassi
 */
public class WriteBuffer {
    private final Map<String, WriteCache<?>> caches;
    private final Collector<Boolean> connectionCollector;
    private final Map<String, ValueCache<Boolean>> connectionCaches;

    WriteBuffer(Map<String, WriteCache<?>> caches) {
        this.caches = Collections.unmodifiableMap(caches);
        this.connectionCaches = generateConnectionCaches();
        this.connectionCollector = new ConnectionCollector(connectionCaches);
    }
    
    /**
     * Returns the write caches used by this buffer.
     * 
     * @return the caches for each channel
     */
    public Map<String, WriteCache<?>> getWriteCaches() {
        return caches;
    }
    
    private Map<String, ValueCache<Boolean>> generateConnectionCaches() {
        // TODO: should refactor with similar method in DataRecipe
        Map<String, ValueCache<Boolean>> newCaches = new HashMap<String, ValueCache<Boolean>>();
        for (String name : caches.keySet()) {
            ValueCache<Boolean> cache = new ValueCache<Boolean>(Boolean.class);
            cache.setValue(false);
            newCaches.put(name, cache);
        }
        return newCaches;
    }

    /**
     * Returns the collector with the connection value to be passed
     * to the PVWriter.
     * 
     * @return the connection collector
     */
    public Collector<Boolean> getConnectionCollector() {
        return connectionCollector;
    }

    /**
     * The caches that will hold the connection flag.
     * 
     * @return a map from the pv name to the connection cache
     */
    public Map<String, ValueCache<Boolean>> getConnectionCaches() {
        return connectionCaches;
    }
    
}
