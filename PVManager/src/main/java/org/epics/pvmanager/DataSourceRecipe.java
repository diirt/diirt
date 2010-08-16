/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author carcassi
 */
public class DataSourceRecipe {

    private final Map<Collector, Map<String, ValueCache>> channelsPerCollector;

    public DataSourceRecipe() {
        channelsPerCollector = Collections.emptyMap();
    }

    /**
     * Creates a new recipe. The collections passed to the constructor must
     * already be immutable copies.
     *
     * @param channelsPerCollector the list of all channels needed by each collector
     * @param notificator
     */
    private DataSourceRecipe(Map<Collector, Map<String, ValueCache>> channelsPerCollector) {
        this.channelsPerCollector = channelsPerCollector;
    }

    /**
     * Creates a new recipe by adding the new collector and the new caches.
     *
     * @param collector the new collector
     * @param caches the caches that the collector depends on
     * @return a new recipe
     */
    public DataSourceRecipe includeCollector(Collector collector, Map<String, ValueCache> caches) {
        Map<Collector, Map<String, ValueCache>> newChannelsPerCollector =
                new HashMap<Collector, Map<String, ValueCache>>(channelsPerCollector);
        Map<String, ValueCache> newCaches =
                Collections.unmodifiableMap(new HashMap<String, ValueCache>(caches));
        newChannelsPerCollector.put(collector, newCaches);
        return new DataSourceRecipe(Collections.unmodifiableMap(newChannelsPerCollector));
    }

    public DataSourceRecipe includeRecipe(DataSourceRecipe dataSource) {
        Map<Collector, Map<String, ValueCache>> newChannelsPerCollector =
                new HashMap<Collector, Map<String, ValueCache>>(channelsPerCollector);
        newChannelsPerCollector.putAll(dataSource.channelsPerCollector);
        return new DataSourceRecipe(Collections.unmodifiableMap(newChannelsPerCollector));
    }

    public Map<Collector, Map<String, ValueCache>> getChannelsPerCollectors() {
        return channelsPerCollector;
    }

}
