/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements a {@link ChannelHandler} on top of a single subscription and
 * multiplexes all reads on top of it.
 *
 * @param ConnectionPayload type of the payload for the connection
 * @param MessagePayload type of the payload for each message
 * @author carcassi
 */
public abstract class MultiplexedChannelHandler<ConnectionPayload, MessagePayload> extends ChannelHandler {

    private static final Logger log = Logger.getLogger(MultiplexedChannelHandler.class.getName());
    private int readUsageCounter = 0;
    private int writeUsageCounter = 0;
    private MessagePayload lastMessage;
    private ConnectionPayload connectionPayload;
    private Map<Collector<?>, MonitorHandler> monitors = new ConcurrentHashMap<Collector<?>, MonitorHandler>();

    private class MonitorHandler {

        private final Collector<?> collector;
        private final ValueCache<?> cache;
        private final ExceptionHandler exceptionHandler;
        private DataSourceTypeAdapter<ConnectionPayload, MessagePayload> typeAdapter;

        public MonitorHandler(Collector<?> collector, ValueCache<?> cache, ExceptionHandler exceptionHandler) {
            this.collector = collector;
            this.cache = cache;
            this.exceptionHandler = exceptionHandler;
        }

        public final void processValue(MessagePayload payload) {
            if (typeAdapter == null)
                return;
            
            // Lock the collector and prepare the new value.
            synchronized (collector) {
                try {
                    if (typeAdapter.updateCache(cache, getConnectionPayload(), payload)) {
                        collector.collect();
                    }
                } catch (RuntimeException e) {
                    exceptionHandler.handleException(e);
                }
            }
        }
        
        public final void findTypeAdapter() {
            if (getConnectionPayload() == null) {
                typeAdapter = null;
            } else {
                try {
                    typeAdapter = MultiplexedChannelHandler.this.findTypeAdapter(cache, getConnectionPayload());
                } catch(RuntimeException ex) {
                    exceptionHandler.handleException(ex);
                }
            }
        }
        
    }

    protected final ConnectionPayload getConnectionPayload() {
        return connectionPayload;
    }
    
    protected final MessagePayload getLastMessagePayload() {
        return lastMessage;
    }

    protected final void processConnection(ConnectionPayload connectionPayload) {
        this.connectionPayload = connectionPayload;
        
        for (MonitorHandler monitor : monitors.values()) {
            monitor.findTypeAdapter();
        }
    }
    
    protected abstract DataSourceTypeAdapter<ConnectionPayload, MessagePayload> findTypeAdapter(ValueCache<?> cache, ConnectionPayload connection);
    
    /**
     * Creates a new channel handler.
     * 
     * @param channelName the name of the channel this handler will be responsible of
     */
    public MultiplexedChannelHandler(String channelName) {
        super(channelName);
    }

    /**
     * Returns how many read or write PVs are open on
     * this channel.
     * 
     * @return the number of open read/writes
     */
    @Override
    public synchronized int getUsageCounter() {
        return readUsageCounter + writeUsageCounter;
    }
    
    /**
     * Returns how many read PVs are open on this channel.
     * 
     * @return the number of open reads
     */
    @Override
    public synchronized int getReadUsageCounter() {
        return readUsageCounter;
    }
    
    /**
     * Returns how many write PVs are open on this channel.
     * 
     * @return the number of open writes
     */
    @Override
    public synchronized int getWriteUsageCounter() {
        return writeUsageCounter;
    }

    /**
     * Used by the data source to add a read request on the channel managed
     * by this handler.
     * 
     * @param collector collector to be notified at each update
     * @param cache cache to contain the new value
     * @param handler to be notified in case of errors
     */
    @Override
    protected synchronized void addMonitor(Collector<?> collector, ValueCache<?> cache, final ExceptionHandler handler) {
        readUsageCounter++;
        MonitorHandler monitor = new MonitorHandler(collector, cache, handler);
        monitors.put(collector, monitor);
        monitor.findTypeAdapter();
        guardedConnect(handler);
        if (readUsageCounter > 1 && lastMessage != null) {
            monitor.processValue(lastMessage);
        } 
    }

    /**
     * Used by the data source to remove a read request.
     * 
     * @param collector the collector that does not need to be notified anymore
     */
    @Override
    protected synchronized void removeMonitor(Collector<?> collector) {
        monitors.remove(collector);
        readUsageCounter--;
        guardedDisconnect(new ExceptionHandler() {

            @Override
            public void handleException(Exception ex) {
                log.log(Level.WARNING, "Couldn't disconnect channel " + getChannelName(), ex);
            }
        });
    }

    /**
     * Used by the data source to prepare the channel managed by this handler
     * for write.
     * 
     * @param handler to be notified in case of errors
     */
    @Override
    protected synchronized void addWriter(ExceptionHandler handler) {
        writeUsageCounter++;
        guardedConnect(handler);
    }

    /**
     * Used by the data source to conclude writes to the channel managed by this handler.
     * 
     * @param exceptionHandler to be notified in case of errors
     */
    @Override
    protected synchronized void removeWrite(ExceptionHandler exceptionHandler) {
        writeUsageCounter--;
        guardedDisconnect(exceptionHandler);
    }

    /**
     * Process the payload for this channel. This should be called whenever
     * a new value needs to be processed. The handler will take care of
     * calling {@link #updateCache(java.lang.Object, org.epics.pvmanager.ValueCache) }
     * for each read monitor that was setup.
     * 
     * @param payload the payload of for this type of channel
     */
    protected synchronized final void processMessage(MessagePayload payload) {
        lastMessage = payload;
        for (MonitorHandler monitor : monitors.values()) {
            monitor.processValue(payload);
        }
    }

    private void guardedConnect(final ExceptionHandler handler) {
        if (getUsageCounter() == 1) {
            connect(handler);
        }
    }

    private void guardedDisconnect(final ExceptionHandler handler) {
        if (getUsageCounter() == 0) {
            disconnect(handler);
        }
    }

    /**
     * Used by the handler to open the connection. This is called whenever
     * the first read or write request is made.
     * 
     * @param handler to be notified in case of errors
     */
    protected abstract void connect(final ExceptionHandler handler);

    /**
     * Used by the handler to close the connection. This is called whenever
     * the last reader or writer is de-registered.
     * 
     * @param handler to be notified in case of errors
     */
    protected abstract void disconnect(final ExceptionHandler handler);

    /**
     * Implements a write operation. Write the newValues to the channel
     * and call the callback when done.
     * 
     * @param newValue new value to be written
     * @param callback called when done or on error
     */
    @Override
    protected abstract void write(Object newValue, ChannelWriteCallback callback);

    /**
     * Returns true if it is connected.
     * 
     * @return true if underlying channel is connected
     */
    @Override
    public abstract boolean isConnected();
}
