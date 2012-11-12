/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author carcassi
 */
class NewConnectionCollector implements Function<Boolean> {

    private final Object lock = new Object();
    private final Map<String, Boolean> channelConnected = new HashMap<>();
    private final Map<String, WriteFunction<Boolean>> writeFunctions = new HashMap<>();
    private Boolean connected;

    public WriteFunction<Boolean> addChannel(final String name) {
        synchronized (lock) {
            channelConnected.put(name, false);
            WriteFunction<Boolean> writeFunction = new WriteFunction<Boolean>() {
                @Override
                public void setValue(Boolean newValue) {
                    channelConnected.put(name, newValue);
                    connected = null;
                }
            };
            writeFunctions.put(name, writeFunction);
            connected = null;
            return writeFunction;
        }
    }

    @Override
    public Boolean getValue() {
        synchronized (lock) {
            if (connected == null) {
                connected = calculate(channelConnected);
            }

            return connected;
        }
    }

    protected boolean calculate(Map<String, Boolean> channelConnected) {
        for (Boolean conn : channelConnected.values()) {
            if (conn != Boolean.TRUE) {
                return false;
            }
        }
        return true;
    }

    public void removeChannel(String channelName) {
        synchronized(lock) {
            channelConnected.remove(channelName);
            writeFunctions.remove(channelName);
        }
    }
}
