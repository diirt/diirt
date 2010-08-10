/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.lang.ref.WeakReference;
import javax.swing.SwingUtilities;

/**
 *
 * @author carcassi
 */
class PullNotificator<T> {

    private final WeakReference<PV<T>> pvRef;
    private final Function<T> function;
    private final ThreadSwitch onThread;

    PullNotificator(PV<T> pv, Function<T> function, ThreadSwitch onThread) {
        this.pvRef = new WeakReference<PV<T>>(pv);
        this.function = function;
        this.onThread = onThread;
    }

    boolean isActive() {
        if (pvRef.get() != null && !pvRef.get().isClosed())
            return true;
        else
            return false;
    }

    void notifyPv() {
        // Synchronization policy: the newValue is guarded by this pull notificator
        final T newValue;
        synchronized(this) {
            newValue = function.getValue();
        }
        onThread.post(new Runnable() {

            @Override
            public void run() {
                T safeValue;
                synchronized(PullNotificator.this) {
                    safeValue = newValue;
                }
                PV<T> pv = pvRef.get();
                if (pv != null && safeValue != null) {
                    TypeSupport.Notification<T> notification =
                            TypeSupport.notification(pv.getValue(), safeValue);
                    if (notification.isNotificationNeeded()) {
                        pv.setValue(notification.getNewValue());
                    }
                }
            }
        });
    }
}
