/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.*;
import org.epics.pvmanager.util.TimeInterval;
import org.epics.pvmanager.util.TimeStamp;

/**
 *
 * @author carcassi
 */
class SimulationChannelHandler<T> extends MultiplexedChannelHandler<Simulation<T>, T> {

    private final Simulation<T> simulation;
    private final ScheduledExecutorService exec;
    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            // Protect the timer thread for possible problems.
            try {
                if (simulation.lastTime == null) {
                    simulation.lastTime = TimeStamp.now();
                }
                List<T> newValues = simulation.createValues(TimeInterval.between(simulation.lastTime, TimeStamp.now()));

                for (T newValue : newValues) {
                    processMessage(newValue);
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, "Data simulation problem", ex);
            }
        }
    };
    private static final Logger log = Logger.getLogger(SimulationChannelHandler.class.getName());
    private ScheduledFuture<?> taskFuture;

    public SimulationChannelHandler(String channelName, Simulation<T> simulation, ScheduledExecutorService exec) {
        super(channelName);
        this.simulation = simulation;
        this.exec = exec;
    }

    @Override
    public void connect(ExceptionHandler handler) {
        processConnection(simulation);
        simulation.lastTime = TimeStamp.now();
        taskFuture = exec.scheduleWithFixedDelay(task, 0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public void disconnect(ExceptionHandler handler) {
        taskFuture.cancel(false);
        taskFuture = null;
        simulation.lastTime = null;
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        throw new UnsupportedOperationException("Can't write to simulation channel.");
    }

    @Override
    public boolean isConnected() {
        return taskFuture != null;
    }

    @Override
    protected DataSourceTypeAdapter<Simulation<T>, T> findTypeAdapter(ValueCache<?> cache, Simulation<T> connection) {
        return new DataSourceTypeAdapter<Simulation<T>, T>() {

            @Override
            public int match(ValueCache<?> cache, Simulation<T> connection) {
                // TODO whould match the type
                return 1;
            }

            @Override
            public Object getSubscriptionParameter(ValueCache<?> cache, Simulation<T> connection) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean updateCache(ValueCache cache, Simulation<T> connection, T message) {
                cache.setValue(message);
                return true;
            }
        };
    }
}
