package org.epics.pvmanager.pva;

import static org.epics.pvmanager.ExpressionLanguage.channel;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.util.time.TimeDuration;

public class PVASimpleExample {
	
	public static void main(String[] args) throws InterruptedException
	{
		PVManager.setDefaultDataSource(new PVADataSource());
		PVManager.read(channel("testCounter")).
				readListener(new PVReaderListener<Object>() {

					@Override
					public void pvChanged(PVReaderEvent<Object> event) {
						if (event.isValueChanged())
							System.out.println(event.getPvReader().getValue());
						else
							System.out.println(event.toString());
						
					}
				}).maxRate(TimeDuration.ofHertz(10));
		
		while (true)
			Thread.sleep(Long.MAX_VALUE);
	}

}
