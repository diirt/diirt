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

    PullNotificator(PV<T> pv, Function<T> aggregator, ThreadSwitch onThread) {
        this.pvRef = new WeakReference<PV<T>>(pv);
        this.function = aggregator;
        this.onThread = onThread;
    }

    boolean isActive() {
        if (pvRef.get() != null)
            return true;
        else
            return false;
    }

    void notifyPv() {
        // TODO This object should be properly synchronized
        final T newValue = function.getValue();
        onThread.post(new Runnable() {

            @Override
            public void run() {
                PV<T> pv = pvRef.get();
                if (pv != null && newValue != null) {
                    TypeSupport.Notification<T> notification =
                            TypeSupport.notification(pv.getValue(), newValue);
                    if (notification.isNotificationNeeded()) {
                        pv.setValue(notification.getNewValue());
                    }
                }
            }
        });
    }
}
