/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.loc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.DataTypeSupport;

/**
 * Data source for data locally written.
 *
 * @author carcassi
 */
public final class LocalDataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    /**
     * Data source instance.
     *
     * @return the data source instance
     */
    public static DataSource localData() {
        return LocalDataSource.instance;
    }

    private static final Logger log = Logger.getLogger(LocalDataSource.class.getName());
    static final LocalDataSource instance = new LocalDataSource();

    private Executor exec = Executors.newSingleThreadExecutor();
    
    private Map<String, LocChannelHandler> usedChannels = new ConcurrentHashMap<String, LocChannelHandler>();
    private Set<DataRecipe> recipes = new CopyOnWriteArraySet<DataRecipe>();

    @Override
    public void connect(final DataRecipe recipe) {
        for (Map.Entry<Collector, Map<String, ValueCache>> collEntry : recipe.getChannelsPerCollectors().entrySet()) {
            final Collector collector = collEntry.getKey();
            for (Map.Entry<String, ValueCache> entry : collEntry.getValue().entrySet()) {
                String channelName = entry.getKey();
                if (usedChannels.get(channelName) == null) {
                    usedChannels.put(channelName, new LocChannelHandler(channelName));
                }
                final LocChannelHandler channelHandler = usedChannels.get(channelName);
                final ValueCache cache = entry.getValue();
                
                // Add monitor on other thread in case it triggers notifications
                exec.execute(new Runnable() {

                    @Override
                    public void run() {
                        channelHandler.addMonitor(collector, cache, recipe.getExceptionHandler());
                    }
                });
            }
        }
        recipes.add(recipe);
    }

    @Override
    public void disconnect(DataRecipe recipe) {
        if (!recipes.contains(recipe)) {
            log.log(Level.WARNING, "DataRecipe {0} was disconnected but was never connected. Ignoring it.", recipe);
            return;
        }

        for (Map.Entry<Collector, Map<String, ValueCache>> collEntry : recipe.getChannelsPerCollectors().entrySet()) {
            Collector collector = collEntry.getKey();
            for (Map.Entry<String, ValueCache> entry : collEntry.getValue().entrySet()) {
                String channelName = entry.getKey();
                LocChannelHandler channelHandler = usedChannels.get(channelName);
                if (channelHandler == null) {
                    log.log(Level.WARNING, "Channel {0} should have been connected, but is not found during disconnection. Ignoring it.", channelName);
                }
                channelHandler.removeMonitor(collector);
            }
        }
        
        recipes.remove(recipe);
    }

}
