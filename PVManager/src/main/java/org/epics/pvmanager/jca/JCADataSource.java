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
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import java.util.HashMap;
import java.util.HashSet;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.ValueCache;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.data.VDouble;

/**
 * A data source that uses jca.
 *
 * @author carcassi
 */
class JCADataSource extends DataSource {
    private static final Logger logger = Logger.getLogger(DataSource.class.getName());
    // Get the JCALibrary instance.
    private static JCALibrary jca = JCALibrary.getInstance();
    private static Context ctxt = null;

    static final JCADataSource INSTANCE = new JCADataSource();

    JCADataSource() {
        initContext();
    }

    /*
     * This Metod will initialize the jca context.
     */
    private void initContext() {
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
     * Destroy JCA context.
     */
    private void releaseContext() {
        try {

            // Destroy the context, check if never initialized.
            if (ctxt != null && connectedProcessors.isEmpty()) // Destroys all the channels associated with this context.
            {
                ctxt.destroy();
            }

        } catch (CAException th) {
            th.printStackTrace();
        }
    }

    private class VDoubleProcessor extends ValueProcessor<MonitorEvent, VDouble> {

        private VDoubleProcessor(final Channel channel, Collector collector, ValueCache<VDouble> cache)
        throws CAException {
            super(collector, cache);
            channel.addConnectionListener(new ConnectionListener() {

                @Override
                public void connectionChanged(ConnectionEvent ev) {
                    try {
                        if (ev.isConnected()) {
                            metadata = (DBR_CTRL_Double) channel.get(DBRType.CTRL_DOUBLE, 1);
                            monitor = channel.addMonitor(DBR_TIME_Double.TYPE, 1, Monitor.VALUE, monitorListener);
                            ctxt.flushIO();
                        }
                    } catch (CAException ex) {

                    }
                }
            });
        }

        private volatile Monitor monitor;
        private volatile DBR_CTRL_Double metadata;
        private final MonitorListener monitorListener = new MonitorListener() {
                @Override
                public void monitorChanged(MonitorEvent event) {
                    processValue(event);
                }
            };

        @Override
        public void close() {
            monitor.removeMonitorListener(monitorListener);
        }

        @Override
        public boolean updateCache(MonitorEvent event, ValueCache<VDouble> cache) {
            try {
                DBR_CTRL_Double rawvalue = (DBR_CTRL_Double) event.getDBR().convert(
                        DBR_CTRL_Double.TYPE);
                cache.setValue(new VDoubleFromDbrCtrlDouble(rawvalue, metadata));
                return true;
            } catch (CAStatusException cAStatusException) {
                throw new RuntimeException(cAStatusException);
            }
        }
    }

    @Override
    public synchronized void connect(DataRecipe connRecipe) {
        initContext();
        Set<ValueProcessor> processors = new HashSet<ValueProcessor>();
        for (Map.Entry<Collector, Map<String, ValueCache>> collEntry : connRecipe.getChannelsPerCollectors().entrySet()) {
            Collector collector = collEntry.getKey();
            for (Map.Entry<String, ValueCache> entry : collEntry.getValue().entrySet()) {
                if (entry.getValue().getType().equals(VDouble.class)) {
                    @SuppressWarnings("unchecked")
                    ValueCache<VDouble> cache = (ValueCache<VDouble>) entry.getValue();
                    ValueProcessor processor = monitorVDouble(entry.getKey(), collector, cache);
                    if (processor != null)
                        processors.add(processor);
                } else {
                    throw new UnsupportedOperationException("Type "
                            + entry.getValue().getType().getName()
                            + " is not yet supported");
                }
            }
        }
        connectedProcessors.put(connRecipe, processors);
    }

    private Map<DataRecipe, Set<ValueProcessor>> connectedProcessors = new HashMap<DataRecipe, Set<ValueProcessor>>();

    private VDoubleProcessor monitorVDouble(String pvName, Collector collector, ValueCache<VDouble> cache) {
        try {
            synchronized (this) {
                Channel channel = ctxt.createChannel(pvName);
                VDoubleProcessor doubleProcessor = new VDoubleProcessor(channel, collector, cache);
                ctxt.flushIO();
                return doubleProcessor;
            }
            //ctxt.flushIO();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void disconnect(DataRecipe recipe) {
        for (ValueProcessor processor : connectedProcessors.get(recipe)) {
            try {
                processor.close();
            } catch (Exception ex) {
                // TODO
            }
        }
        connectedProcessors.remove(recipe);
        releaseContext();
    }

}
