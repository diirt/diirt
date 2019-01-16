/**
 * Copyright information and license terms for this software can be
 * found in the file LICENSE.TXT included with the distribution.
 */
package org.diirt.support.pva;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
import org.epics.pvdata.factory.StandardFieldFactory;
import org.epics.vtype.VByte;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VEnum;
import org.epics.vtype.VFloat;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VInt;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VLong;
import org.epics.vtype.VLongArray;
import org.epics.vtype.VShort;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VString;
import org.epics.vtype.VUByte;
import org.epics.vtype.VUByteArray;
import org.epics.vtype.VUInt;
import org.epics.vtype.VUIntArray;
import org.epics.vtype.VULong;
import org.epics.vtype.VULongArray;
import org.epics.vtype.VUShort;
import org.epics.vtype.VUShortArray;

import static org.diirt.support.pva.PVAToVTypes.*;
/**
 *
 * @author msekoranja
 */
public class PVAVTypeAdapterSet implements PVATypeAdapterSet {

    private static final FieldCreate fieldCreate = FieldFactory.getFieldCreate();

    @Override
    public Set<PVATypeAdapter> getAdapters() {
        return converters;
    }

    // String types
    //--------------
    
    final static PVATypeAdapter vStringAdapter = new PVATypeAdapter(VString.class,
            new String[]{"epics:nt/NTScalar:1.", "string"},
            fieldCreate.createScalar(ScalarType.pvString)) {
        @Override
        public VString createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vStringOf(valueField, message, disconnected);
            } else {
                return vStringOf(message, disconnected);
            }
        }
    };

    // Numeric scalars
    //-----------------
    
    final static PVATypeAdapter vDoubleAdapter = new PVATypeAdapter(VDouble.class,
            new String[]{"epics:nt/NTScalar:1.", "double"},
            fieldCreate.createScalar(ScalarType.pvDouble)) {
        @Override
        public VDouble createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vDoubleOf(valueField, message, disconnected);
            } else {
                return vDoubleOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vFloatAdapter = new PVATypeAdapter(VFloat.class,
            new String[]{"epics:nt/NTScalar:1.", "float"},
            fieldCreate.createScalar(ScalarType.pvFloat)) {
        @Override
        public VFloat createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vFloatOf(valueField, message, disconnected);
            } else {
                return vFloatOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vULongAdapter = new PVATypeAdapter(VULong.class,
            new String[]{"epics:nt/NTScalar:1.", "ulong"},
            fieldCreate.createScalar(ScalarType.pvULong)) {
        @Override
        public VULong createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vULongOf(valueField, message, disconnected);
            } else {
                return vULongOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vLongAdapter = new PVATypeAdapter(VLong.class,
            new String[]{"epics:nt/NTScalar:1.", "long"},
            fieldCreate.createScalar(ScalarType.pvLong)) {
        @Override
        public VLong createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vLongOf(valueField, message, disconnected);
            } else {
                return vLongOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vUIntAdapter = new PVATypeAdapter(VUInt.class,
            new String[]{"epics:nt/NTScalar:1.", "uint"},
            fieldCreate.createScalar(ScalarType.pvUInt)) {
        @Override
        public VUInt createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vUIntOf(valueField, message, disconnected);
            } else {
                return vUIntOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vIntAdapter = new PVATypeAdapter(VInt.class,
            new String[]{"epics:nt/NTScalar:1.", "int"},
            fieldCreate.createScalar(ScalarType.pvInt)) {
        @Override
        public VInt createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vIntOf(valueField, message, disconnected);
            } else {
                return vIntOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vUShortAdapter = new PVATypeAdapter(VUShort.class,
            new String[]{"epics:nt/NTScalar:1.", "ushort"},
            fieldCreate.createScalar(ScalarType.pvUShort)) {
        @Override
        public VUShort createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vUShortOf(valueField, message, disconnected);
            } else {
                return vUShortOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vShortAdapter = new PVATypeAdapter(VShort.class,
            new String[]{"epics:nt/NTScalar:1.", "short"},
            fieldCreate.createScalar(ScalarType.pvShort)) {
        @Override
        public VShort createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vShortOf(valueField, message, disconnected);
            } else {
                return vShortOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vUByteAdapter = new PVATypeAdapter(VUByte.class,
            new String[]{"epics:nt/NTScalar:1.", "ubyte"},
            fieldCreate.createScalar(ScalarType.pvUByte)) {
        @Override
        public VUByte createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vUByteOf(valueField, message, disconnected);
            } else {
                return vUByteOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vByteAdapter = new PVATypeAdapter(VByte.class,
            new String[]{"epics:nt/NTScalar:1.", "byte"},
            fieldCreate.createScalar(ScalarType.pvByte)) {
        @Override
        public VByte createValue(PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return vByteOf(valueField, message, disconnected);
            } else {
                return vByteOf(message, disconnected);
            }
        }
    };

    // Numeric arrays
    //--------------
    
    final static PVATypeAdapter vDoubleArrayAdapter = new PVATypeAdapter(VDoubleArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "double[]"},
            fieldCreate.createScalarArray(ScalarType.pvDouble)) {
        @Override
        public VDoubleArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vDoubleArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vDoubleArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vFloatArrayAdapter = new PVATypeAdapter(VFloatArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "float[]"},
            fieldCreate.createScalarArray(ScalarType.pvFloat)) {
        @Override
        public VFloatArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vFloatArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vFloatArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vULongArrayAdapter = new PVATypeAdapter(VULongArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "ulong[]"},
            fieldCreate.createScalarArray(ScalarType.pvULong)) {
        @Override
        public VULongArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vULongArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vULongArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vLongArrayAdapter = new PVATypeAdapter(VLongArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "long[]"},
            fieldCreate.createScalarArray(ScalarType.pvLong)) {
        @Override
        public VLongArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vLongArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vLongArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vUIntArrayAdapter = new PVATypeAdapter(VUIntArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "uint[]"},
            fieldCreate.createScalarArray(ScalarType.pvUInt)) {
        @Override
        public VUIntArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vUIntArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vUIntArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vIntArrayAdapter = new PVATypeAdapter(VIntArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "int[]"},
            fieldCreate.createScalarArray(ScalarType.pvInt)) {
        @Override
        public VIntArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vIntArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vIntArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vUShortArrayAdapter = new PVATypeAdapter(VUShortArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "ushort[]"},
            fieldCreate.createScalarArray(ScalarType.pvUShort)) {
        @Override
        public VUShortArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vUShortArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vUShortArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vShortArrayAdapter = new PVATypeAdapter(VShortArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "short[]"},
            fieldCreate.createScalarArray(ScalarType.pvShort)) {
        @Override
        public VShortArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vShortArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vShortArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vUByteArrayAdapter = new PVATypeAdapter(VUByteArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "ubyte[]"},
            fieldCreate.createScalarArray(ScalarType.pvUByte)) {
        @Override
        public VUByteArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vUByteArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vUByteArrayOf(message, disconnected);
            }
        }
    };

    final static PVATypeAdapter vByteArrayAdapter = new PVATypeAdapter(VByteArray.class,
            new String[]{"epics:nt/NTScalarArray:1.", "byte[]"},
            fieldCreate.createScalarArray(ScalarType.pvByte)) {
        @Override
        public VByteArray createValue(final PVStructure message, PVField valueField, boolean disconnected) {
            if (valueField != null) {
                return PVAToVTypes.vByteArrayOf(valueField, message, disconnected);
            } else {
                return PVAToVTypes.vByteArrayOf(message, disconnected);
            }
        }
    };

    // Enum types
    //--------------
    
    final static PVATypeAdapter vEnumAdapter = new PVATypeAdapter(VEnum.class,
            new String[]{"epics:nt/NTEnum:1.", "enum_t"},
            StandardFieldFactory.getStandardField().enumerated()) {
        @Override
        public VEnum createValue(PVStructure message, PVField valueField, boolean disconnected) {
            return PVAToVTypes.vEnumOf(message, disconnected);
        }
    };

    public static final Set<PVATypeAdapter> converters;

    static {
        // preserve order
        Set<PVATypeAdapter> newFactories = new HashSet<PVATypeAdapter>();

        // Add all SCALARs
        newFactories.add(vStringAdapter);

        newFactories.add(vDoubleAdapter);
        newFactories.add(vFloatAdapter);
        newFactories.add(vULongAdapter);
        newFactories.add(vLongAdapter);
        newFactories.add(vUIntAdapter);
        newFactories.add(vIntAdapter);
        newFactories.add(vUShortAdapter);
        newFactories.add(vShortAdapter);
        newFactories.add(vUByteAdapter);
        newFactories.add(vByteAdapter);

        newFactories.add(vEnumAdapter);

        // Add all ARRAYs
        newFactories.add(vDoubleArrayAdapter);
        newFactories.add(vFloatArrayAdapter);
        newFactories.add(vULongArrayAdapter);
        newFactories.add(vLongArrayAdapter);
        newFactories.add(vUIntArrayAdapter);
        newFactories.add(vIntArrayAdapter);
        newFactories.add(vUShortArrayAdapter);
        newFactories.add(vShortArrayAdapter);
        newFactories.add(vUByteArrayAdapter);
        newFactories.add(vByteArrayAdapter);

        converters = Collections.unmodifiableSet(newFactories);
    }
}
