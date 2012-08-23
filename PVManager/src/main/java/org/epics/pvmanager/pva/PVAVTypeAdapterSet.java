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

/**
 *
 * @author carcassi
 */
public class PVAVTypeAdapterSet implements PVATypeAdapterSet {
    
    @Override
    public Set<PVATypeAdapter> getAdapters() {
        return converters;
    }
    

    private static final Set<PVATypeAdapter> converters;
    
    
    static {
        Set<PVATypeAdapter> newFactories = new HashSet<PVATypeAdapter>();
        /*
        // Add all SCALARs
        newFactories.add(DBRFloatToVDouble);
        newFactories.add(DBRDoubleToVDouble);
        newFactories.add(DBRByteToVInt);
        newFactories.add(DBRShortToVInt);
        newFactories.add(DBRIntToVInt);
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
