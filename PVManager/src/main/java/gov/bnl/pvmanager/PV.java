package gov.bnl.pvmanager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the PV. It contains all elements that are common
 * to all PVs of all type. The payload is specified by the generic type,
 * and is returned by {@link #getValue()}. The value object is final
 * and cannot be changed, so all changes are done in place. Changes in
 * values are notified throught the {@link PVValueChangeListener}. PVs
 * are created throught the static factory which makes sure the value is
 * propertly created and initialized.
 *
 * @author carcassi
 * @param <T> the type of the PV.
 */
public final class PV<T> {

    //Static factory should be substituted by constructor? Should factory
    // be public or package private? Should PV name also be final?

    private PV(String name) {
        this.name = name;
    }

    /**
     * Factory methods for PV objects. The class is used to initialize
     * the value of the PV.
     *
     * @param <E> type of the new PV
     * @param clazz type of the new PV
     * @return a new PV
     */
    static <E> PV<E> createPv(String name, Class<E> clazz) {
        return new PV<E>(name);
    }

    private List<PVValueChangeListener> valueChangeListeners = new ArrayList<PVValueChangeListener>();

    void firePvValueChanged() {
        for (PVValueChangeListener listener : valueChangeListeners) {
            listener.pvValueChanged();
        }
    }

    /**
     * Adds a listener to the value.
     *
     * @param listener a new listener
     */
    public void addPVValueChangeListener(PVValueChangeListener listener) {
        valueChangeListeners.add(listener);
    }

    /**
     * Adds a listener to the value.
     *
     * @param listener a new listener
     */
    public void removePVValueChangeListener(PVValueChangeListener listener) {
        valueChangeListeners.remove(listener);
    }

    private final String name;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    private T value;

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
        firePvValueChanged();
    }
}
