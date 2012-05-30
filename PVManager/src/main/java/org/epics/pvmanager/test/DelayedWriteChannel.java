/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.test;

import org.epics.pvmanager.*;

/**
 * Implementation for channels of a {@link TestDataSource}.
 *
 * @author carcassi
 */
class DelayedWriteChannel extends MultiplexedChannelHandler<Object, Object> {

    DelayedWriteChannel(String channelName) {
        super(channelName);
    }

    @Override
    public void connect(ExceptionHandler handler) {
        processConnection(new Object());
    }

    @Override
    public void disconnect(ExceptionHandler handler) {
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        try {
            Thread.sleep(1000);
            processMessage(newValue);
            callback.channelWritten(null);
        } catch (Exception ex) {
            callback.channelWritten(ex);
        }
    }

    @Override
    public boolean isConnected() {
        return getUsageCounter() != 0;
    }

    @Override
    protected DataSourceTypeAdapter<Object, Object> findTypeAdapter(ValueCache<?> cache, Object connection) {
        return new DataSourceTypeAdapter<Object, Object>() {

            @Override
            public int match(ValueCache<?> cache, Object connection) {
                return 1;
            }

            @Override
            public Object getSubscriptionParameter(ValueCache<?> cache, Object connection) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean updateCache(ValueCache cache, Object connection, Object message) {
                Object oldValue = cache.getValue();
                cache.setValue(message);
                if ((message == oldValue) || (message != null && message.equals(oldValue)))
                    return false;
                return true;
            }
        };
    }
    
}
