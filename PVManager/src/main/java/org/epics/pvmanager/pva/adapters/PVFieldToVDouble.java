/**
 * 
 */
package org.epics.pvmanager.pva.adapters;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmanager.data.VDouble;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVDouble extends PVFieldToVNumber implements VDouble {

	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVDouble(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
	}

    /* (non-Javadoc)
     * @see org.epics.pvmanager.pva.adapters.PVFieldToVNumber#getValue()
     */
	@Override
    public Double getValue()
    {
    	return value;
    }

}
