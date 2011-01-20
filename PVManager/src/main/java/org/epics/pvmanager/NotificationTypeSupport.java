package org.epics.pvmanager;



/**
 * Dedicated notification type support.
 * 
 * @author bknerr
 * @since 17.01.2011
 */
public abstract class NotificationTypeSupport<T> extends TypeSupport<T> {
    
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
        NotificationTypeSupport<T> support = 
            (NotificationTypeSupport<T>) cachedTypeSupportFor(NotificationTypeSupport.class, 
                                                              typeClass);
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
    
    /**
     * Adds an immutable type support that does not actually has to discriminate via {@param clazz}
     * but uses the parameter merely as compiler information for @param <T>.
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> NotificationTypeSupport<T> immutableTypeSupport(@SuppressWarnings("unused") final Class<T> clazz) {
        return new NotificationTypeSupport<T>() {
          @Override
          public Notification<T> prepareNotification(final T oldValue, final T newValue) {
              if (NullUtils.equalsOrBothNull(oldValue, newValue)) {
                return new Notification<T>(false, null);
            }
              return new Notification<T>(true, newValue);
          }
        };
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends TypeSupport<T>> getTypeSupportFamily() {
        return (Class<? extends TypeSupport<T>>) NotificationTypeSupport.class;
    }
}
