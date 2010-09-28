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
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.ExceptionHandler;
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
    }

    /*
     * This Metod will initialize the jca context.
     */
    private void initContext(ExceptionHandler handler) {
        // Create a context which uses pure channel access java with HARDCODED
        // configuration
        // values.
        // TDB create the context reading some configuration file????
        if (ctxt == null) {
            try {
                logger.fine("Initializing the context.");
                ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
            } catch (CAException e) {
                handler.handleException(e);
            }
        }
    }

    /**
     * Destroy JCA context.
     */
    private void releaseContext(ExceptionHandler handler) {
        if (ctxt != null && connectedProcessors.isEmpty()) {
            try {
                // If a context was created and is the last pv active,
                // destroy the context.
                ctxt.destroy();
            } catch (CAException e) {
                handler.handleException(e);
            }
        }
    }

    private class VDoubleProcessor extends ValueProcessor<MonitorEvent, VDouble> {

        private VDoubleProcessor(final Channel channel, Collector collector,
                ValueCache<VDouble> cache, final ExceptionHandler handler)
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
                        handler.handleException(ex);
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
    public synchronized void connect(DataRecipe dataRecipe) {
        initContext(dataRecipe.getExceptionHandler());
        Set<ValueProcessor> processors = new HashSet<ValueProcessor>();
        for (Map.Entry<Collector, Map<String, ValueCache>> collEntry : dataRecipe.getChannelsPerCollectors().entrySet()) {
            Collector collector = collEntry.getKey();
            for (Map.Entry<String, ValueCache> entry : collEntry.getValue().entrySet()) {
                if (entry.getValue().getType().equals(VDouble.class)) {
                    @SuppressWarnings("unchecked")
                    ValueCache<VDouble> cache = (ValueCache<VDouble>) entry.getValue();
                    ValueProcessor processor = monitorVDouble(entry.getKey(), collector, cache, dataRecipe.getExceptionHandler());
                    if (processor != null)
                        processors.add(processor);
                } else {
                    throw new UnsupportedOperationException("Type "
                            + entry.getValue().getType().getName()
                            + " is not yet supported");
                }
            }
        }
        connectedProcessors.put(dataRecipe, processors);
    }

    private Map<DataRecipe, Set<ValueProcessor>> connectedProcessors = new HashMap<DataRecipe, Set<ValueProcessor>>();

    private VDoubleProcessor monitorVDouble(String pvName, Collector collector,
            ValueCache<VDouble> cache, ExceptionHandler handler) {
        try {
            Channel channel = ctxt.createChannel(pvName);
            VDoubleProcessor doubleProcessor = new VDoubleProcessor(channel, collector, cache, handler);
            ctxt.flushIO();
            return doubleProcessor;
        } catch (Exception e) {
            handler.handleException(e);
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
        releaseContext(recipe.getExceptionHandler());
    }

}
