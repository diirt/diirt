/**
 * 
 */
package org.diirt.datasource.pva.adapters;

import org.epics.pvdata.pv.PVStructure;

/**
 * @author msekoranja
 *
 */
public class PVANTNDArray {
	
	private final PVStructure ntNdArray;
	
	public PVANTNDArray(PVStructure ntNdArray, boolean disconnected)
	{
		this.ntNdArray = ntNdArray;
	}
	
	public PVStructure getNTNdArray() {
		return ntNdArray;
	}

	@Override
	public String toString() {
		return ntNdArray.toString();
	}
	
	
}
