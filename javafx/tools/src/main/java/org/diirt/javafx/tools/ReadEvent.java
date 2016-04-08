/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.time.Instant;

import org.diirt.datasource.PVReaderEvent;

/**
 *
 * @author carcassi
 */
public class ReadEvent implements Event {
    private final Instant timestamp;
    private final String pvName;
    private final PVReaderEvent<?> event;
    private final boolean connected;
    private final Object value;
    private final Exception lastException;

    public ReadEvent(Instant timestamp, String pvName, PVReaderEvent<?> event, boolean coonected, Object value, Exception lastException) {
        this.timestamp = timestamp;
        this.pvName = pvName;
        this.event = event;
        this.connected = coonected;
        this.value = value;
        this.lastException = lastException;
    }

    @Override
    public Instant getTimestamp() {
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

    @Override
    public String toString() {
        return event.toString();
    }

}
