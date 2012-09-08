/**
 * 
 */
package org.epics.pvmanager.pva.adapters;

import java.util.Collections;
import java.util.List;

import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.VShortArray;
import org.epics.util.array.ListShort;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVShortArray extends AlarmTimeDisplayExtractor implements VShortArray {

	private final short[] array;
	private final ListShort list;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVShortArray(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
		
		PVShortArray valueField =
			(PVShortArray)pvField.getScalarArrayField("value", ScalarType.pvShort);
		if (valueField != null)
		{
			ShortArrayData data = new ShortArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.array = data.data;
			this.list = new ListShort() {
				
				@Override
				public int size() {
					return array.length;
				}
				
				@Override
				public short getShort(int index) {
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
	 * @see org.epics.pvmanager.data.VShortArray#getArray()
	 */
	@Override
	public short[] getArray() {
		return array;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VShortArray#getData()
	 */
	@Override
	public ListShort getData() {
		return list;
	}

}
