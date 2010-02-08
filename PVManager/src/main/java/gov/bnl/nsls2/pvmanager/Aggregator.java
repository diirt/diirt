package gov.bnl.nsls2.pvmanager;

public interface Aggregator {
	
	void compute(PV<?> inputs, PV<?> output);
}
