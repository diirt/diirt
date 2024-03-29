/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.datasource.BasicTypeSupport;
import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;

/**
 * Data source for testing, error conditions in particular. Each instance of this
 * data source will have its own separate channels and values.
 *
 * @author carcassi
 */
public final class TestDataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        BasicTypeSupport.install();
    }

    /**
     * Creates a new data source.
     */
    public TestDataSource() {
        super(true);
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        if ("delayedWrite".equals(channelName)) {
            return new DelayedWriteChannel(channelName);
        }
        if ("delayedConnection".equals(channelName)) {
            return new DelayedConnectionChannel(channelName);
        }
        if ("delayedConnectionError".equals(channelName)) {
            return new DelayedConnectionErrorChannel(channelName);
        }
        if ("brokenWrite".equals(channelName)) {
            return new BrokenWriteChannel(channelName);
        }
        if ("normal".equals(channelName)) {
            return new NormalChannel(channelName);
        }
        return null;
    }

}
