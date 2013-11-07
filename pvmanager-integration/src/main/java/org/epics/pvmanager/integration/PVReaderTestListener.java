/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.ArrayList;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;

/**
 *
 * @author carcassi
 */
public abstract class PVReaderTestListener<T> implements PVReaderListener<T> {
    public abstract boolean isSuccess();
    public abstract String getErrorMessage();
    public abstract void close();
    
    public static <T> PVReaderTestListener<T> matchConnections(final boolean... connectionFlags) {
        return new PVReaderTestListener<T>() {
            private int nextCall;
            private boolean success = true;
            private String message;

            @Override
            public void close() {
                if (nextCall < connectionFlags.length) {
                    success = false;
                    message = "Fewer connection notification than expected (" + nextCall + " instead of " + connectionFlags.length + ")";
                }
            }
            
            @Override
            public synchronized boolean isSuccess() {
                return success;
            }

            @Override
            public synchronized String getErrorMessage() {
                return message;
            }

            @Override
            public synchronized void pvChanged(PVReaderEvent<T> event) {
                // Must be a connection notification
                if (!event.isConnectionChanged()) {
                    return;
                }
                
                // If already failed, don't even bother
                if (success) {
                    if (nextCall == connectionFlags.length) {
                        success = false;
                        message = "More connection notification than expected (" + connectionFlags.length + ")";
                    } else {
                        if (event.getPvReader().isConnected() != connectionFlags[nextCall]) {
                            success = false;
                            message = "Connection notification " + nextCall + " was " + event.getPvReader().isConnected() + " (expected " + connectionFlags[nextCall] + ")";
                        } else {
                            nextCall++;
                        }
                    }
                }
            }
        };
    }
}
