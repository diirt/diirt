package gov.bnl.nsls2.pvmanager;

public class PV<T> {
	private String name;
	private T value;
	private State state;
	
	private enum State
    {
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}	

}
