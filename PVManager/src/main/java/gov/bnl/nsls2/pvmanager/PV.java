package gov.bnl.nsls2.pvmanager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class PV<T extends PVType> {

    private PV(T value) {
        this.value = value;
    }

    public static <E extends PVType> PV<E> createPv(Class<E> clazz) {
        try {
            E data = clazz.newInstance();
            PV<E> pv = new PV<E>(data);
            return pv;
        } catch (Exception e) {
            throw new RuntimeException("Can't create PV of type " + clazz.getName(), e);
        }
    }

    public void addPVValueChangeListener(PVValueChangeListener listener) {
        value.addPVValueChangeListener(listener);
    }

    public void removePVValueChangeListener(PVValueChangeListener listener) {
        value.removePVValueChangeListener(listener);
    }

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

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange(PROP_NAME, oldName, name);
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
    private final T value;
    public static final String PROP_VALUE = "value";

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public T getValue() {
        return value;
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
