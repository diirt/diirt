/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.pva;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.pva.adapters.PVFieldToVDouble;
import org.epics.pvmanager.pva.adapters.PVFieldToVInt;

/**
 *
 * @author carcassi
 */
public class PVAVTypeAdapterSet implements PVATypeAdapterSet {
    
    @Override
    public Set<PVATypeAdapter> getAdapters() {
        return converters;
    }
    
    //  -> VDouble
    final static PVATypeAdapter ToVDouble = new PVATypeAdapter(VDouble.class, new String[] { "NTScalar", "scalar_t", "structure" }, FieldFactory.getFieldCreate().createScalar(ScalarType.pvDouble)) {

            @Override
            public VDouble createValue(PVStructure message, Field valueType, boolean disconnected) {
                return new PVFieldToVDouble(message, disconnected);
            }
        };

    //  -> VInt
    final static PVATypeAdapter ToVInt = new PVATypeAdapter(VInt.class, new String[] { "NTScalar", "scalar_t", "structure" }, FieldFactory.getFieldCreate().createScalar(ScalarType.pvInt)) {

            @Override
            public VInt createValue(final PVStructure message, Field valueType, boolean disconnected) {
            	return new PVFieldToVInt(message, disconnected);
            }
        };

    private static final Set<PVATypeAdapter> converters;
    
    static {
        Set<PVATypeAdapter> newFactories = new HashSet<PVATypeAdapter>();
        
        // Add all SCALARs
        //newFactories.add(DBRFloatToVDouble);
        newFactories.add(ToVDouble);
        /*
        newFactories.add(DBRByteToVInt);
        newFactories.add(DBRShortToVInt);
        */
        newFactories.add(ToVInt);
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
