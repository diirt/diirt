/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.epics.pvmanager.Notification;
import org.epics.pvmanager.NotificationTypeSupport;
import org.epics.pvmanager.NullUtils;
import org.epics.pvmanager.TypeSupport;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.TimedTypeSupport;

/**
 * Adds support for control system standard types defined in this package.
 *
 * @author carcassi
 */
public class DataTypeSupport {

    private static boolean installed = false;

    /**
     * Installs type support. This should only be called by either DataSources
     * or ExpressionLanguage libraries that require support for these types.
     */
    public static void install() {
        // Install only once
        if (installed)
            return;

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
        NotificationTypeSupport.addTypeSupport(Scalar.class, 
                                               immutableNotificationTypeSupport(Scalar.class));
        TimedTypeSupport.addTypeSupport(Scalar.class, 
                                        immutableTimedTypeSupport(Scalar.class));
    }

    private static void addMultiScalar() {
        NotificationTypeSupport.addTypeSupport(MultiScalar.class, 
                       immutableNotificationTypeSupport(MultiScalar.class));
        TimedTypeSupport.addTypeSupport(MultiScalar.class, 
                                        immutableTimedTypeSupport(MultiScalar.class));
    }

    private static void addArray() {
        NotificationTypeSupport.addTypeSupport(Array.class, 
                                               immutableNotificationTypeSupport(Array.class));
        TimedTypeSupport.addTypeSupport(Array.class, 
                                        immutableTimedTypeSupport(Array.class));
    }

    private static void addStatistics() {
        NotificationTypeSupport.addTypeSupport(Statistics.class, 
                                               immutableNotificationTypeSupport(Statistics.class));
        TimedTypeSupport.addTypeSupport(Statistics.class, 
                                        immutableTimedTypeSupport(Statistics.class));
    }

    private static void addImage() {
        NotificationTypeSupport.addTypeSupport(VImage.class, 
                                               immutableNotificationTypeSupport(VImage.class));
        TimedTypeSupport.addTypeSupport(VImage.class, 
                                        immutableTimedTypeSupport(VImage.class));
    }
    
    private static void addList() {
        NotificationTypeSupport.addTypeSupport(List.class, 
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
    
    private static <T> NotificationTypeSupport<T> immutableNotificationTypeSupport(final Class<T> clazz) {
        return new NotificationTypeSupport<T>() {
          @Override
          public Notification<T> prepareNotification(final T oldValue, final T newValue) {
              if (NullUtils.equalsOrBothNull(oldValue, newValue))
                  return new Notification<T>(false, null);
              return new Notification<T>(true, newValue);
          }
        };
    }
    
    private static <T> TimedTypeSupport<T> immutableTimedTypeSupport(final Class<T> clazz) {
        return new TimedTypeSupport<T>() {
            @Override
            public TimeStamp extractTimestamp(final T object) {
                return ((Time) object).getTimeStamp();
            }
        };
    }
}
