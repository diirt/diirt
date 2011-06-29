/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.loc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.WriteBuffer;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.WriteCache;
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
    private Set<WriteBuffer> registeredBuffers = new CopyOnWriteArraySet<WriteBuffer>();

    private LocChannelHandler channel(String channelName) {
        LocChannelHandler channel = usedChannels.get(channelName);
        if (channel == null) {
            channel = new LocChannelHandler(channelName);
            usedChannels.put(channelName, channel);
        }
        return channel;
    }
    
    @Override
    public void connect(final DataRecipe recipe) {
        for (Map.Entry<Collector, Map<String, ValueCache>> collEntry : recipe.getChannelsPerCollectors().entrySet()) {
            final Collector collector = collEntry.getKey();
            for (Map.Entry<String, ValueCache> entry : collEntry.getValue().entrySet()) {
                String channelName = entry.getKey();
                final LocChannelHandler channelHandler = channel(channelName);
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
    
    @Override
    public void prepareWrite(final WriteBuffer writeBuffer) {
        final Set<LocChannelHandler> handlers = new HashSet<LocChannelHandler>();
        for (String channelName : writeBuffer.getWriteCaches().keySet()) {
            handlers.add(channel(channelName));
        }
        
        // Connect using another thread
        exec.execute(new Runnable() {

            @Override
            public void run() {
                for (LocChannelHandler channelHandler : handlers) {
                    channelHandler.addWriter(writeBuffer.getExceptionHandler());
                }
            }
        });
        registeredBuffers.add(writeBuffer);
    }

    @Override
    public void concludeWrite(final WriteBuffer writeBuffer) {
        if (!registeredBuffers.contains(writeBuffer)) {
            log.log(Level.WARNING, "WriteBuffer {0} was unregistered but was never registered. Ignoring it.", writeBuffer);
            return;
        }
        
        registeredBuffers.remove(writeBuffer);
        final Set<LocChannelHandler> handlers = new HashSet<LocChannelHandler>();
        for (String channelName : writeBuffer.getWriteCaches().keySet()) {
            handlers.add(channel(channelName));
        }
        
        // Connect using another thread
        exec.execute(new Runnable() {

            @Override
            public void run() {
                for (LocChannelHandler channelHandler : handlers) {
                    channelHandler.removeWrite(writeBuffer.getExceptionHandler());
                }
            }
        });
    }

    @Override
    public void write(final WriteBuffer writeBuffer) {
        final Map<LocChannelHandler, Object> handlersAndValues = new HashMap<LocChannelHandler, Object>();
        for (Map.Entry<String, WriteCache> entry : writeBuffer.getWriteCaches().entrySet()) {
            LocChannelHandler channel = channel(entry.getKey());
            handlersAndValues.put(channel, entry.getValue().getValue());
        }
        
        // Connect using another thread
        exec.execute(new Runnable() {

            @Override
            public void run() {
                ChannelWriteCallback callback = new ChannelWriteCallback() {

                    AtomicInteger counter = new AtomicInteger();
                    
                    @Override
                    public void channelWritten(Exception ex) {
                        // Notify only when the last channel was written
                        int value = counter.incrementAndGet();
                        if (value == handlersAndValues.size()) {
                            writeBuffer.getWriteListener().pvValueWritten();
                        }
                    }
                };
                for (Map.Entry<LocChannelHandler, Object> entry : handlersAndValues.entrySet()) {
                    entry.getKey().write(entry.getValue(), callback);
                }
            }
        });
    }

}
