/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the mechanism for registering different types so that the library
 * knows how to handle them.
 * <p>
 * For a type to be usable by the library it needs to be defined:
 * <ul>
 *   <li>How to copy - since values given to the UI should be modified only
 *   within the UI thread, it follows that new values cannot be prepared
 *   "in place", on the same object that was given to the UI. At notification,
 *   there will be then two copies, the old and the new, and in need to be clear
 *   how the new copy should be delivered. (e.g. just pass the new copy, modify
 *   the old object in place, etc...).</li>
 *   <li>When to notify - by comparing elements of the value, it should
 *   decide on what condition the old value need to be modified and the
 *   UI should be notified of the change.</li>
 * </ul>
 *
 * @author carcassi
 */
public abstract class TypeSupport<T> {

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

    /**
     * Given the old and new value, prepare the final value that will be notified.
     * This method is guaranteed to be called in the notification thread (the
     * UI thread). This method may either update the old value or return the new
     * value, depending on wether the type is immutable or what is more efficient.
     *
     * @param oldValue the oldValue, which was previously in the previous notification
     * @param newValue the newValue, which was computed during the scanning
     * @return the value to be notified
     */
    public abstract Notification<T> prepareNotification(T oldValue, T newValue);
    
    private static Map<Class<?>, TypeSupport<?>> typeSupports = new ConcurrentHashMap<Class<?>, TypeSupport<?>>();

    /**
     * Adds support for a new type.
     *
     * @param <T> the type to add support for
     * @param typeClass the class of the type
     * @param typeSupport the support for the type
     */
    public static <T> void addTypeSupport(Class<T> typeClass, TypeSupport<T> typeSupport) {
        typeSupports.put(typeClass, typeSupport);
    }

    /**
     * Retrieves support for the given type.
     *
     * @param <T> the type to retrieve support for
     * @param typeClass the class of the type
     * @return the support for the type or null
     */
    @SuppressWarnings("unchecked")
    static <T> TypeSupport<T> typeSupportFor(Class<T> typeClass) {
        return (TypeSupport<T>) typeSupports.get(typeClass);
    }

    /**
     * Retrieve support for the given type and if not found looks at the
     * implemented interfaces.
     *
     * @param <T> the type to retrieve support for
     * @param typeClass the class of the type
     * @return the support for the type or null
     */
    @SuppressWarnings("unchecked")
    static <T> TypeSupport<T> recursiveTypeSupportFor(Class<T> typeClass) {
        TypeSupport<T> support = typeSupportFor(typeClass);
        if (support == null) {
            for (Class clazz : typeClass.getInterfaces()) {
                support = typeSupportFor(clazz);
                if (support != null)
                    return support;
            }
        }
        return support;
    }

    /**
     * Returns the final value by using the appropriate type support.
     *
     * @param <T> the type of the value
     * @param oldValue the oldValue, which was previously in the previous notification
     * @param newValue the newValue, which was computed during the scanning
     * @return the value to be notified
     */
    public static <T> Notification<T> notification(T oldValue, T newValue) {
        @SuppressWarnings("unchecked")
        Class<T> typeClass = (Class<T>) newValue.getClass();
        if (recursiveTypeSupportFor(typeClass) == null)
            throw new RuntimeException("No support found for type " + typeClass);
        return recursiveTypeSupportFor(typeClass).prepareNotification(oldValue, newValue);
    }

    public static <T> TimeStamp timestampOf(T value) {
        @SuppressWarnings("unchecked")
        Class<T> typeClass = (Class<T>) value.getClass();
        return ((TimedTypeSupport<T>) recursiveTypeSupportFor(typeClass)).extractTimestamp(value);
    }

}
