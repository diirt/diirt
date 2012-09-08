/**
 * 
 */
package org.epics.pvmanager.pva.adapters;

import java.util.Collections;
import java.util.List;

import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.VDoubleArray;
import org.epics.util.array.ListDouble;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVDoubleArray extends AlarmTimeDisplayExtractor implements VDoubleArray {

	private final double[] array;
	private final ListDouble list;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVDoubleArray(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
		
		PVDoubleArray valueField =
			(PVDoubleArray)pvField.getScalarArrayField("value", ScalarType.pvDouble);
		if (valueField != null)
		{
			DoubleArrayData data = new DoubleArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.array = data.data;
			this.list = new ListDouble() {
				
				@Override
				public int size() {
					return array.length;
				}
				
				@Override
				public double getDouble(int index) {
					return array[index];
				}
			};
		}
		else
		{
			array = null;
			list = null;
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
	 * @see org.epics.pvmanager.data.VDoubleArray#getArray()
	 */
	@Override
	public double[] getArray() {
		return array;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VDoubleArray#getData()
	 */
	@Override
	public ListDouble getData() {
		return list;
	}

}
