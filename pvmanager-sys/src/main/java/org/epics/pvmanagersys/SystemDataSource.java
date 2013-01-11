/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanagersys;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.vtype.DataTypeSupport;
import static org.epics.pvmanager.util.Executors.*;

/**
 * Data source to produce simulated signals that can be using during development
 * and testing.
 *
 * @author carcassi
 */
public final class SystemDataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    public SystemDataSource() {
        super(false);
    }

    private static final Logger log = Logger.getLogger(SystemDataSource.class.getName());

    /**
     * ExecutorService on which all data is polled.
     */
    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(namedPool("pvmanager-sys poller "));

    static ScheduledExecutorService getScheduledExecutorService() {
        return exec;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ChannelHandler createChannel(String channelName) {
        if ("free_mb".equals(channelName)) {
            return new FreeMemoryChannelHandler(channelName);
        }
        if ("max_mb".equals(channelName)) {
            return new MaxMemoryChannelHandler(channelName);
        }
        if ("used_mb".equals(channelName)) {
            return new UsedMemoryChannelHandler(channelName);
        }
        throw new IllegalArgumentException("Channel " + channelName + " does not exist");
    }

}
