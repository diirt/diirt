package org.epics.pvmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.epics.pvmanager.data.Array;
import org.epics.pvmanager.data.MultiScalar;
import org.epics.pvmanager.data.Scalar;
import org.epics.pvmanager.data.Statistics;
import org.epics.pvmanager.data.VImage;


/**
 * Dedicated notification type support.
 * 
 * @author bknerr
 * @since 17.01.2011
 */
public abstract class NotificationTypeSupport<T> extends TypeSupport<T> {
    
    private static Map<Class<?>, TypeSupport<?>> typeSupports = 
        new ConcurrentHashMap<Class<?>, TypeSupport<?>>();
    private static Map<Class<?>, TypeSupport<?>> calculatedTypeSupports = 
        new ConcurrentHashMap<Class<?>, TypeSupport<?>>();
    
    public static class Notification<T> {
        private boolean notificationNeeded;
        private T newValue;

        public Notification(boolean notificationNeeded, T newValue) {
            this.notificationNeeded = notificationNeeded;
            this.newValue = newValue;
        }

        public boolean isNotificationNeeded() {
            return notificationNeeded;
        }

        public T getNewValue() {
            return newValue;
        }
    }
    
    private static boolean installed = false;

    /**
     * Installs type support. This should only be called by either DataSources
     * or ExpressionLanguage libraries that require support for these types.
     */
    public static void install() {
        // Install only once
        if (installed)
            return;

        addScalar();
        addMultiScalar();
        addStatistics();
        addArray();
        addImage();
        addList();

        installed = true;
    }
    private static void addScalar() {
        // Add support for all scalars: simply return the new value
        addTypeSupport(Scalar.class, 
                       immutableTypeSupport(Scalar.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addMultiScalar() {
        // Add support for all multi scalars: simply return the new value
        addTypeSupport(MultiScalar.class, 
                       immutableTypeSupport(MultiScalar.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addArray() {
        // Add support for all arrays: simply return the new value
        addTypeSupport(Array.class, 
                       immutableTypeSupport(Array.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addStatistics() {
        // Add support for statistics: simply return the new value
        addTypeSupport(Statistics.class, 
                       immutableTypeSupport(Statistics.class),
                       typeSupports,
                       calculatedTypeSupports);
    }

    private static void addImage() {
        // Add support for statistics: simply return the new value
        addTypeSupport(VImage.class, 
                       immutableTypeSupport(VImage.class),
                       typeSupports,
                       calculatedTypeSupports);
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
                                  },
                                  typeSupports,
                                  calculatedTypeSupports);
  }
    
    
    /**
     * Returns the final value by using the appropriate type support.
     *
     * @param <T> the type of the value
     * @param oldValue the oldValue, which was previously in the previous notification
     * @param newValue the newValue, which was computed during the scanning
     * @return the value to be notified
     */
    public static <T> Notification<T> notification(final T oldValue, final T newValue) {
        @SuppressWarnings("unchecked")
        Class<T> typeClass = (Class<T>) newValue.getClass();
        NotificationTypeSupport<T> support = (NotificationTypeSupport<T>) cachedTypeSupportFor(typeClass, 
                                                                                               typeSupports,
                                                                                               calculatedTypeSupports);
        return support.prepareNotification(oldValue, newValue);
    }    
    

    /**
     * Given the old and new value, prepare the final value that will be notified.
     * This method is guaranteed to be called in the notification thread (the
     * UI thread). This method may either update the old value or return the new
     * value, depending on whether the type is immutable or what is more efficient.
     *
     * @param oldValue the oldValue, which was previously in the previous notification
     * @param newValue the newValue, which was computed during the scanning
     * @return the value to be notified
     */
    protected abstract Notification<T> prepareNotification(final T oldValue, final T newValue);
    
    // TODO (carcassi) : Similar for TimedTypeSupport, as for now, they are treated equal without
    //                   discriminating by type 'clazz', right?! So momentarily the TypeSupport 
    //                   map lookup isn't really necessary, or am I wrong?
    private static <T> NotificationTypeSupport<T> immutableTypeSupport(final Class<T> clazz) {
        return new NotificationTypeSupport<T>() {
          @Override
          public Notification<T> prepareNotification(final T oldValue, final T newValue) {
              if (NullUtils.equalsOrBothNull(oldValue, newValue))
                  return new Notification<T>(false, null);
              return new Notification<T>(true, newValue);
          }
        };
    }
    
}
