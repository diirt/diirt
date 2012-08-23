/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.pva;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class PVAVTypeAdapterSet implements PVATypeAdapterSet {
    
    @Override
    public Set<PVATypeAdapter> getAdapters() {
        return converters;
    }
    
    // double -> VDouble
    final static PVATypeAdapter DoubleToVDouble = new PVATypeAdapter(VDouble.class, FieldFactory.getFieldCreate().createScalar(ScalarType.pvDouble)) {

            @Override
            public VDouble createValue(PVStructure message, Field valueType, boolean disconnected) {
                return null;
            }
        };

    // int -> VInt
    final static PVATypeAdapter IntToVInt = new PVATypeAdapter(VInt.class, FieldFactory.getFieldCreate().createScalar(ScalarType.pvInt)) {

            @Override
            public VInt createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	// TODO temp
            	return new VInt() {

					@Override
					public AlarmSeverity getAlarmSeverity() {
						// TODO Auto-generated method stub
						return AlarmSeverity.UNDEFINED;
					}

					@Override
					public AlarmStatus getAlarmStatus() {
						// TODO Auto-generated method stub
						return AlarmStatus.UNDEFINED;
					}

					@Override
					public TimeStamp getTimeStamp() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Timestamp getTimestamp() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Integer getTimeUserTag() {
						return null;
					}

					@Override
					public boolean isTimeValid() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public Double getLowerDisplayLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getLowerCtrlLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getLowerAlarmLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getLowerWarningLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public String getUnits() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public NumberFormat getFormat() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getUpperWarningLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getUpperAlarmLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getUpperCtrlLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Double getUpperDisplayLimit() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Integer getValue() {
						return message.getIntField("value").get();
					}
            		
            	};
            }
        };

    private static final Set<PVATypeAdapter> converters;
    
    static {
        Set<PVATypeAdapter> newFactories = new HashSet<PVATypeAdapter>();
        
        // Add all SCALARs
        //newFactories.add(DBRFloatToVDouble);
        newFactories.add(DoubleToVDouble);
        /*
        newFactories.add(DBRByteToVInt);
        newFactories.add(DBRShortToVInt);
        */
        newFactories.add(IntToVInt);
        /*
        newFactories.add(DBRStringToVString);
        newFactories.add(DBRByteToVString);
        newFactories.add(DBREnumToVEnum);

        // Add all ARRAYs
        newFactories.add(DBRFloatToVFloatArray);
        newFactories.add(DBRDoubleToVDoubleArray);
        newFactories.add(DBRByteToVByteArray);
        newFactories.add(DBRShortToVShortArray);
        newFactories.add(DBRIntToVIntArray);
        newFactories.add(DBRStringToVStringArray);
        */
        converters = Collections.unmodifiableSet(newFactories);
    }
    
}
