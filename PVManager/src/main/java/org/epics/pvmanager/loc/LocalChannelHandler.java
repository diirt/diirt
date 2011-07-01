/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.loc;

import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.ExceptionHandler;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.ValueFactory;
import org.epics.pvmanager.util.TimeStamp;

/**
 *
 * @author carcassi
 */
public class LocalChannelHandler extends ChannelHandler<Object> {

    public LocalChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    public void connect(ExceptionHandler handler) {
        // Nothing to be done
    }

    @Override
    public void disconnect(ExceptionHandler handler) {
        // Nothing to be done
    }

    @Override
    public void write(Object newValue, ChannelWriteCallback callback) {
        try {
            if (newValue instanceof Number) {
                newValue = ValueFactory.newVDouble(((Number) newValue).doubleValue(),
                        AlarmSeverity.NONE, AlarmStatus.NONE, TimeStamp.now(), null, null, null,
                        null, null, null, null, null, null, null, null);
            } else if (newValue instanceof String) {
                newValue = ValueFactory.newVString(((String) newValue),
                        AlarmSeverity.NONE, AlarmStatus.NONE, TimeStamp.now(), null);
            }
            processValue(newValue);
            callback.channelWritten(null);
        } catch (Exception ex) {
            callback.channelWritten(ex);
        }
    }

    @Override
    public boolean updateCache(Object event, ValueCache<?> cache) {
        Object oldValue = cache.getValue();
        cache.setValue(event);
        if ((event == oldValue) || (event != null && event.equals(oldValue)))
            return false;
        return true;
    }

    @Override
    public boolean isConnected() {
        return getUsageCounter() != 0;
    }
    
}
