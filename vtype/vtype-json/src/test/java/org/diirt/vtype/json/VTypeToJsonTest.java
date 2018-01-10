/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.json;

import org.diirt.vtype.json.VTypeToJson;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import org.diirt.util.array.ArrayBoolean;
import org.diirt.util.array.ArrayByte;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ArrayLong;
import org.diirt.util.array.ArrayShort;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.Time;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VBooleanArray;
import org.diirt.vtype.VByte;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VEnumArray;
import org.diirt.vtype.VFloat;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VLongArray;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VShortArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.VTypeValueEquals;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.diirt.vtype.ValueFactory.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class VTypeToJsonTest {

    public void compareJson(JsonObject json, String text) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(json);
        assertThat(writer.toString(), equalTo(text));
    }

    public void compareVType(VType expected, VType actual) {
        assertThat("Type mismatch", VTypeValueEquals.typeEquals(actual, expected), equalTo(true));
        assertThat("Value mismatch", VTypeValueEquals.valueEquals(actual, expected), equalTo(true));
        if (expected instanceof Alarm) {
            assertThat("Alarm mismatch", VTypeValueEquals.alarmEquals((Alarm) actual, (Alarm) expected), equalTo(true));
        }
        if (expected instanceof Time) {
            assertThat("Time mismatch", VTypeValueEquals.timeEquals((Time) actual, (Time) expected), equalTo(true));
        }
    }

    public JsonObject parseJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        }
    }

    public VDouble vDouble = newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vDoubleJson = "{\"type\":{\"name\":\"VDouble\",\"version\":1},"
            + "\"value\":3.14,"
            + "\"alarm\":{\"severity\":\"MINOR\",\"status\":\"LOW\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VFloat vFloat = newVFloat((float) 3.125, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vFloatJson = "{\"type\":{\"name\":\"VFloat\",\"version\":1},"
            + "\"value\":3.125,"
            + "\"alarm\":{\"severity\":\"MINOR\",\"status\":\"HIGH\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VLong vLong = newVLong(313L, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vLongJson = "{\"type\":{\"name\":\"VLong\",\"version\":1},"
            + "\"value\":313,"
            + "\"alarm\":{\"severity\":\"MINOR\",\"status\":\"HIGH\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VInt vInt = newVInt(314, alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vIntJson = "{\"type\":{\"name\":\"VInt\",\"version\":1},"
            + "\"value\":314,"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VShort vShort = newVShort((short) 314, alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vShortJson = "{\"type\":{\"name\":\"VShort\",\"version\":1},"
            + "\"value\":314,"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VByte vByte = newVByte((byte) 31, alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vByteJson = "{\"type\":{\"name\":\"VByte\",\"version\":1},"
            + "\"value\":31,"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VBoolean vBoolean = newVBoolean(true, alarmNone(), newTime(Instant.ofEpochSecond(0, 0)));
    public String vBooleanJson = "{\"type\":{\"name\":\"VBoolean\",\"version\":1},"
            + "\"value\":true,"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VString vString = newVString("Flower", alarmNone(), newTime(Instant.ofEpochSecond(0, 0)));
    public String vStringJson = "{\"type\":{\"name\":\"VString\",\"version\":1},"
            + "\"value\":\"Flower\","
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VEnum vEnum = newVEnum(1, Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)));
    public String vEnumJson = "{\"type\":{\"name\":\"VEnum\",\"version\":1},"
            + "\"value\":1,"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"enum\":{\"labels\":[\"One\",\"Two\",\"Three\"]}}";
    public VDoubleArray vDoubleArray = newVDoubleArray(new ArrayDouble(0.0, 0.1, 0.2), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vDoubleArrayJson = "{\"type\":{\"name\":\"VDoubleArray\",\"version\":1},"
            + "\"value\":[0.0,0.1,0.2],"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VFloatArray vFloatArray = newVFloatArray(new ArrayFloat(new float[] {0, 1, 2}), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vFloatArrayJson = "{\"type\":{\"name\":\"VFloatArray\",\"version\":1},"
                + "\"value\":[0.0,1.0,2.0],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VLongArray vLongArray = newVLongArray(new ArrayLong(new long[] {0, 1, 2}), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vLongArrayJson = "{\"type\":{\"name\":\"VLongArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VIntArray vIntArray = newVIntArray(new ArrayInt(new int[] {0, 1, 2}), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vIntArrayJson = "{\"type\":{\"name\":\"VIntArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VShortArray vShortArray = newVShortArray(new ArrayShort(new short[] {0, 1, 2}), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vShortArrayJson = "{\"type\":{\"name\":\"VShortArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VNumberArray vByteArray = newVNumberArray(new ArrayByte(new byte[]{0, 1, 2}), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)), displayNone());
    public String vByteArrayJson = "{\"type\":{\"name\":\"VByteArray\",\"version\":1},"
            + "\"value\":[0,1,2],"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VBooleanArray vBooleanArray = newVBooleanArray(new ArrayBoolean(true, false, true), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)));
    public String vBooleanArrayJson = "{\"type\":{\"name\":\"VBooleanArray\",\"version\":1},"
            + "\"value\":[true,false,true],"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VStringArray vStringArray = newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)));
    public String vStringArrayJson = "{\"type\":{\"name\":\"VStringArray\",\"version\":1},"
            + "\"value\":[\"A\",\"B\",\"C\"],"
            + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VEnumArray vEnumArray = newVEnumArray(new ArrayInt(1,0,1), Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Instant.ofEpochSecond(0, 0)));
    public String vEnumArrayJson = "{\"type\":{\"name\":\"VEnumArray\",\"version\":1},"
            + "\"value\":[1,0,1],\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
            + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "\"enum\":{\"labels\":[\"One\",\"Two\",\"Three\"]}}";
    public VTable vTable = newVTable(Arrays.<Class<?>>asList(String.class, int.class, double.class), Arrays.asList("Name", "Index", "Value"), Arrays.asList(Arrays.asList("A", "B", "C"), new ArrayInt(1,2,3), new ArrayDouble(3.14, 1.25, -0.1)));
    public String vTableJson = "{\"type\":{\"name\":\"VTable\",\"version\":1},"
            + "\"columnNames\":[\"Name\",\"Index\",\"Value\"],"
            + "\"columnTypes\":[\"String\",\"int\",\"double\"],"
            + "\"columnValues\":[[\"A\",\"B\",\"C\"],[1,2,3],[3.14,1.25,-0.1]]}";
    public VTable vTable2 = newVTable(Arrays.<Class<?>>asList(String.class, int.class, double.class, Instant.class), Arrays.asList("Name", "Index", "Value", "Timestamp"), Arrays.asList(Arrays.asList("A", "B", "C"), new ArrayInt(1,2,3), new ArrayDouble(3.14, 1.25, -0.1), Arrays.asList(Instant.ofEpochSecond(1234, 0), Instant.ofEpochSecond(2345, 0), Instant.ofEpochSecond(3456, 0))));
    public String vTable2Json = "{\"type\":{\"name\":\"VTable\",\"version\":1},"
            + "\"columnNames\":[\"Name\",\"Index\",\"Value\",\"Timestamp\"],"
            + "\"columnTypes\":[\"String\",\"int\",\"double\",\"Timestamp\"],"
            + "\"columnValues\":[[\"A\",\"B\",\"C\"],[1,2,3],[3.14,1.25,-0.1],[1234,2345,3456]]}";
    public VTable vTable3 = newVTable(Arrays.<Class<?>>asList(String.class, int.class, double.class, Instant.class), Arrays.asList("Name", "Index", "Value", "Timestamp"), Arrays.asList(Arrays.asList(null, "B", "C"), new ArrayInt(1,2,3), new ArrayDouble(Double.NaN, 1.25, -0.1), Arrays.asList(Instant.ofEpochSecond(1234, 0), Instant.ofEpochSecond(2345, 0), Instant.ofEpochSecond(3456, 0))));
    public String vTable3Json = "{\"type\":{\"name\":\"VTable\",\"version\":1},"
            + "\"columnNames\":[\"Name\",\"Index\",\"Value\",\"Timestamp\"],"
            + "\"columnTypes\":[\"String\",\"int\",\"double\",\"Timestamp\"],"
            + "\"columnValues\":[[\"\",\"B\",\"C\"],[1,2,3],[null,1.25,-0.1],[1234,2345,3456]]}";

    @Test
    public void serializeVDouble() {
        compareJson(VTypeToJson.toJson(vDouble), vDoubleJson);
    }

    @Test
    public void serializeVFloat() {
        compareJson(VTypeToJson.toJson(vFloat), vFloatJson);
    }

    @Test
    public void serializeVLong() {
        compareJson(VTypeToJson.toJson(vLong), vLongJson);
    }

    @Test
    public void serializeVInt() {
        compareJson(VTypeToJson.toJson(vInt), vIntJson);
    }

    @Test
    public void serializeVShort() {
        compareJson(VTypeToJson.toJson(vShort), vShortJson);
    }

    @Test
    public void serializeVByte() {
        compareJson(VTypeToJson.toJson(vByte), vByteJson);
    }

    @Test
    public void serializeVBoolean() {
        compareJson(VTypeToJson.toJson(vBoolean), vBooleanJson);
    }

    @Test
    public void serializeVString() {
        compareJson(VTypeToJson.toJson(vString), vStringJson);
    }

    @Test
    public void serializeVEnum() {
        compareJson(VTypeToJson.toJson(vEnum), vEnumJson);
    }

    @Test
    public void serializeVDoubleArray() {
        compareJson(VTypeToJson.toJson(vDoubleArray), vDoubleArrayJson);
    }

    @Test
    public void serializeVFloatArray() {
        compareJson(VTypeToJson.toJson(vFloatArray), vFloatArrayJson);
    }

    @Test
    public void serializeVLongArray() {
        compareJson(VTypeToJson.toJson(vLongArray), vLongArrayJson);
    }

    @Test
    public void serializeVIntArray() {
        compareJson(VTypeToJson.toJson(vIntArray), vIntArrayJson);
    }

    @Test
    public void serializeVShortArray() {
        compareJson(VTypeToJson.toJson(vShortArray), vShortArrayJson);
    }

    @Test
    public void serializeVByteArray() {
        compareJson(VTypeToJson.toJson(vByteArray), vByteArrayJson);
    }

    @Test
    public void serializeVBooleanArray() {
        compareJson(VTypeToJson.toJson(vBooleanArray), vBooleanArrayJson);
    }

    @Test
    public void serializeVStringArray() {
        compareJson(VTypeToJson.toJson(vStringArray), vStringArrayJson);
    }

    @Test
    public void serializeVEnumArray() {
        compareJson(VTypeToJson.toJson(vEnumArray), vEnumArrayJson);
    }

    @Test
    public void serializeVTable1() {
        compareJson(VTypeToJson.toJson(vTable), vTableJson);
    }

    @Test
    public void serializeVTable2() {
        compareJson(VTypeToJson.toJson(vTable2), vTable2Json);
    }

    @Test
    public void serializeVTable3() {
        compareJson(VTypeToJson.toJson(vTable3), vTable3Json);
    }

    @Test
    public void parseVDouble() {
        compareVType(vDouble, VTypeToJson.toVType(parseJson(vDoubleJson)));
    }

    @Test
    public void parseVFloat() {
        compareVType(vFloat, VTypeToJson.toVType(parseJson(vFloatJson)));
    }

    @Test
    public void parseVLong() {
        compareVType(vLong, VTypeToJson.toVType(parseJson(vLongJson)));
    }

    @Test
    public void parseVInt() {
        compareVType(vInt, VTypeToJson.toVType(parseJson(vIntJson)));
    }

    @Test
    public void parseVShort() {
        compareVType(vShort, VTypeToJson.toVType(parseJson(vShortJson)));
    }

    @Test
    public void parseVByte() {
        compareVType(vByte, VTypeToJson.toVType(parseJson(vByteJson)));
    }

    @Test
    public void parseVBoolean() {
        compareVType(vBoolean, VTypeToJson.toVType(parseJson(vBooleanJson)));
    }

    @Test
    public void parseVString() {
        compareVType(vString, VTypeToJson.toVType(parseJson(vStringJson)));
    }

    @Test
    public void parseVEnum() {
        compareVType(vEnum, VTypeToJson.toVType(parseJson(vEnumJson)));
    }

    @Test
    public void parseVDoubleArray() {
        compareVType(vDoubleArray, VTypeToJson.toVType(parseJson(vDoubleArrayJson)));
    }

    @Test
    public void parseVFloatArray() {
        compareVType(vFloatArray, VTypeToJson.toVType(parseJson(vFloatArrayJson)));
    }

    @Test
    public void parseVLongArray() {
        compareVType(vLongArray, VTypeToJson.toVType(parseJson(vLongArrayJson)));
    }

    @Test
    public void parseVIntArray() {
        compareVType(vIntArray, VTypeToJson.toVType(parseJson(vIntArrayJson)));
    }

    @Test
    public void parseVShortArray() {
        compareVType(vShortArray, VTypeToJson.toVType(parseJson(vShortArrayJson)));
    }

    @Test
    public void parseVByteArray() {
        compareVType(vByteArray, VTypeToJson.toVType(parseJson(vByteArrayJson)));
    }

    @Test
    public void parseVBooleanArray() {
        compareVType(vBooleanArray, VTypeToJson.toVType(parseJson(vBooleanArrayJson)));
    }

    @Test
    public void parseVStringArray() {
        compareVType(vStringArray, VTypeToJson.toVType(parseJson(vStringArrayJson)));
    }

    @Test
    public void parseVEnumArray() {
        compareVType(vEnumArray, VTypeToJson.toVType(parseJson(vEnumArrayJson)));
    }

    @Test
    public void parseVTable() {
        compareVType(vTable, VTypeToJson.toVType(parseJson(vTableJson)));
    }

    @Test
    public void parseVTable2() {
        compareVType(vTable2, VTypeToJson.toVType(parseJson(vTable2Json)));
    }

}
