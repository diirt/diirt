/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import org.epics.pvmanager.DataSourceTypeAdapterSet;
import gov.aps.jca.dbr.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.epics.pvmanager.data.*;

/**
 *
 * @author carcassi
 */
public class JCAVTypeAdapterSet implements DataSourceTypeAdapterSet {
    
    @Override
    public Set<JCATypeAdapter> getConverters() {
        return converters;
    }
    
    // DBR_TIME_Float -> VDouble
    static JCATypeAdapter DBRFloatToVDouble = new JCATypeAdapter(VDouble.class, DBR_TIME_Float.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VDouble createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VDoubleFromDbr((DBR_TIME_Float) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        };

    // DBR_CTRL_Double -> VDouble
    static JCATypeAdapter DBRDoubleToVDouble = new JCATypeAdapter(VDouble.class, DBR_TIME_Double.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VDouble createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VDoubleFromDbr((DBR_TIME_Double) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        };
    
    // DBR_TIME_Byte -> VInt
    static JCATypeAdapter DBRByteToVInt = new JCATypeAdapter(VInt.class, DBR_TIME_Byte.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VInt createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntFromDbr((DBR_TIME_Byte) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        };
    
    private static final Set<JCATypeAdapter> converters;
    
    static {
        Set<JCATypeAdapter> newFactories = new HashSet<JCATypeAdapter>();
        // Add all SCALARs
        newFactories.add(DBRFloatToVDouble);
        newFactories.add(DBRDoubleToVDouble);
        newFactories.add(DBRByteToVInt);
        // DBR_CTRL_Short -> VInt
        newFactories.add(new JCATypeAdapter(VInt.class, DBR_TIME_Short.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VInt createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntFromDbr((DBR_TIME_Short) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        // DBR_CTRL_Int -> VInt
        newFactories.add(new JCATypeAdapter(VInt.class, DBR_TIME_Int.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VInt createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntFromDbr((DBR_TIME_Int) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VString.class, DBR_TIME_String.TYPE, null, false) {

            @Override
            public VString createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VStringFromDbr((DBR_TIME_String) value, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VEnum.class, DBR_TIME_Enum.TYPE, DBR_LABELS_Enum.TYPE, false) {

            @Override
            public VEnum createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VEnumFromDbr((DBR_TIME_Enum) value, (DBR_LABELS_Enum) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VDoubleArray.class, DBR_TIME_Double.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VDoubleArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VDoubleArrayFromDbr((DBR_TIME_Double) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VFloatArray.class, DBR_TIME_Float.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VFloatArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VFloatArrayFromDbr((DBR_TIME_Float) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VByteArray.class, DBR_TIME_Byte.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VByteArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VByteArrayFromDbr((DBR_TIME_Byte) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VShortArray.class, DBR_TIME_Short.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VShortArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VShortArrayFromDbr((DBR_TIME_Short) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VIntArray.class, DBR_TIME_Int.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VIntArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntArrayFromDbr((DBR_TIME_Int) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeAdapter(VStringArray.class, DBR_TIME_String.TYPE, null, true) {

            @Override
            public VStringArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VStringArrayFromDbr((DBR_TIME_String) value, disconnected);
            }
        });
        converters = Collections.unmodifiableSet(newFactories);
    }
    
}
