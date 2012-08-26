/**
 * 
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmanager.data.VInt;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVInt extends PVFieldToVNumber implements VInt {

	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVInt(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.pva.adapters.PVFieldToVNumber#getValue()
	 */
	@Override
    public Integer getValue()
    {
    	return value.intValue();
    }

}
