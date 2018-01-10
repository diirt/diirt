/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.vtype.DataTypeSupport;
import static org.diirt.util.concurrent.Executors.namedPool;

/**
 * Data source to monitor system information.
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
        if ("time".equals(channelName)) {
            return new TimeChannelHandler(channelName);
        }
        if ("user".equals(channelName)) {
            return new UserChannelHandler(channelName);
        }
        if ("host_name".equals(channelName)) {
            return new HostnameChannelHandler(channelName);
        }
        if ("qualified_host_name".equals(channelName)) {
            return new QualifiedHostnameChannelHandler(channelName);
        }
        if (channelName.startsWith(SystemPropertyChannelHandler.PREFIX)) {
            return new SystemPropertyChannelHandler(channelName);
        }
        throw new IllegalArgumentException("Channel " + channelName + " does not exist");
    }

}
