/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import gov.aps.jca.event.MonitorEvent;
import org.epics.ca.client.Channel;
import org.epics.ca.client.Channel.ConnectionState;
import org.epics.ca.client.ChannelRequester;
import org.epics.ca.impl.remote.Context;
import org.epics.pvData.monitor.Monitor;
import org.epics.pvData.monitor.MonitorElement;
import org.epics.pvData.monitor.MonitorRequester;
import org.epics.pvData.pv.MessageType;
import org.epics.pvData.pv.PVStructure;
import org.epics.pvData.pv.Status;
import org.epics.pvData.pv.Structure;
import org.epics.pvmanager.*;

/**
 *
 * @author carcassi
 */
public class PVAChannelHandler extends MultiplexedChannelHandler<Object, MonitorEvent> {

    private final Context pvaContext;
    private final short priority;
    private Channel channel;
    private volatile Monitor monitor;

    public PVAChannelHandler(String channelName, Context pvaContext, short priority) {
        super(channelName);
        this.pvaContext = pvaContext;
        this.priority = priority;
    }

    @Override
    public synchronized void addMonitor(Collector<?> collector, ValueCache<?> cache, ExceptionHandler handler) {
//        if (cacheType == null) {
//            cacheType = cache.getType();
//        }
        super.addMonitor(collector, cache, handler);
    }
    private final ChannelRequester channelRequester = new ChannelRequester() {

        @Override
        public void channelCreated(Status status, Channel channel) {
            // Do nothing
        }

        @Override
        public void channelStateChange(Channel c, ConnectionState connectionState) {
//            if (channel.isConnected()) {
//                pvAConnect();
//            } else {
//                pvADisconnect();
//            }
        }

        @Override
        public String getRequesterName() {
            return "pvManager";
        }

        @Override
        public void message(String message, MessageType messageType) {
            // TODO
        }
    };

    @Override
    public void connect(ExceptionHandler handler) {
        //channel = pvaContext.getProvider().createChannel(getChannelName(), channelRequester, priority);
    }
//
//    private void pvAConnect() {
//        // TODO This gives me the type information, to figure out what type I should map to
//        // Create the monitor on this callback?
//        channel.getField(new GetFieldRequester() {
//
//            @Override
//            public void getDone(Status status, Field field) {
//                if (monitor == null) {
//                    monitor = channel.createMonitor(monitorRequester, CreateRequestFactory.createRequest("field()", monitorRequester));
//                } else {
//                    monitor.start();
//                }
//            }
//
//            @Override
//            public String getRequesterName() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void message(String message, MessageType messageType) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        }, null);
//        if (event != null) {
//            processValue(event);
//        }
//    }
//
//    private void pvADisconnect() {
//        monitor.stop();
//        if (event != null) {
//            processValue(event);
//        }
//    }
//
//    // protected (not private) to allow different type factory
//    protected synchronized void setup(Channel channel) throws CAException {
//        // This method may be called twice, if the connection happens
//        // after the ConnectionListener is setup but before
//        // the connection state is polled.
//
//        // The synchronization makes sure that, if that happens, the
//        // two calls are serial. Checking the monitor for null to
//        // make sure the second call does not create another monitor.
//        if (monitor == null) {
//            vTypeFactory = VTypeFactory.matchFor(cacheType, channel.getFieldType(), channel.getElementCount());
//            if (vTypeFactory.getEpicsMetaType() != null) {
//                metadata = channel.get(vTypeFactory.getEpicsMetaType(), 1);
//            }
//            if (vTypeFactory.isArray()) {
//                monitor = channel.addMonitor(vTypeFactory.getEpicsValueType(), channel.getElementCount(), priority, monitorListener);
//            } else {
//                monitor = channel.addMonitor(vTypeFactory.getEpicsValueType(), 1, priority, monitorListener);
//            }
//            channel.getContext().flushIO();
//        }
//    }
////    // protected (not private) to allow different type factory
////    protected Class<?> cacheType;
////    // protected (not private) to allow different type factory
////    protected volatile TypeFactory vTypeFactory;
////    private ConnectionListener connectionListener;
////    // protected (not private) to allow different type factory
////    protected volatile Monitor monitor;
////    // protected (not private) to allow different type factory
////    protected volatile DBR metadata;
////    private volatile MonitorEvent event;
    protected final MonitorRequester monitorRequester = new MonitorRequester() {

        @Override
        public void monitorConnect(Status status, Monitor monitor, Structure structure) {
            // Nothing to do
        }

        @Override
        public void monitorEvent(Monitor monitor) {
            // There may be more then one 
            MonitorElement monitorElement;
            while ((monitorElement = monitor.poll()) != null) {
                PVStructure pvStructure = monitorElement.getPVStructure();
                // TODO Need to create a defensive copy, look at biset to figure
                // out what you can re-use from previous copy
                monitor.release(monitorElement);
            }
            
        }

        @Override
        public void unlisten(Monitor monitor) {
            // Nothing to do
        }

        @Override
        public String getRequesterName() {
            return "pvManager";
        }

        @Override
        public void message(String message, MessageType messageType) {
            // TODO
        }
    };
//
//    private ConnectionListener createConnectionListener(final Channel channel,
//            final ExceptionHandler handler) {
//        return new ConnectionListener() {
//
//            @Override
//            public void connectionChanged(ConnectionEvent ev) {
//                try {
//                    // Setup monitors on connection
//                    if (ev.isConnected()) {
//                        setup(channel);
//                        if (event != null) {
//                            processValue(event);
//                        }
//                    } else {
//                        if (event != null) {
//                            processValue(event);
//                        }
//                    }
//                } catch (Exception ex) {
//                    handler.handleException(ex);
//                }
//            }
//        };
//    }
//
    @Override
    public void disconnect(ExceptionHandler handler) {
        // Close the channel
        try {
            channel.destroy();
        } finally {
            channel = null;
            monitor = null;
        }
    }

    @Override
    public void write(Object newValue, final ChannelWriteCallback callback) {
//        try {
//            PutListener listener = new PutListener() {
//
//                @Override
//                public void putCompleted(PutEvent ev) {
//                    if (ev.getStatus().isSuccessful()) {
//                        callback.channelWritten(null);
//                    } else {
//                        callback.channelWritten(new Exception(ev.toString()));
//                    }
//                }
//            };
//            if (newValue instanceof String) {
//                channel.put(newValue.toString(), listener);
//            } else if (newValue instanceof byte[]) {
//                channel.put((byte[]) newValue, listener);
//            } else if (newValue instanceof short[]) {
//                channel.put((short[]) newValue, listener);
//            } else if (newValue instanceof int[]) {
//                channel.put((int[]) newValue, listener);
//            } else if (newValue instanceof float[]) {
//                channel.put((float[]) newValue, listener);
//            } else if (newValue instanceof double[]) {
//                channel.put((double[]) newValue, listener);
//            } else if (newValue instanceof Byte || newValue instanceof Short
//                    || newValue instanceof Integer || newValue instanceof Long) {
//                channel.put(((Number) newValue).longValue(), listener);
//            } else if (newValue instanceof Float || newValue instanceof Double) {
//                channel.put(((Number) newValue).doubleValue(), listener);
//            } else {
//                throw new RuntimeException("Unsupported type for CA: " + newValue.getClass());
//            }
//            pvaContext.flushIO();
//        } catch (CAException ex) {
//            callback.channelWritten(ex);
//        }
    }

    @Override
    public boolean isConnected() {
        return channel != null && channel.isConnected();
    }

    @Override
    protected DataSourceTypeAdapter<Object, MonitorEvent> findTypeAdapter(ValueCache<?> cache, Object connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
