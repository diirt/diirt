/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.test;

import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;

/**
 * Data source for testing, error conditions in particular. Each instance of this
 * data source will have its own separate channels and values.
 *
 * @author carcassi
 */
public final class TestDataSource extends DataSource {

    /**
     * Creates a new data source.
     */
    public TestDataSource() {
        super(true);
    }

    @Override
    protected ChannelHandler<?> createChannel(String channelName) {
        if ("delayedWrite".equals(channelName)) {
            return new DelayedWriteChannel(channelName);
        }
        return null;
    }

}
