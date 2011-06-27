/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An object representing a writable PV. The write payload is specified by the generic type,
 * and is changed is returned by {@link #write(java.lang.Object)}. Changes in
 * values are notified through the {@link PVValueChangeListener}. Listeners
 * can be registered from any thread. The value can only be accessed on the
 * thread on which the listeners is called.
 *
 * @author carcassi
 */
public class PVWriter<T> {

    private List<PVValueWriteListener> valueWriteListeners = new CopyOnWriteArrayList<PVValueWriteListener>();

    void firePvValueWritten() {
        for (PVValueWriteListener listener : valueWriteListeners) {
            listener.pvValueWritten();
        }
    }

    /**
     * Adds a listener to the value. This method is thread safe.
     *
     * @param listener a new listener
     */
    public void addPVValueWriteListener(PVValueWriteListener listener) {
        if (isClosed())
            throw new IllegalStateException("Can't add listeners to a closed PV");
        valueWriteListeners.add(listener);
    }

    /**
     * Removes a listener to the value. This method is thread safe.
     *
     * @param listener the old listener
     */
    public void removePVValueChangeListener(PVValueWriteListener listener) {
        valueWriteListeners.remove(listener);
    }
    
    
    public void write(T newValue) {
        
    }

    // Theoretically, this should be checked only on the client thread.
    // Better to be conservative, and guarantee that another thread
    // cannot add a listener when another is closing.
    private volatile boolean closed = false;

    /**
     * De-registers all listeners, stops all notifications and closes all
     * connections from the data sources needed by this. Once the PV
     * is closed, it can't be re-opened. Subsequent calls to close do not
     * do anything.
     */
    public void close() {
        valueWriteListeners.clear();
        closed = true;
        // TODO close/deallocate the write buffer
    }

    /**
     * True if no more notifications are going to be sent for this PV.
     *
     * @return true if closed
     */
    public boolean isClosed() {
        return closed;
    }
    
}
