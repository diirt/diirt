/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.util.ArrayList;
import java.util.List;

import org.epics.pvmanager.Notification;
import org.epics.pvmanager.NotificationSupport;
import org.epics.pvmanager.TimeSupport;
import org.epics.pvmanager.TypeSupport;
import org.epics.pvmanager.util.TimeStamp;

/**
 * Adds support for control system standard types defined in this package.
 *
 * @author carcassi
 */
public final class DataTypeSupport {

    private static boolean installed = false;

    /**
     * Installs type support. This should only be called by either DataSources
     * or ExpressionLanguage libraries that require support for these types.
     */
    public static void install() {
        // Install only once
        if (installed) {
            return;
        }

        // Add time support for everything
        TimeSupport.addTypeSupport(new TimeSupport<Time>(Time.class) {

            @Override
            public TimeStamp extractTimestamp(final Time object) {
                return object.getTimeStamp();
            }
        });

        // Add notification support for all immutable types
        TypeSupport.addTypeSupport(NotificationSupport.immutableTypeSupport(Scalar.class));
        TypeSupport.addTypeSupport(NotificationSupport.immutableTypeSupport(MultiScalar.class));
        TypeSupport.addTypeSupport(NotificationSupport.immutableTypeSupport(Array.class));
        TypeSupport.addTypeSupport(NotificationSupport.immutableTypeSupport(Statistics.class));
        TypeSupport.addTypeSupport(NotificationSupport.immutableTypeSupport(VImage.class));
        addList();

        installed = true;
    }

    private static void addList() {
        TypeSupport.addTypeSupport(new NotificationSupport<List>(List.class) {

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public Notification<List> prepareNotification(List oldValue, final List newValue) {
                // Initialize value if never initialized
                if (oldValue == null) {
                    oldValue = new ArrayList();
                }

                boolean notificationNeeded = false;

                // Check all the elements in the list and use StandardTypeSupport
                // to understand whether any needs notification.
                // Notification is done only if at least one element needs notification.
                for (int index = 0; index < newValue.size(); index++) {
                    if (oldValue.size() <= index) {
                        oldValue.add(null);
                    }

                    if (newValue.get(index) != null) {
                        Notification itemNotification = NotificationSupport.notification(oldValue.get(index), newValue.get(index));
                        if (itemNotification.isNotificationNeeded()) {
                            notificationNeeded = true;
                            oldValue.set(index, itemNotification.getNewValue());
                        }
                    }
                }

                // Shrink the list if more elements are there
                while (oldValue.size() > newValue.size()) {
                    oldValue.remove(oldValue.size() - 1);
                }

                return new Notification<List>(notificationNeeded, oldValue);
            }
        });
    }

    /**
     * Constructor.
     */
    private DataTypeSupport() {
        // Don't instantiate, utility class.
    }
}
