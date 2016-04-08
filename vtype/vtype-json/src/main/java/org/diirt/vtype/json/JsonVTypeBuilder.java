/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.diirt.util.array.ListBoolean;
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.Display;
import org.diirt.vtype.Time;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueUtil;

import static org.diirt.vtype.json.JsonArrays.*;

/**
 *
 * @author carcassi
 */
class JsonVTypeBuilder implements JsonObjectBuilder {

    private final JsonObjectBuilder builder = Json.createObjectBuilder();

    @Override
    public JsonVTypeBuilder add(String string, JsonValue jv) {
        builder.add(string, jv);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, String string1) {
        builder.add(string, string1);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, BigInteger bi) {
        builder.add(string, bi);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, BigDecimal bd) {
        builder.add(string, bd);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, int i) {
        builder.add(string, i);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, long l) {
        builder.add(string, l);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            builder.addNull(string);
        } else {
            builder.add(string, d);
        }
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, boolean bln) {
        builder.add(string, bln);
        return this;
    }

    @Override
    public JsonVTypeBuilder addNull(String string) {
        builder.addNull(string);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, JsonObjectBuilder job) {
        builder.add(string, job);
        return this;
    }

    @Override
    public JsonVTypeBuilder add(String string, JsonArrayBuilder jab) {
        builder.add(string, jab);
        return this;
    }

    @Override
    public JsonObject build() {
        return builder.build();
    }

    public JsonVTypeBuilder addAlarm(Alarm alarm) {
        return add("alarm", new JsonVTypeBuilder()
                .add("severity", alarm.getAlarmSeverity().toString())
                .add("status", alarm.getAlarmName()));
    }

    public JsonVTypeBuilder addTime(Time time) {
        return add("time", new JsonVTypeBuilder()
                .add("unixSec", time.getTimestamp().getEpochSecond())
                .add("nanoSec", time.getTimestamp().getNano())
                .addNullableObject("userTag", time.getTimeUserTag()));
    }

    public JsonVTypeBuilder addDisplay(Display display) {
        return add("display", new JsonVTypeBuilder()
                .add("lowAlarm", display.getLowerAlarmLimit())
                .add("highAlarm", display.getUpperAlarmLimit())
                .add("lowDisplay", display.getLowerDisplayLimit())
                .add("highDisplay", display.getUpperDisplayLimit())
                .add("lowWarning", display.getLowerWarningLimit())
                .add("highWarning", display.getUpperDisplayLimit())
                .add("units", display.getUnits()));
    }

    public JsonVTypeBuilder addEnum(org.diirt.vtype.Enum en) {
        return add("enum", new JsonVTypeBuilder()
                .addListString("labels", en.getLabels()));
    }

    public JsonVTypeBuilder addListString(String string, List<String> ls) {
        add(string, fromListString(ls));
        return this;
    }

    public JsonVTypeBuilder addListColumnType(String string, List<Class<?>> ls) {
        JsonArrayBuilder b = Json.createArrayBuilder();
        for (Class<?> element : ls) {
            if (element.equals(String.class)) {
                b.add("String");
            } else if (element.equals(double.class)) {
                b.add("double");
            } else if (element.equals(float.class)) {
                b.add("float");
            } else if (element.equals(long.class)) {
                b.add("long");
            } else if (element.equals(int.class)) {
                b.add("int");
            } else if (element.equals(short.class)) {
                b.add("short");
            } else if (element.equals(byte.class)) {
                b.add("byte");
            } else if (element.equals(Instant.class)) {
                b.add("Timestamp");
            } else {
                throw new IllegalArgumentException("Column type " + element + " not supported");
            }
        }
        add(string, b);
        return this;
    }

    public JsonVTypeBuilder  addListColumnValues(String string, VTable vTable) {
        JsonArrayBuilder b = Json.createArrayBuilder();
        for (int column = 0; column < vTable.getColumnCount(); column++) {
            Class<?> type = vTable.getColumnType(column);
            if (type.equals(String.class)) {
                @SuppressWarnings("unchecked")
                List<String> listString = (List<String>) vTable.getColumnData(column);
                b.add(fromListString(listString));
            } else if (type.equals(double.class) || type.equals(float.class) || type.equals(long.class) ||
                    type.equals(int.class) || type.equals(short.class) || type.equals(byte.class)) {
                b.add(fromListNumber((ListNumber) vTable.getColumnData(column)));
            } else if (type.equals(Instant.class)) {
                @SuppressWarnings("unchecked")
                List<Instant> listTimestamp = (List<Instant>) vTable.getColumnData(column);
                b.add(fromListTimestamp(listTimestamp));
            } else {
                throw new IllegalArgumentException("Column type " + type.getSimpleName() + " not supported");
            }
        }
        add(string, b);
        return this;
    }

    public JsonVTypeBuilder addListNumber(String string, ListNumber ln) {
        add(string, fromListNumber(ln));
        return this;
    }

    public JsonVTypeBuilder addListBoolean(String string, ListBoolean lb) {
        JsonArrayBuilder b = Json.createArrayBuilder();
        for (int i = 0; i < lb.size(); i++) {
            b.add(lb.getBoolean(i));
        }
        add(string, b);
        return this;
    }

    public JsonVTypeBuilder addNullableObject(String string, Object o) {
        if (o == null) {
            addNull(string);
        } else {
            addObject(string, o);
        }
        return this;
    }

    public JsonVTypeBuilder addObject(String string, Object o) {
        if (o == null) {
            return this;
        }

        if (o instanceof Double || o instanceof Float) {
            add(string, ((Number) o).doubleValue());
        } else if (o instanceof Byte || o instanceof Short || o instanceof Integer) {
            add(string, ((Number) o).intValue());
        } else if (o instanceof Long) {
            add(string, ((Number) o).longValue());
        } else if (o instanceof ListNumber) {
            addListNumber(string, (ListNumber) o);
        } else if (o instanceof ListBoolean) {
            addListBoolean(string, (ListBoolean) o);
        } else {
            throw new UnsupportedOperationException("Class " + o.getClass() + " not supported");
        }

        return this;
    }

    public JsonVTypeBuilder addType(VType vType) {
        Class<?> clazz = ValueUtil.typeOf(vType);
        return add("type", new JsonVTypeBuilder()
                .add("name", clazz.getSimpleName())
                .add("version", 1));
    }
}
