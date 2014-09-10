/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.vtype.json;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import org.epics.util.array.ArrayBoolean;
import org.epics.util.array.ArrayByte;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayFloat;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ArrayLong;
import org.epics.util.array.ArrayShort;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VBooleanArray;
import org.epics.vtype.VByte;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VEnum;
import org.epics.vtype.VEnumArray;
import org.epics.vtype.VFloat;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VInt;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VLong;
import org.epics.vtype.VLongArray;
import org.epics.vtype.VNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VShort;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VType;
import org.epics.vtype.VTypeValueEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.vtype.ValueFactory.*;
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
    
    public void compareVType(VType actual, VType expected) {
        assertThat("Type mismatch", VTypeValueEquals.typeEquals(expected, actual), equalTo(true));
        assertThat("Value mismatch", VTypeValueEquals.valueEquals(expected, actual), equalTo(true));
        if (expected instanceof Alarm) {
            assertThat("Alarm mismatch", VTypeValueEquals.alarmEquals((Alarm) expected, (Alarm) actual), equalTo(true));
        }
        if (expected instanceof Time) {
            assertThat("Time mismatch", VTypeValueEquals.timeEquals((Time) expected, (Time) actual), equalTo(true));
        }
    }
    
    public JsonObject parseJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        }
    }
    
    @Test
    public void testVDouble() {
        compareJson(VTypeToJson.toJson(vDouble), vDoubleJson);
    }
    
    @Test
    public void testVFloat() {
        compareJson(VTypeToJson.toJson(vFloat), vFloatJson);
    }

    @Test
    public void testVLong() {
        compareJson(VTypeToJson.toJson(vLong), vLongJson);
    }

    @Test
    public void testVInt() {
        compareJson(VTypeToJson.toJson(vInt), vIntJson);
    }

    @Test
    public void testVShort() {
        compareJson(VTypeToJson.toJson(vShort), vShortJson);
    }

    @Test
    public void testVByte() {
        compareJson(VTypeToJson.toJson(vByte), vByteJson);
    }

    @Test
    public void testVBoolean() {
        compareJson(VTypeToJson.toJson(vBoolean), vBooleanJson);
    }

    @Test
    public void testVString() {
        compareJson(VTypeToJson.toJson(vString), vStringJson);
    }
    
    public VDouble vDouble = newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(0, 0)), displayNone());
    public String vDoubleJson = "{\"type\":{\"name\":\"VDouble\",\"version\":1},\"value\":3.14,\"alarm\":{\"severity\":\"MINOR\",\"status\":\"LOW\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VFloat vFloat = newVFloat((float) 3.125, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(0, 0)), displayNone());
    public String vFloatJson = "{\"type\":{\"name\":\"VFloat\",\"version\":1},\"value\":3.125,\"alarm\":{\"severity\":\"MINOR\",\"status\":\"HIGH\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VLong vLong = newVLong(313L, newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(0, 0)), displayNone());
    public String vLongJson = "{\"type\":{\"name\":\"VLong\",\"version\":1},\"value\":313,\"alarm\":{\"severity\":\"MINOR\",\"status\":\"HIGH\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VInt vInt = newVInt(314, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vIntJson = "{\"type\":{\"name\":\"VInt\",\"version\":1},\"value\":314,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VShort vShort = newVShort((short) 314, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vShortJson = "{\"type\":{\"name\":\"VShort\",\"version\":1},\"value\":314,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VByte vByte = newVByte((byte) 31, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vByteJson = "{\"type\":{\"name\":\"VByte\",\"version\":1},\"value\":31,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VBoolean vBoolean = newVBoolean(true, alarmNone(), newTime(Timestamp.of(0, 0)));
    public String vBooleanJson = "{\"type\":{\"name\":\"VBoolean\",\"version\":1},\"value\":true,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VString vString = newVString("Flower", alarmNone(), newTime(Timestamp.of(0, 0)));
    public String vStringJson = "{\"type\":{\"name\":\"VString\",\"version\":1},\"value\":\"Flower\",\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VEnum vEnum = newVEnum(1, Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Timestamp.of(0, 0)));
    public String vEnumJson = "{\"type\":{\"name\":\"VEnum\",\"version\":1},\"value\":1,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"enum\":{\"labels\":[\"One\",\"Two\",\"Three\"]}}";
    public VStringArray vStringArray = newVStringArray(Arrays.asList("A", "B", "C"), alarmNone(), newTime(Timestamp.of(0, 0)));
    public String vStringArrayJson = "{\"type\":{\"name\":\"VStringArray\",\"version\":1},\"value\":[\"A\",\"B\",\"C\"],\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
    public VEnumArray vEnumArray = newVEnumArray(new ArrayInt(1,0,1), Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Timestamp.of(0, 0)));
    public String vEnumArrayJson = "{\"type\":{\"name\":\"VEnumArray\",\"version\":1},\"value\":[1,0,1],\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"enum\":{\"labels\":[\"One\",\"Two\",\"Three\"]}}";
    public VDoubleArray vDoubleArray = newVDoubleArray(new ArrayDouble(0.0, 0.1, 0.2), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vDoubleArrayJson = "{\"type\":{\"name\":\"VDoubleArray\",\"version\":1},\"value\":[0.0,0.1,0.2],\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VFloatArray vFloatArray = newVFloatArray(new ArrayFloat(new float[] {0, 1, 2}), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vFloatArrayJson = "{\"type\":{\"name\":\"VFloatArray\",\"version\":1},"
                + "\"value\":[0.0,1.0,2.0],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VLongArray vLongArray = newVLongArray(new ArrayLong(new long[] {0, 1, 2}), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vLongArrayJson = "{\"type\":{\"name\":\"VLongArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VNumberArray vByteArray = newVNumberArray(new ArrayByte(new byte[]{0, 1, 2}), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
    public String vByteArrayJson = "{\"type\":{\"name\":\"VByteArray\",\"version\":1},\"value\":[0,1,2],\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}";
    public VBooleanArray vBooleanArray = newVBooleanArray(new ArrayBoolean(true, false, true), alarmNone(), newTime(Timestamp.of(0, 0)));
    public String vBooleanArrayJson = "{\"type\":{\"name\":\"VBooleanArray\",\"version\":1},\"value\":[true,false,true],\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}";
        
    @Test
    public void testVEnum() {
        compareJson(VTypeToJson.toJson(vEnum), vEnumJson);
    }

    @Test
    public void testVEnumArray() {
        compareJson(VTypeToJson.toJson(vEnumArray), vEnumArrayJson);
    }

    @Test
    public void testVDoubleArray() {
        compareJson(VTypeToJson.toJson(vDoubleArray), vDoubleArrayJson);
    }

    @Test
    public void testVFloatArray() {
        compareJson(VTypeToJson.toJson(vFloatArray), vFloatArrayJson);
    }

    @Test
    public void testVLongArray() {
        compareJson(VTypeToJson.toJson(vLongArray), vLongArrayJson);
    }

    @Test
    public void testVByteArray() {
        compareJson(VTypeToJson.toJson(vByteArray), vByteArrayJson);
    }

    @Test
    public void testVStringArray() {
        compareJson(VTypeToJson.toJson(vStringArray), vStringArrayJson);
    }

    @Test
    public void testVBooleanArray() {
        compareJson(VTypeToJson.toJson(vBooleanArray), vBooleanArrayJson);
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
        JsonObject json;
        try (JsonReader reader = Json.createReader(new StringReader("{\"type\":{\"name\":\"VIntArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}"))) {
            json = reader.readObject();
        }
        VType vType = VTypeToJson.toVType(json);
        VIntArray expected = newVIntArray(new ArrayInt(new int[] {0, 1, 2}), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        assertThat(vType, instanceOf(VIntArray.class));
        assertThat("Value mismatch", VTypeValueEquals.valueEquals(expected, vType), equalTo(true));
        assertThat("Alarm mismatch", VTypeValueEquals.alarmEquals(expected, (Alarm) vType), equalTo(true));
        assertThat("Time mismatch", VTypeValueEquals.timeEquals(expected, (Time) vType), equalTo(true));
    }

    @Test
    public void parseVShortArray() {
        JsonObject json;
        try (JsonReader reader = Json.createReader(new StringReader("{\"type\":{\"name\":\"VShortArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}"))) {
            json = reader.readObject();
        }
        VType vType = VTypeToJson.toVType(json);
        VShortArray expected = newVShortArray(new ArrayShort(new short[] {0, 1, 2}), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        assertThat(vType, instanceOf(VShortArray.class));
        assertThat("Value mismatch", VTypeValueEquals.valueEquals(expected, vType), equalTo(true));
        assertThat("Alarm mismatch", VTypeValueEquals.alarmEquals(expected, (Alarm) vType), equalTo(true));
        assertThat("Time mismatch", VTypeValueEquals.timeEquals(expected, (Time) vType), equalTo(true));
    }

    @Test
    public void parseVByteArray() {
        JsonObject json;
        try (JsonReader reader = Json.createReader(new StringReader("{\"type\":{\"name\":\"VByteArray\",\"version\":1},"
                + "\"value\":[0,1,2],"
                + "\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}"))) {
            json = reader.readObject();
        }
        VType vType = VTypeToJson.toVType(json);
        VByteArray expected = (VByteArray) newVNumberArray(new ArrayByte(new byte[] {0, 1, 2}), alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        assertThat(vType, instanceOf(VByteArray.class));
        assertThat("Value mismatch", VTypeValueEquals.valueEquals(expected, vType), equalTo(true));
        assertThat("Alarm mismatch", VTypeValueEquals.alarmEquals(expected, (Alarm) vType), equalTo(true));
        assertThat("Time mismatch", VTypeValueEquals.timeEquals(expected, (Time) vType), equalTo(true));
    }

    @Test
    public void parseVString() {
        JsonObject json;
        try (JsonReader reader = Json.createReader(new StringReader("{\"type\":{\"name\":\"VString\",\"version\":1},\"value\":\"A JSON string\",\"alarm\":{\"severity\":\"MINOR\",\"status\":\"HIGH\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}"))) {
            json = reader.readObject();
        }
        VType vType = VTypeToJson.toVType(json);
        VString expected = newVString("A JSON string", newAlarm(AlarmSeverity.MINOR, "HIGH"), newTime(Timestamp.of(0, 0)));
        assertThat(vType, instanceOf(VString.class));
        assertThat("Value mismatch", VTypeValueEquals.valueEquals(expected, vType), equalTo(true));
        assertThat("Alarm mismatch", VTypeValueEquals.alarmEquals(expected, (Alarm) vType), equalTo(true));
        assertThat("Time mismatch", VTypeValueEquals.timeEquals(expected, (Time) vType), equalTo(true));
    }

    @Test
    public void parseVEnum() {
        compareVType(vEnum, VTypeToJson.toVType(parseJson(vEnumJson)));
    }

    @Test
    public void parseVEnumArray() {
        compareVType(vEnumArray, VTypeToJson.toVType(parseJson(vEnumArrayJson)));
    }

    @Test
    public void parseVStringArray() {
        compareVType(vStringArray, VTypeToJson.toVType(parseJson(vStringArrayJson)));
    }
    
}
