package gov.bnl.nsls2.pvmanager;

class ConnectionManager {
	
	private static ConnectionManager instance = new ConnectionManager();
		
	private ConnectionManager(){}
	
	static ConnectionManager getInstance(){
		return instance;
	}
	
	PV<TypeDouble> doublePV(String name){
		return null;
		// implement the CA monitor.
	}
	
}
