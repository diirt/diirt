/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.lang.ref.WeakReference;

/**
 * Object responsible to notify the PV of changes on the appropriate thread.
 *
 * @author carcassi
 */
class Notifier<T> {

    private final WeakReference<PV<T>> pvRef;
    private final Function<T> function;
    private final ThreadSwitch onThread;
    private volatile PVRecipe pvRecipe;

    /**
     * Creates a new notifier. The new notifier will notifier the given pv
     * with new values calculated by the function, and will use onThread to
     * perform the notifications.
     * <p>
     * After construction, one MUST set the pvRecipe, so that the
     * dataSource is appropriately closed.
     *
     * @param pv the pv on which to notify
     * @param function the function used to calculate new values
     * @param onThread the thread switching mechanism
     */
    Notifier(PV<T> pv, Function<T> function, ThreadSwitch onThread) {
        this.pvRef = new WeakReference<PV<T>>(pv);
        this.function = function;
        this.onThread = onThread;
    }

    /**
     * Determines whether the notifier is active or not.
     * <p>
     * The notifier becomes inactive if the PV is closed or is garbage collected.
     * The first time this function determines that the notifier is inactive,
     * it will ask the data source to close all channels relative to the
     * pv.
     *
     * @return true if new notification should be performed
     */
    boolean isActive() {
        if (pvRef.get() != null && !pvRef.get().isClosed()) {
            return true;
        } else {
            if (pvRecipe != null) {
                pvRecipe.getDataSource().disconnect(pvRecipe.getDataSourceRecipe());
                pvRecipe = null;
            }
            return false;
        }
    }

    /**
     * Notifies the PV of a new value.
     */
    void notifyPv() {
        // Synchronization policy: the newValue is guarded by this pull notifier
        final T newValue;
        synchronized(this) {
            newValue = function.getValue();
        }
        onThread.post(new Runnable() {

            @Override
            public void run() {
                T safeValue;
                synchronized(Notifier.this) {
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

    void setPvRecipe(PVRecipe pvRecipe) {
        this.pvRecipe = pvRecipe;
    }

    PVRecipe getPvRecipe() {
        return pvRecipe;
    }

}
