/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.ArrayList;
import java.util.List;

import org.epics.pvmanager.Notification;
import org.epics.pvmanager.NotificationTypeSupport;
import org.epics.pvmanager.TimedTypeSupport;
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

        // Install NotificationTypeSupports and TimedTypeSupports for data package
        addScalar();
        addMultiScalar();
        addStatistics();
        addArray();
        addImage();
        addList();

        installed = true;
    }
    private static void addScalar() {
        TypeSupport.addTypeSupport(Scalar.class, 
                                   NotificationTypeSupport.immutableTypeSupport(Scalar.class));
        TypeSupport.addTypeSupport(Scalar.class, 
                                   immutableTimedTypeSupport(Scalar.class));
    }

    private static void addMultiScalar() {
        TypeSupport.addTypeSupport(MultiScalar.class, 
                                   NotificationTypeSupport.immutableTypeSupport(MultiScalar.class));
        TypeSupport.addTypeSupport(MultiScalar.class, 
                                   immutableTimedTypeSupport(MultiScalar.class));
    }

    private static void addArray() {
        TypeSupport.addTypeSupport(Array.class, 
                                   NotificationTypeSupport.immutableTypeSupport(Array.class));
        TypeSupport.addTypeSupport(Array.class, 
                                   immutableTimedTypeSupport(Array.class));
    }

    private static void addStatistics() {
        TypeSupport.addTypeSupport(Statistics.class, 
                                   NotificationTypeSupport.immutableTypeSupport(Statistics.class));
        TypeSupport.addTypeSupport(Statistics.class, 
                                   immutableTimedTypeSupport(Statistics.class));
    }

    private static void addImage() {
        TypeSupport.addTypeSupport(VImage.class, 
                                   NotificationTypeSupport.immutableTypeSupport(VImage.class));
        TypeSupport.addTypeSupport(VImage.class, 
                                   immutableTimedTypeSupport(VImage.class));
    }
    
    private static void addList() {
        TypeSupport.addTypeSupport(List.class, 
                                   new NotificationTypeSupport<List>() {
                                       @Override
                                       @SuppressWarnings({ "unchecked", "rawtypes" })
                                       public Notification<List> prepareNotification(List oldValue, final List newValue) {
                                           // Initialize value if never initialized
                                           if (oldValue == null)
                                               oldValue = new ArrayList();
                                           
                                           boolean notificationNeeded = false;
                                           
                                           // Check all the elements in the list and use StandardTypeSupport
                                           // to understand whether any needs notification.
                                           // Notification is done only if at least one element needs notification.
                                           for (int index = 0; index < newValue.size(); index++) {
                                               if (oldValue.size() <= index) {
                                                   oldValue.add(null);
                                               }
                                               
                                               if (newValue.get(index) != null) {
                                                   Notification itemNotification = NotificationTypeSupport.notification(oldValue.get(index), newValue.get(index));
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
    
    private static <T> TimedTypeSupport<T> immutableTimedTypeSupport(@SuppressWarnings("unused") final Class<T> clazz) {
        return new TimedTypeSupport<T>() {
            @Override
            public TimeStamp extractTimestamp(final T object) {
                return ((Time) object).getTimeStamp();
            }
        };
    }
    
    /**
     * Constructor.
     */
    private DataTypeSupport() {
        // Don't instantiate, utility class.
    }
}
