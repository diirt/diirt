/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVStructure;
import org.epics.util.time.Timestamp;

public class PVFieldToTimestamp  {

	public static final Timestamp create(PVStructure timeStampStructure)
	{
		if (timeStampStructure != null)
		{
			PVLong secsField = timeStampStructure.getLongField("secondsPastEpoch");
			PVInt nanosField = timeStampStructure.getIntField("nanoseconds");
			
			if (secsField == null || nanosField == null)
				return null;
			else
				return  org.epics.util.time.Timestamp.of(secsField.get(), nanosField.get());
		}
		else
			return null;
	}

}
