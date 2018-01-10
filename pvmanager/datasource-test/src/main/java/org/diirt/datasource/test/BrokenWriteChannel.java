/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.MultiplexedChannelHandler;

/**
 * Implementation for channels of a {@link TestDataSource}.
 *
 * @author carcassi
 */
class BrokenWriteChannel extends MultiplexedChannelHandler<Object, Object> {

    BrokenWriteChannel(String channelName) {
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
        try {
            callback.channelWritten(new RuntimeException("BrokenWriteChannel"));
        } catch (Exception ex) {
            callback.channelWritten(ex);
        }
    }

}
