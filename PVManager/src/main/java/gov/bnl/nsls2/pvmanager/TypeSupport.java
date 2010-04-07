/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.bnl.nsls2.pvmanager.types.DoubleStatistics;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    public abstract Notification<T> prepareValue(T oldValue, T newValue);
    
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
     * @return the support of the type
     */
    @SuppressWarnings("unchecked")
    static <T> TypeSupport<T> typeSupportFor(Class<T> typeClass) {
        return (TypeSupport<T>) typeSupports.get(typeClass);
    }

    /**
     * Returns the final value by using the appropriate type support.
     *
     * @param <T> the type of the value
     * @param oldValue the oldValue, which was previously in the previous notification
     * @param newValue the newValue, which was computed during the scanning
     * @return the value to be notified
     */
    static <T> Notification<T> prepareValueAccordingly(T oldValue, T newValue) {
        @SuppressWarnings("unchecked")
        Class<T> typeClass = (Class<T>) newValue.getClass();
        return typeSupportFor(typeClass).prepareValue(oldValue, newValue);
    }

    static <T> TimeStamp timestampOfAccordingly(T value) {
        @SuppressWarnings("unchecked")
        Class<T> typeClass = (Class<T>) value.getClass();
        return ((TimedTypeSupport<T>) typeSupportFor(typeClass)).timestampOf(value);
    }

    static {
        addStandardTypeSupport();
    }

    /**
     * Adds supports for standard types: Double and DoubleStatistics
     */
    public static void addStandardTypeSupport() {
        // Add Double support: simply return the new value
        addTypeSupport(Double.class, new TypeSupport<Double>() {
            @Override
            public Notification<Double> prepareValue(Double oldValue, Double newValue) {
                if (NullUtils.equalsOrBothNull(oldValue, newValue))
                    return new Notification<Double>(false, null);
                return new Notification<Double>(true, newValue);
            }
        });

        // Add DoubleStatistics support: copy the new values in the old object.
        addTypeSupport(DoubleStatistics.class, new TypeSupport<DoubleStatistics>() {
            @Override
            public Notification<DoubleStatistics> prepareValue(DoubleStatistics oldValue, DoubleStatistics newValue) {
                if (oldValue == null)
                    oldValue = new DoubleStatistics();
                if (oldValue.equals(newValue))
                    return new Notification<DoubleStatistics>(false, null);
                oldValue.setStatistics(newValue.getAverage(), newValue.getMin(),
                        newValue.getMax(), newValue.getStdDev());
                return new Notification<DoubleStatistics>(true, oldValue);
            }
        });
    }

}
