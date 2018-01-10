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
class DelayedConnectionChannel extends MultiplexedChannelHandler<Object, Object> {

    DelayedConnectionChannel(String channelName) {
        super(channelName);
    }

    @Override
    public void connect() {
        try {
            Thread.sleep(1000);
        } catch(Exception ex) {
        }

        processConnection(new Object());
        processMessage("Initial value");
    }

    @Override
    public void disconnect() {
        processConnection(null);
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        try {
            processMessage(newValue);
            callback.channelWritten(null);
        } catch (Exception ex) {
            callback.channelWritten(ex);
        }
    }

}
