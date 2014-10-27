/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.MultiplexedChannelHandler;
import org.epics.pvmanager.*;

/**
 * Implementation for channels of a {@link TestDataSource}.
 *
 * @author carcassi
 */
class NormalChannel extends MultiplexedChannelHandler<Object, Object> {

    NormalChannel(String channelName) {
        super(channelName);
    }

    @Override
    public void connect() {
        processConnection(new Object());
    }

    @Override
    public void disconnect() {
        processConnection(null);
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        if ("Fail".equals(newValue)) {
            callback.channelWritten(new RuntimeException("Total failure"));
        } else {
            callback.channelWritten(null);
        }
    }

    @Override
    protected boolean isWriteConnected(Object payload) {
        return super.isConnected(payload);
    }
    
}
