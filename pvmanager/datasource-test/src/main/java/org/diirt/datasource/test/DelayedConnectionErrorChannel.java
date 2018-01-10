/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.MultiplexedChannelHandler;
import org.diirt.datasource.PVManager;
import java.util.concurrent.TimeUnit;

/**
 * Implementation for channels of a {@link TestDataSource}.
 *
 * @author carcassi
 */
class DelayedConnectionErrorChannel extends MultiplexedChannelHandler<Object, Object> {

    DelayedConnectionErrorChannel(String channelName) {
        super(channelName);
    }

    @Override
    public void connect() {
        PVManager.getReadScannerExecutorService().schedule(new Runnable() {

            @Override
            public void run() {
                reportExceptionToAllReadersAndWriters(new RuntimeException("Connection error"));
            }
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public void disconnect() {
        processConnection(null);
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        // Do nothing
    }

    @Override
    public boolean isConnected(Object obj) {
        return false;
    }

}
