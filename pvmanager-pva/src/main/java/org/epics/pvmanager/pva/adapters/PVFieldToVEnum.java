/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva.adapters;

import java.util.Arrays;
import java.util.List;

import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.StringArrayData;
import org.epics.pvmanager.data.VEnum;

public class PVFieldToVEnum extends AlarmTimeDisplayExtractor implements VEnum {
	
	protected final String value;
	protected final List<String> labels;
	
	public PVFieldToVEnum(PVStructure pvField, boolean disconnected)
	{
		super(pvField, disconnected);
	
		PVStructure enumField = pvField.getStructureField("value");
		if (enumField != null)
		{
			PVStringArray labelsField =
				(PVStringArray)enumField.getScalarArrayField("choices", ScalarType.pvString);
			if (labelsField != null)
			{
				StringArrayData data = new StringArrayData();
				labelsField.get(0, labelsField.getLength(), data);
				labels = Arrays.asList(data.data);
				
				PVInt indexField = enumField.getIntField("index");
				if (indexField != null)
					value = labels.get(indexField.get());
				else
					value = null;
				
				return;
			}
		}
		
		// error
		value = null;
		labels = null;
	}
	
	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Enum#getLabels()
	 */
	@Override
	public List<String> getLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VEnum#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VEnum#getIndex()
	 */
	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PVFieldToVEnum [value=" + value + ", alarmSeverity="
				+ alarmSeverity + ", alarmStatus=" + alarmStatus
				+ ", timeStamp=" + timeStamp + ", timeUserTag=" + timeUserTag
				+ ", isTimeValid=" + isTimeValid + ", lowerDisplayLimit="
				+ lowerDisplayLimit + ", lowerCtrlLimit=" + lowerCtrlLimit
				+ ", lowerAlarmLimit=" + lowerAlarmLimit
				+ ", lowerWarningLimit=" + lowerWarningLimit + ", units="
				+ units + ", format=" + format + ", upperWarningLimit="
				+ upperWarningLimit + ", upperAlarmLimit=" + upperAlarmLimit
				+ ", upperCtrlLimit=" + upperCtrlLimit + ", upperDisplayLimit="
				+ upperDisplayLimit + "]";
	}

}
