/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents all information needed to create a PV.
 * <p>
 * Once a recipe is created, it cannot be changed.
 *
 * @author carcassi
 */
class PVRecipe {

    private final Map<Collector, Map<String, ValueCache>> channelsPerCollector;
    private final PullNotificator notificator;

    PVRecipe() {
        channelsPerCollector = Collections.emptyMap();
        notificator = null;
    }

    /**
     * Creates a new recipe. The collections passed to the constructor must
     * already be immutable copies.
     *
     * @param channelsPerCollector the list of all channels needed by each collector
     * @param notificator 
     */
    PVRecipe(Map<Collector, Map<String, ValueCache>> channelsPerCollector, PullNotificator notificator) {
        this.channelsPerCollector = channelsPerCollector;
        this.notificator = notificator;
    }

    /**
     * Creates a new recipe by adding the new collector and the new caches.
     *
     * @param collector the new collector
     * @param caches the caches that the collector depends on
     * @return a new recipe
     */
    PVRecipe includeCollector(Collector collector, Map<String, ValueCache> caches) {
        Map<Collector, Map<String, ValueCache>> newChannelsPerCollector =
                new HashMap<Collector, Map<String, ValueCache>>(channelsPerCollector);
        Map<String, ValueCache> newCaches =
                Collections.unmodifiableMap(new HashMap<String, ValueCache>(caches));
        newChannelsPerCollector.put(collector, newCaches);
        return new PVRecipe(Collections.unmodifiableMap(newChannelsPerCollector), notificator);
    }

    public Map<Collector, Map<String, ValueCache>> getChannelsPerCollectors() {
        return channelsPerCollector;
    }

    PullNotificator getNotificator() {
        return notificator;
    }

}
