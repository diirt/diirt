/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.epics.pvdata.factory.ConvertFactory;
import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.Convert;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.LongArrayData;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.pvdata.pv.PVULongArray;
import org.epics.pvdata.pv.PVUShortArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.StringArrayData;
import org.epics.util.array.ArrayByte;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayFloat;
import org.epics.util.array.ArrayInteger;
import org.epics.util.array.ArrayLong;
import org.epics.util.array.ArrayShort;
import org.epics.util.array.ArrayUByte;
import org.epics.util.array.ArrayUInteger;
import org.epics.util.array.ArrayULong;
import org.epics.util.array.ArrayUShort;
import org.epics.util.stats.Range;
import org.epics.util.text.NumberFormats;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.AlarmStatus;
import org.epics.vtype.Display;
import org.epics.vtype.EnumDisplay;
import org.epics.vtype.Time;
import org.epics.vtype.VByte;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
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

/**
 *
 * Utility class to convert Normative Type structures from PVData to VTypes.
 * 
 * @author carcassi
 */
public class PVAToVTypes {

    /**
     * Extracts the time information from the given PVStructure.
     * <p>
     * It expects a substructure with name {@code timeStamp} of type {@code timeStamp_t}.
     * If it's not found, the current time is returned.
     * 
     * @param pvField the root field
     * @return the time information
     */
    public static Time timeOf(PVStructure pvField) {
        // Expect a timeStamp field of type timeStamp_t
        PVStructure timeStampStructure = (pvField != null) ? pvField.getStructureField("timeStamp") : null;
        if (timeStampStructure != null) {
            Instant timestamp;
            boolean timeValid;
            Integer timeUserTag;

            // Extract the time
            PVLong secsField = timeStampStructure.getLongField("secondsPastEpoch");
            PVInt nanosField = timeStampStructure.getIntField("nanoseconds");
            if (secsField != null && nanosField != null) {
                timestamp = Instant.ofEpochSecond(secsField.get(), nanosField.get());
                timeValid = true;
            } else {
                timestamp = Instant.ofEpochSecond(0);
                timeValid = false;
            }

            // Extract the user tag
            PVInt userTagField = timeStampStructure.getIntField("userTag");
            if (userTagField != null) {
                timeUserTag = userTagField.get();
            } else {
                timeUserTag = null;
            }

            return Time.of(timestamp, timeUserTag, timeValid);
        } else {
            // No time found
            return Time.now();
        }
    }
    
    // Conversion table from pva AlarmSeverity to vType AlarmSeverity
    private static final List<AlarmSeverity> FROM_PVA_SEVERITY = Arrays.asList(AlarmSeverity.NONE,
            AlarmSeverity.MINOR,
            AlarmSeverity.MAJOR,
            AlarmSeverity.INVALID,
            AlarmSeverity.UNDEFINED);

    // Conversion table from pva AlarmStatus to vType AlarmStatus
    private static final List<AlarmStatus> FROM_PVA_STATUS = Arrays.asList(AlarmStatus.NONE,
            AlarmStatus.DEVICE,
            AlarmStatus.DRIVER,
            AlarmStatus.RECORD,
            AlarmStatus.DB,
            AlarmStatus.CONF,
            AlarmStatus.UNDEFINED,
            AlarmStatus.CLIENT);

    /**
     * Extracts the alarm information from the given PVStructure.
     * <p>
     * It expects a substructure with name {@code alarm} of type {@code alarm_t}.
     * If it's not found, no alarm is returned. If disconnected, disconnected is
     * returned.
     * 
     * @param pvField the root field
     * @param disconnected whether the channel is disconnected
     * @return the alarm information
     */
    public static Alarm alarmOf(PVStructure pvField, boolean disconnected) {
        if (disconnected) {
            return Alarm.disconnected();
        }
        
        // Expect an alarm field of type alarm_t
        PVStructure alarmStructure = (pvField != null) ? pvField.getStructureField("alarm") : null;
        if (alarmStructure != null) {
            AlarmSeverity alarmSeverity;
            AlarmStatus alarmStatus;
            String name;
            
            PVInt severityField = alarmStructure.getIntField("severity");
            if (severityField == null) {
                alarmSeverity = AlarmSeverity.UNDEFINED;
            } else {
                alarmSeverity = FROM_PVA_SEVERITY.get(severityField.get());
            }

            PVInt statusField = alarmStructure.getIntField("status");
            if (statusField == null) {
                alarmStatus = AlarmStatus.UNDEFINED;
            } else {
                alarmStatus = FROM_PVA_STATUS.get(statusField.get());
            }

            PVString messageField = alarmStructure.getStringField("message");
            if (messageField == null) {
                name = "";
            } else {
                name = messageField.get();
            }
            
            return Alarm.of(alarmSeverity, alarmStatus, name);
        } else {
            return Alarm.none();
        }
    }
    
    /**
     * Extracts the numeric display information from the given PVStructure.
     * <p>
     * It expects the following substructures:
     * <ul>
     *   <li>{@code display} field of type {@code display_t} containing display range, units and format</li>
     *   <li>{@code valueAlarm} field of type {@code valueAlarm_t} containing alarm ranges</li>
     *   <li>{@code control} field of type {@code control_t} containing the control range</li>
     * </ul>
     * The undefined range is used for missing ranges. The default unit and
     * format are used if no unit and/or format are found.
     * 
     * @param pvField the root field
     * @return the display information
     */
    public static Display displayOf(PVStructure pvField) {
        if (pvField == null) {
            return Display.none();
        }
        
        Range controlRange;
        Range displayRange;
        Range alarmRange;
        Range warningRange;
        NumberFormat format;
        String units;
        
        // Expect a display field of type display_t
        PVStructure displayStructure = pvField.getStructureField("display");
        displayRange = rangeOf(displayStructure, "limitLow", "limitHigh");
        if (displayStructure != null) {
            PVString formatField = displayStructure.getStringField("format");
            if (formatField == null) {
                format = Display.defaultNumberFormat();
            } else {
                format = NumberFormats.printfFormat(formatField.get());
            }

            PVString unitsField = displayStructure.getStringField("units");
            if (unitsField == null || unitsField.get() == null) {
                units = Display.defaultUnits();
            } else {
                units = unitsField.get();
            }
        } else {
            format = Display.defaultNumberFormat();
            units = Display.defaultUnits();
        }

        // Expect a control field of type control_t
        controlRange = rangeOf(pvField.getStructureField("control"), "limitLow", "limitHigh");

        // Expect a valueAlarm field of type valueAlarm_t
        PVStructure valueAlarmStructure = pvField.getStructureField("valueAlarm");
        warningRange = rangeOf(valueAlarmStructure, "lowWarningLimit", "highWarningLimit");
        alarmRange = rangeOf(valueAlarmStructure, "lowAlarmLimit", "highAlarmLimit");
        
        return Display.of(displayRange, alarmRange, warningRange, controlRange, units, format);
    }
    
    private static final Convert convert = ConvertFactory.getConvert();
    
    private static double doubleValueOf(PVStructure structure, String fieldName, Double defaultValue) {
        PVField field = structure.getSubField(fieldName);
        if (field instanceof PVScalar) {
            return convert.toDouble((PVScalar) field);
        } else {
            return defaultValue;
        }
    }
    
    private static Range rangeOf(PVStructure pvStructure, String lowValueName, String highValueName) {
        if (pvStructure != null) {
            return Range.of(doubleValueOf(pvStructure, lowValueName, Double.NaN),
                    doubleValueOf(pvStructure, highValueName, Double.NaN));
        } else {
            return Range.undefined();
        }
    }

    /**
     * Converts the the given field to a {@link VString}.
     * 
     * @param pvField a field of type NTScalar string
     * @param disconnected whether the client is disconnected
     * @return a new VString
     */
    public static VString vStringOf(PVStructure pvField, boolean disconnected) {
        return vStringOf(pvField.getSubField("value"), pvField, disconnected);
    }

    /**
     * Converts the the given field and metadata to a {@link VString}.
     * 
     * @param pvField a field convertible to a string
     * @param pvMetadata the metadata structure from an NTScalar
     * @param disconnected whether the client is disconnected
     * @return a new VString
     */
    public static VString vStringOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVString) {
            return VString.of(convert.toString((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VDouble vDoubleOf(PVStructure pvField, boolean disconnected) {
        return vDoubleOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VDouble vDoubleOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VDouble.of(convert.toDouble((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VFloat vFloatOf(PVStructure pvField, boolean disconnected) {
        return vFloatOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VFloat vFloatOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VFloat.of(convert.toFloat((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VULong vULongOf(PVStructure pvField, boolean disconnected) {
        return vULongOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VULong vULongOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VULong.of(convert.toLong((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VLong vLongOf(PVStructure pvField, boolean disconnected) {
        return vLongOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VLong vLongOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VLong.of(convert.toLong((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VUInt vUIntOf(PVStructure pvField, boolean disconnected) {
        return vUIntOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VUInt vUIntOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VUInt.of(convert.toInt((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VInt vIntOf(PVStructure pvField, boolean disconnected) {
        return vIntOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VInt vIntOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VInt.of(convert.toInt((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VUShort vUShortOf(PVStructure pvField, boolean disconnected) {
        return vUShortOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VUShort vUShortOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VUShort.of(convert.toShort((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VShort vShortOf(PVStructure pvField, boolean disconnected) {
        return vShortOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VShort vShortOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VShort.of(convert.toShort((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VUByte vUByteOf(PVStructure pvField, boolean disconnected) {
        return vUByteOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VUByte vUByteOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VUByte.of(convert.toByte((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VByte vByteOf(PVStructure pvField, boolean disconnected) {
        return vByteOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VByte vByteOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVScalar) {
            return VByte.of(convert.toByte((PVScalar)pvField), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VDoubleArray vDoubleArrayOf(PVStructure pvField, boolean disconnected) {
        return vDoubleArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VDoubleArray vDoubleArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVDoubleArray) {
            PVDoubleArray valueField = (PVDoubleArray) pvField;
            DoubleArrayData data = new DoubleArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VDoubleArray.of(ArrayDouble.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VFloatArray vFloatArrayOf(PVStructure pvField, boolean disconnected) {
        return vFloatArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VFloatArray vFloatArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVFloatArray) {
            PVFloatArray valueField = (PVFloatArray) pvField;
            FloatArrayData data = new FloatArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VFloatArray.of(ArrayFloat.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VULongArray vULongArrayOf(PVStructure pvField, boolean disconnected) {
        return vULongArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VULongArray vULongArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVULongArray) {
            PVULongArray valueField = (PVULongArray) pvField;
            LongArrayData data = new LongArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VULongArray.of(ArrayULong.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VLongArray vLongArrayOf(PVStructure pvField, boolean disconnected) {
        return vLongArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VLongArray vLongArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVLongArray) {
            PVLongArray valueField = (PVLongArray) pvField;
            LongArrayData data = new LongArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VLongArray.of(ArrayLong.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VUIntArray vUIntArrayOf(PVStructure pvField, boolean disconnected) {
        return vUIntArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VUIntArray vUIntArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVUIntArray) {
            PVUIntArray valueField = (PVUIntArray) pvField;
            IntArrayData data = new IntArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VUIntArray.of(ArrayUInteger.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VIntArray vIntArrayOf(PVStructure pvField, boolean disconnected) {
        return vIntArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VIntArray vIntArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVIntArray) {
            PVIntArray valueField = (PVIntArray) pvField;
            IntArrayData data = new IntArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VIntArray.of(ArrayInteger.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VUShortArray vUShortArrayOf(PVStructure pvField, boolean disconnected) {
        return vUShortArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VUShortArray vUShortArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVUShortArray) {
            PVUShortArray valueField = (PVUShortArray) pvField;
            ShortArrayData data = new ShortArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VUShortArray.of(ArrayUShort.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VShortArray vShortArrayOf(PVStructure pvField, boolean disconnected) {
        return vShortArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VShortArray vShortArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVShortArray) {
            PVShortArray valueField = (PVShortArray) pvField;
            ShortArrayData data = new ShortArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VShortArray.of(ArrayShort.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VUByteArray vUByteArrayOf(PVStructure pvField, boolean disconnected) {
        return vUByteArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VUByteArray vUByteArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVUByteArray) {
            PVUByteArray valueField = (PVUByteArray) pvField;
            ByteArrayData data = new ByteArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VUByteArray.of(ArrayUByte.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }

    public static VByteArray vByteArrayOf(PVStructure pvField, boolean disconnected) {
        return vByteArrayOf(pvField.getSubField("value"), pvField, disconnected);
    }

    public static VByteArray vByteArrayOf(PVField pvField, PVStructure pvMetadata, boolean disconnected) {
        if (pvField instanceof PVByteArray) {
            PVByteArray valueField = (PVByteArray) pvField;
            ByteArrayData data = new ByteArrayData();
            valueField.get(0, valueField.getLength(), data);
            return VByteArray.of(ArrayByte.of(data.data), alarmOf(pvMetadata, disconnected), timeOf(pvMetadata), displayOf(pvMetadata));
        } else {
            return null;
        }
    }
    
    public static VEnum vEnumOf(PVStructure pvField, boolean disconnected) {
        int index;
        List<String> choices;
        
        PVStructure enumStructure = (pvField != null) ? pvField.getStructureField("value") : null;

        PVInt indexField = (enumStructure != null) ? enumStructure.getIntField("index") : null;
        if (indexField != null) {
            index = indexField.get();
        } else {
            index = -1;
        }

        PVStringArray choicesField = (enumStructure != null) ? (PVStringArray) enumStructure.getScalarArrayField("choices", ScalarType.pvString) : null;
        if (choicesField != null) {
            StringArrayData data = new StringArrayData();
            choicesField.get(0, choicesField.getLength(), data);
            choices = Arrays.asList(data.data);
        } else {
            choices = Collections.emptyList();
        }
        
        
        return VEnum.of(index, EnumDisplay.of(choices), alarmOf(pvField, disconnected), timeOf(pvField));
    }

}
