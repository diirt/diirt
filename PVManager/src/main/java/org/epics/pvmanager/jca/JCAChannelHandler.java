/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.CAException;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.DBR;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.GetEvent;
import gov.aps.jca.event.GetListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import gov.aps.jca.event.PutEvent;
import gov.aps.jca.event.PutListener;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.ValueCache;

/**
 * A ChannelHandler for the JCADataSource.
 * <p>
 * NOTE: this class is extensible as per Bastian request so that DESY can hook
 * a different type factory. This is a temporary measure until the problem
 * is solved in better, more general way, so that data sources
 * can work only with data source specific types, while allowing
 * conversions to normalized type through operators. The contract of this
 * class is, therefore, expected to change.
 * <p>
 * Related changes are marked so that they are not accidentally removed in the
 * meantime, and can be intentionally removed when a better solution is implemented.
 *
 * @author carcassi
 */
public class JCAChannelHandler extends ChannelHandler<JCAMessagePayload> {

    private static final int LARGE_ARRAY = 100000;
    private final JCADataSource jcaDataSource;
    private volatile Channel channel;
    private volatile ExceptionHandler connectionExceptionHandler;
    private volatile boolean needsMonitor;
    private volatile boolean largeArray = false;

    public JCAChannelHandler(String channelName, JCADataSource jcaDataSource) {
        super(channelName);
        this.jcaDataSource = jcaDataSource;
    }

    @Override
    public synchronized void addMonitor(Collector<?> collector, ValueCache<?> cache, ExceptionHandler handler) {
        if (firstMonitorCache == null) {
            firstMonitorCache = cache;
        }
        super.addMonitor(collector, cache, handler);
    }
 
    private JCATypeAdapter matchAdapterFor(ValueCache<?> cache, Channel channel) {
        return jcaDataSource.getTypeSupport().find(cache, channel);
    }

    @Override
    public void connect(ExceptionHandler handler) {
        connectionExceptionHandler = handler;
        try {
            // Give the listener right away so that no event gets lost
	    // If it's a large array, connect using lower priority
	    if (largeArray) {
                channel = jcaDataSource.getContext().createChannel(getChannelName(), connectionListener, Channel.PRIORITY_MIN);
	    } else {
                channel = jcaDataSource.getContext().createChannel(getChannelName(), connectionListener, (short) (Channel.PRIORITY_MIN + 1));
	    }
            needsMonitor = true;
        } catch (CAException ex) {
            handler.handleException(ex);
        }
    }

    // protected (not private) to allow different type factory
    protected void setup(Channel channel) throws CAException {
        typeAdapter = matchAdapterFor(firstMonitorCache, channel);
        JCASubscriptionParameters subParameters = typeAdapter.getSubscriptionParameter(firstMonitorCache, channel);

        // If metadata is needed, get it
        if (subParameters.getEpicsMetaType() != null) {
            // Need to use callback for the listener instead of doing a synchronous get
            // (which seemed to perform better) because JCA (JNI implementation)
            // would return an empty list of labels for the Enum metadata
            channel.get(subParameters.getEpicsMetaType(), 1, new GetListener() {

                @Override
                public void getCompleted(GetEvent ev) {
                    synchronized(JCAChannelHandler.this) {
                        metadata = ev.getDBR();
                        // In case the metadata arrives after the monitor
                        dispatchValue();
                    }
                }
            });
        }

        // Start the monitor only if the channel was (re)created, and
        // not because a disconnection/reconnection
        if (needsMonitor) {
            channel.addMonitor(subParameters.getEpicsValueType(), subParameters.getCount(), jcaDataSource.getMonitorMask(), monitorListener);
            needsMonitor = false;
        }

        // Flush the entire context (it's the best we can do)
        channel.getContext().flushIO();
    }
    
    protected volatile ValueCache<?> firstMonitorCache;
    protected volatile JCATypeAdapter typeAdapter;
    
    private final ConnectionListener connectionListener = new ConnectionListener() {

            @Override
            public void connectionChanged(ConnectionEvent ev) {
                try {
                    // Take the channel from the event so that there is no
                    // synchronization problem
                    Channel channel = (Channel) ev.getSource();
		    
		    // Check whether the channel is large and was opened
		    // as large. Reconnect if does not match
		    if (ev.isConnected() && channel.getElementCount() >= LARGE_ARRAY && !largeArray) {
			disconnect(connectionExceptionHandler);
			largeArray = true;
			connect(connectionExceptionHandler);
			return;
		    }
                    
                    // Setup monitors on connection
                    if (ev.isConnected()) {
                        setup(channel);
                        dispatchValue();
                    } else {
                        dispatchValue();
                    }
                } catch (Exception ex) {
                    connectionExceptionHandler.handleException(ex);
                }
            }
        };;
    
    // The change in metadata and even has to be atomic
    // so they are both guarded by this
    // protected (not private) to allow different type factory
    protected DBR metadata;
    private MonitorEvent event;
    
    // protected (not private) to allow different type factory
    protected final MonitorListener monitorListener = new MonitorListener() {

        @Override
        public void monitorChanged(MonitorEvent event) {
            synchronized(JCAChannelHandler.this) {
                JCAChannelHandler.this.event = event;
                dispatchValue();
            }
        }
    };
    
    private synchronized void dispatchValue() {
        processValue(new JCAMessagePayload(metadata, event));
    }

    @Override
    public void disconnect(ExceptionHandler handler) {
        try {
            // Close the channel
            channel.destroy();
        } catch (CAException ex) {
            handler.handleException(ex);
        } finally {
            channel = null;
            synchronized(this) {
                metadata = null;
                event = null;
            }
        }
    }

    @Override
    public void write(Object newValue, final ChannelWriteCallback callback) {
        try {
            PutListener listener = new PutListener() {

                @Override
                public void putCompleted(PutEvent ev) {
                    if (ev.getStatus().isSuccessful()) {
                        callback.channelWritten(null);
                    } else {
                        callback.channelWritten(new Exception(ev.toString()));
                    }
                }
            };
            if (newValue instanceof String) {
                channel.put(newValue.toString(), listener);
            } else if (newValue instanceof byte[]) {
                channel.put((byte[]) newValue, listener);
            } else if (newValue instanceof short[]) {
                channel.put((short[]) newValue, listener);
            } else if (newValue instanceof int[]) {
                channel.put((int[]) newValue, listener);
            } else if (newValue instanceof float[]) {
                channel.put((float[]) newValue, listener);
            } else if (newValue instanceof double[]) {
                channel.put((double[]) newValue, listener);
            } else if (newValue instanceof Byte || newValue instanceof Short
                    || newValue instanceof Integer || newValue instanceof Long) {
                channel.put(((Number) newValue).longValue(), listener);
            } else if (newValue instanceof Float || newValue instanceof Double) {
                channel.put(((Number) newValue).doubleValue(), listener);
            } else {
                throw new RuntimeException("Unsupported type for CA: " + newValue.getClass());
            }
            jcaDataSource.getContext().flushIO();
        } catch (CAException ex) {
            callback.channelWritten(ex);
        }
    }

    @Override
    public boolean updateCache(JCAMessagePayload message, ValueCache<?> cache) {
        return typeAdapter.updateCache(cache, channel, message);
    }

    @Override
    public boolean isConnected() {
        return isConnected(channel);
    }
    
    static boolean isConnected(Channel channel) {
        return channel != null && channel.getConnectionState() == Channel.ConnectionState.CONNECTED;
    }
}
