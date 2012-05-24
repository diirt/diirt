/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import edu.emory.mathcs.backport.java.util.Collections;
import gov.aps.jca.Channel;
import gov.aps.jca.dbr.*;
import java.util.HashSet;
import java.util.Set;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.*;

/**
 *
 * @author carcassi
 */
public class JCAVTypeSupport implements DataSourceTypeAdapterSet {
    
    private static final Set<JCATypeConverter> converters;
    
    @Override
    public Set<JCATypeConverter> getConverters() {
        return converters;
    }
    
    static {
        Set<JCATypeConverter> newFactories = new HashSet<JCATypeConverter>();
        // Add all SCALARs
        // DBR_TIME_Float -> VDouble
        newFactories.add(new JCATypeConverter(VDouble.class, DBR_TIME_Float.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VDouble createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VDoubleFromDbr((DBR_TIME_Float) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        // DBR_CTRL_Double -> VDouble
        newFactories.add(new JCATypeConverter(VDouble.class, DBR_TIME_Double.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VDouble createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VDoubleFromDbr((DBR_TIME_Double) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        // DBR_TIME_Byte -> VInt
        newFactories.add(new JCATypeConverter(VInt.class, DBR_TIME_Byte.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VInt createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntFromDbr((DBR_TIME_Byte) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        // DBR_CTRL_Short -> VInt
        newFactories.add(new JCATypeConverter(VInt.class, DBR_TIME_Short.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VInt createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntFromDbr((DBR_TIME_Short) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        // DBR_CTRL_Int -> VInt
        newFactories.add(new JCATypeConverter(VInt.class, DBR_TIME_Int.TYPE, DBR_CTRL_Double.TYPE, false) {

            @Override
            public VInt createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntFromDbr((DBR_TIME_Int) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VString.class, DBR_TIME_String.TYPE, null, false) {

            @Override
            public VString createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VStringFromDbr((DBR_TIME_String) value, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VEnum.class, DBR_TIME_Enum.TYPE, DBR_LABELS_Enum.TYPE, false) {

            @Override
            public VEnum createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VEnumFromDbr((DBR_TIME_Enum) value, (DBR_LABELS_Enum) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VDoubleArray.class, DBR_TIME_Double.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VDoubleArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VDoubleArrayFromDbr((DBR_TIME_Double) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VFloatArray.class, DBR_TIME_Float.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VFloatArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VFloatArrayFromDbr((DBR_TIME_Float) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VByteArray.class, DBR_TIME_Byte.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VByteArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VByteArrayFromDbr((DBR_TIME_Byte) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VShortArray.class, DBR_TIME_Short.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VShortArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VShortArrayFromDbr((DBR_TIME_Short) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VIntArray.class, DBR_TIME_Int.TYPE, DBR_CTRL_Double.TYPE, true) {

            @Override
            public VIntArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VIntArrayFromDbr((DBR_TIME_Int) value, (DBR_CTRL_Double) metadata, disconnected);
            }
        });
        newFactories.add(new JCATypeConverter(VStringArray.class, DBR_TIME_String.TYPE, null, true) {

            @Override
            public VStringArray createValue(DBR value, DBR metadata, boolean disconnected) {
                return new VStringArrayFromDbr((DBR_TIME_String) value, disconnected);
            }
        });
        converters = Collections.unmodifiableSet(newFactories);
    }
    
}
