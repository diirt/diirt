/**
 * 
 */
package org.epics.pvmanager.pva.adapters;

import java.util.Collections;
import java.util.List;

import org.epics.pvdata.pv.StringArrayData;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.VStringArray;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVStringArray extends AlarmTimeDisplayExtractor implements VStringArray {

	private final String[] array;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVStringArray(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
		
		PVStringArray valueField =
			(PVStringArray)pvField.getScalarArrayField("value", ScalarType.pvString);
		if (valueField != null)
		{
			StringArrayData data = new StringArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.array = data.data;
		}
		else
		{
			this.array = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public List<Integer> getSizes() {
		return Collections.singletonList(array.length);
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VStringArray#getArray()
	 */
	@Override
	public String[] getArray() {
		return array;
	}

}
