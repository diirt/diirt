package gov.bnl.nsls2.pvmanager;

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
    public static <E> PV<E> createPv(String name, Class<E> clazz) {
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

    /**
     * State of the connection. This was just taken from PV in CSS.
     */
    public enum State {

        /** Nothing happened, yet */
        Idle,
        /** Trying to connect */
        Connecting,
        /** Got basic connection */
        Connected,
        /** Requested MetaData */
        GettingMetadata,
        /** Received MetaData */
        GotMetaData,
        /** Subscribing to receive value updates */
        Subscribing,
        /** Received Value Updates
         *  <p>
         *  This is the ultimate state!
         */
        GotMonitor,
        /** Got disconnected */
        Disconnected
    }

    private String name;
    public static final String PROP_NAME = "name";

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private T value;
    public static final String PROP_VALUE = "value";

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

    private State state;
    public static final String PROP_STATE = "state";

    /**
     * Get the value of state
     *
     * @return the value of state
     */
    public State getState() {
        return state;
    }

    /**
     * Set the value of state
     *
     * @param state new value of state
     */
    public void setState(State state) {
        State oldState = this.state;
        this.state = state;
        propertyChangeSupport.firePropertyChange(PROP_STATE, oldState, state);
    }

}
