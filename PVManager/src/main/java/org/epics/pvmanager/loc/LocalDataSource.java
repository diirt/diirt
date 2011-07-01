/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.loc;

import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.data.DataTypeSupport;

/**
 * Data source for data locally written.
 *
 * @author carcassi
 */
public final class LocalDataSource extends AbstractChannelDataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    /**
     * Data source instance.
     *
     * @return the data source instance
     */
    public static LocalDataSource localData() {
        return LocalDataSource.instance;
    }

    static final LocalDataSource instance = new LocalDataSource();

    @Override
    protected ChannelHandler createChannel(String channelName) {
        return new LocalChannelHandler(channelName);
    }

}
