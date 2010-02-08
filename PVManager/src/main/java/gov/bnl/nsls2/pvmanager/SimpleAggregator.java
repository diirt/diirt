package gov.bnl.nsls2.pvmanager;

import javax.swing.SwingUtilities;

public class SimpleAggregator implements Aggregator {

	@Override
	@SuppressWarnings("unchecked")
	public void compute(PV inputs, final PV output) {
		// TODO Auto-generated method stub
		final Object input = inputs.getValue();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				output.setValue(input);
			}
		});
	}

}
