/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import org.epics.pvmanager.PVReaderEvent;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class ReadEvent implements Event {
    private final Timestamp timestamp;
    private final String pvName;
    private final PVReaderEvent<?> event;
    private final boolean connected;
    private final Object value;
    private final Exception lastException;

    public ReadEvent(Timestamp timestamp, String pvName, PVReaderEvent<?> event, boolean coonected, Object value, Exception lastException) {
        this.timestamp = timestamp;
        this.pvName = pvName;
        this.event = event;
        this.connected = coonected;
        this.value = value;
        this.lastException = lastException;
    }
    
    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String getPvName() {
        return pvName;
    }

    @Override
    public PVReaderEvent<?> getEvent() {
        return event;
    }

    public boolean isConnected() {
        return connected;
    }

    public Object getValue() {
        return value;
    }

    public Exception getLastException() {
        return lastException;
    }
    
}
