/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import org.diirt.datasource.PVWriterEvent;
import org.diirt.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class WriteEvent implements Event {
    private Timestamp timestamp;
    private String pvName;
    private PVWriterEvent<?> event;
    private boolean connected;
    private Exception lastException;

    public WriteEvent(Timestamp timestamp, String pvName, PVWriterEvent<?> event, boolean coonected, Exception lastException) {
        this.timestamp = timestamp;
        this.pvName = pvName;
        this.event = event;
        this.connected = coonected;
        this.lastException = lastException;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
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

    @Override
    public String toString() {
        return event.toString();
    }

}
