/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.time.Instant;

import org.diirt.datasource.PVWriterEvent;

/**
 *
 * @author carcassi
 */
public class WriteEvent implements Event {
    private Instant timestamp;
    private String pvName;
    private PVWriterEvent<?> event;
    private boolean connected;
    private Exception lastException;

    public WriteEvent(Instant timestamp, String pvName, PVWriterEvent<?> event, boolean coonected, Exception lastException) {
        this.timestamp = timestamp;
        this.pvName = pvName;
        this.event = event;
        this.connected = coonected;
        this.lastException = lastException;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getPvName() {
        return pvName;
    }

    public void setPvName(String pvName) {
        this.pvName = pvName;
    }

    @Override
    public PVWriterEvent<?> getEvent() {
        return event;
    }

    public void setEvent(PVWriterEvent<?> event) {
        this.event = event;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Exception getLastException() {
        return lastException;
    }

    public void setLastException(Exception lastException) {
        this.lastException = lastException;
    }

}
