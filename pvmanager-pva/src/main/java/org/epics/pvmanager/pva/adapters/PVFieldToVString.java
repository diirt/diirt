/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.vtype.VString;

/**
 * @author msekoranja
 */
public class PVFieldToVString extends AlarmTimeDisplayExtractor implements VString {
	// NOTE: VString does not extend Display, it's OK (will be undefined)
	
	protected final String value;

	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVString(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);

		PVString stringField = pvField.getStringField("value");
		if (stringField != null)
			value = stringField.get();
		else
			value = null;
	}

	@Override
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PVFieldToVString [value=" + value + ", alarmSeverity="
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
