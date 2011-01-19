package org.epics.pvmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
    
    
    public static <T> void addTypeSupport(final Class<T> typeClass, 
                                          final TypeSupport<T> typeSupport) {
        typeSupports.put(typeClass, typeSupport);
        calculatedTypeSupports.remove(typeClass);
      // TODO (carcassi) : On adding a new type support for 'typeClass', all other calculated ones are removed?
//      calculatedTypeSupport.clear(); 
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
    public abstract Notification<T> prepareNotification(final T oldValue, final T newValue);
}
