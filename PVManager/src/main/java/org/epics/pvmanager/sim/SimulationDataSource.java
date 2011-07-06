/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import org.epics.pvmanager.AbstractChannelDataSource;
import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.DataTypeSupport;

/**
 * Data source to produce simulated signals that can be using during development
 * and testing.
 *
 * @author carcassi
 */
public final class SimulationDataSource extends AbstractChannelDataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    /**
     * Data source instance.
     *
     * @return the data source instance
     */
    public static DataSource simulatedData() {
        return SimulationDataSource.instance;
    }

    private static final Logger log = Logger.getLogger(SimulationDataSource.class.getName());
    static final SimulationDataSource instance = new SimulationDataSource();

    /**
     * ExecutorService on which all simulated data is generated.
     */
    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected ChannelHandler<?> createChannel(String channelName) {
        SimFunction simFunction = (SimFunction) NameParser.createFunction(channelName);
        return new SimulationChannelHandler<Object>(channelName, simFunction, exec);
    }

}
