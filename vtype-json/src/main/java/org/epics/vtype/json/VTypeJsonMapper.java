/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */


package org.epics.vtype.json;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayFloat;
import org.epics.util.array.ArrayLong;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListFloat;
import org.epics.util.array.ListLong;
import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.ValueFactory;

/**
 *
 * @author carcassi
 */
public class VTypeJsonMapper implements JsonObject {
    
    private final JsonObject json;

    public VTypeJsonMapper(JsonObject json) {
        this.json = json;
    }
    
    public String getTypeName() {
        JsonObject type = json.getJsonObject("type");
        if (type == null) {
            return null;
        }
        return type.getString("name");
    }
    
    public Alarm getAlarm() {
        JsonObject alarm = json.getJsonObject("alarm");
        if (alarm == null) {
            return null;
        }
        return ValueFactory.newAlarm(AlarmSeverity.valueOf(alarm.getString("severity")), alarm.getString("status"));
    }
    
    public Time getTime() {
        VTypeJsonMapper time = getJsonObject("time");
        if (time == null) {
            return null;
        }
        return ValueFactory.newTime(Timestamp.of(time.getInt("unixSec"), time.getInt("nanoSec")), time.getInteger("userTag"), true);
    }
    
    public Display getDisplay() {
        VTypeJsonMapper display = getJsonObject("display");
        if (display == null) {
            return null;
        }
        return ValueFactory.newDisplay(display.getNotNullDouble("lowDisplay"),
                display.getNotNullDouble("lowAlarm"),
                display.getNotNullDouble("lowWarning"),
                display.getString("units"), null,
                display.getNotNullDouble("highWarning"),
                display.getNotNullDouble("highAlarm"),
                display.getNotNullDouble("highDisplay"),
                Double.NaN,
                Double.NaN);
    }
    
    public ListDouble getListDouble(String string) {
        JsonArray array = getJsonArray(string);
        double[] values = new double[array.size()];
        for (int i = 0; i < values.length; i++) {
            if (array.isNull(i)) {
                values[i] = Double.NaN;
            } else {
                values[i] = array.getJsonNumber(i).doubleValue();
            }
        }
        return new ArrayDouble(values);
    }
    
    public ListFloat getListFloat(String string) {
        JsonArray array = getJsonArray(string);
        float[] values = new float[array.size()];
        for (int i = 0; i < values.length; i++) {
            if (array.isNull(i)) {
                values[i] = Float.NaN;
            } else {
                values[i] = (float) array.getJsonNumber(i).doubleValue();
            }
        }
        return new ArrayFloat(values);
    }
    
    public ListLong getListLong(String string) {
        JsonArray array = getJsonArray(string);
        long[] values = new long[array.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = (long) array.getJsonNumber(i).longValue();
        }
        return new ArrayLong(values);
    }
    
    public Integer getInteger(String string) {
        if (isNull(string)) {
            return null;
        }
        return getInt(string);
    }
    
    public Double getNotNullDouble(String string) {
        if (isNull(string)) {
            return Double.NaN;
        }
        return getJsonNumber(string).doubleValue();
    }

    @Override
    public JsonArray getJsonArray(String string) {
        return json.getJsonArray(string);
    }

    @Override
    public VTypeJsonMapper getJsonObject(String string) {
        return new VTypeJsonMapper(json.getJsonObject(string));
    }

    @Override
    public JsonNumber getJsonNumber(String string) {
        return json.getJsonNumber(string);
    }

    @Override
    public JsonString getJsonString(String string) {
        return json.getJsonString(string);
    }

    @Override
    public String getString(String string) {
        return json.getString(string);
    }

    @Override
    public String getString(String string, String string1) {
        return json.getString(string, string1);
    }

    @Override
    public int getInt(String string) {
        return json.getInt(string);
    }

    @Override
    public int getInt(String string, int i) {
        return json.getInt(string, i);
    }

    @Override
    public boolean getBoolean(String string) {
        return json.getBoolean(string);
    }

    @Override
    public boolean getBoolean(String string, boolean bln) {
        return json.getBoolean(string, bln);
    }

    @Override
    public boolean isNull(String string) {
        return json.isNull(string);
    }

    @Override
    public ValueType getValueType() {
        return json.getValueType();
    }

    @Override
    public int size() {
        return json.size();
    }

    @Override
    public boolean isEmpty() {
        return json.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return json.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return json.containsValue(value);
    }

    @Override
    public JsonValue get(Object key) {
        return json.get(key);
    }

    @Override
    public JsonValue put(String key, JsonValue value) {
        return json.put(key, value);
    }

    @Override
    public JsonValue remove(Object key) {
        return json.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends JsonValue> m) {
        json.putAll(m);
    }

    @Override
    public void clear() {
        json.clear();
    }

    @Override
    public Set<String> keySet() {
        return json.keySet();
    }

    @Override
    public Collection<JsonValue> values() {
        return json.values();
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return json.entrySet();
    }

}
