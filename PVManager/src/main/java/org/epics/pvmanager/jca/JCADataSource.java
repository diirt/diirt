/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.jca;

import gov.aps.jca.CAException;
import gov.aps.jca.CAStatusException;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.DBR_Double;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.MonitorRecipe;
import org.epics.pvmanager.ValueCache;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A data source that uses jca.
 *
 * @author carcassi
 */
class JCADataSource extends DataSource {
    private static Logger logger = Logger.getLogger(DataSource.class.getName());
    // Get the JCALibrary instance.
    private static JCALibrary jca = JCALibrary.getInstance();
    private static Context ctxt = null;

    static final JCADataSource INSTANCE = new JCADataSource();

    /*
     * This Metod will initialize the jca context.
     */
    private void JCAinit() {
        // Create a context which uses pure channel access java with HARDCODED
        // configuration
        // values.
        // TDB create the context reading some configuration file????
        if (ctxt == null) {
            try {
                logger.fine("Initializing the context.");
                ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
            } catch (CAException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Destroy the channel.
     */
    private synchronized void destroyChannel(Channel channel) {
    }

    /**
     * Remove connection listeners.
     */
    private synchronized void disconnect(Channel channel, ConnectionListener connectionListener) {
        try {
            //TODO add check to determine when to destroy channel.
            channel.removeConnectionListener(connectionListener);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Remove monitor.
     */
    private synchronized void removeMonitor(Monitor monitor, MonitorListener monitorListener) {
        monitor.removeMonitorListener(monitorListener);
    }

    /**
     * Destroy JCA context.
     */
    private void destroy() {
        try {

            // Destroy the context, check if never initialized.
            if (ctxt != null) // Destroys all the channels associated with this context.
            {
                ctxt.destroy();
            }

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * Implementation of MonitorListener. Update the collector associated with
     * this PV.
     */
    private class MonitorListenerImpl extends ValueProcessor<MonitorEvent, Double>
            implements MonitorListener {

        private volatile Monitor monitor;

        public MonitorListenerImpl(Collector collector, ValueCache<Double> value) {
            super(collector, value);
        }

        public synchronized void setMonitor(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public synchronized void monitorChanged(MonitorEvent event) {
            processValue(event);
        }

        public synchronized void reset() {
        }

        @Override
        public void close() {
            removeMonitor(monitor, this);
        }

        @Override
        public boolean updateCache(MonitorEvent event, ValueCache<Double> cache) {
            try {
                DBR_Double rawvalue = (DBR_Double) event.getDBR().convert(
                        DBR_Double.TYPE);
                cache.setValue(rawvalue.getDoubleValue()[0]);
                return true;
            } catch (CAStatusException cAStatusException) {
                throw new RuntimeException(cAStatusException);
            }
        }
    }

    @Override
    public synchronized void monitor(MonitorRecipe connRecipe) {
        for (Map.Entry<String, ValueCache> entry : connRecipe.caches.entrySet()) {
            if (entry.getValue().getType().equals(Double.class)) {
                @SuppressWarnings("unchecked")
                ValueCache<Double> cache = (ValueCache<Double>) entry.getValue();
                monitor(entry.getKey(), connRecipe.collector, cache);
            } else {
                throw new UnsupportedOperationException("Type "
                        + entry.getValue().getType().getName()
                        + " is not yet supported");
            }
        }
    }

    synchronized void monitor(String name, Collector collector,
            ValueCache<Double> cache) {
        JCAinit();

        try {
            Channel channel = ctxt.createChannel(name);
            // we assume it to be a double and move on....
            MonitorListenerImpl monitorListenerImpl = new MonitorListenerImpl(collector, cache);
            Monitor monitor = channel.addMonitor(DBR_Double.TYPE, 1, Monitor.VALUE, monitorListenerImpl);
            monitorListenerImpl.setMonitor(monitor);
            ctxt.flushIO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
