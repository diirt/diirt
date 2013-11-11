/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import org.epics.pvmanager.PVWriterEvent;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class WriteEvent implements Event {
    private Timestamp timestamp;
    private String pvName;
    private PVWriterEvent<?> event;
    private boolean coonected;
    private Object value;
    private Exception lastException;

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

    public boolean isCoonected() {
        return coonected;
    }

    public void setCoonected(boolean coonected) {
        this.coonected = coonected;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getLastException() {
        return lastException;
    }

    public void setLastException(Exception lastException) {
        this.lastException = lastException;
    }
    
}
