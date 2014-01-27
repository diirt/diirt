/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.MultiplexedChannelHandler;
import org.epics.pvmanager.PVManager;
import java.util.concurrent.TimeUnit;
import org.epics.pvmanager.*;

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
