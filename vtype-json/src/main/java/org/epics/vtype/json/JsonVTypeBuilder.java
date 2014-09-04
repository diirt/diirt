/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */


package org.epics.vtype.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;

/**
 *
 * @author carcassi
 */
public class JsonVTypeBuilder implements JsonObjectBuilder {
    
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
        if (Double.isFinite(d)) {
            builder.add(string, d);
        } else {
            builder.addNull(string);
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
                .add("unixSec", time.getTimestamp().getSec())
                .add("nanoSec", time.getTimestamp().getNanoSec())
                .addNullableObject("userTag", time.getTimeUserTag()));
    }
    
    public JsonVTypeBuilder addDisplay(Display display) {
        return add("time", new JsonVTypeBuilder()
                .add("lowAlarm", display.getLowerAlarmLimit())
                .add("highAlarm", display.getUpperAlarmLimit())
                .add("lowDisplay", display.getLowerDisplayLimit())
                .add("highDisplay", display.getUpperDisplayLimit())
                .add("lowWarning", display.getLowerWarningLimit())
                .add("highWarning", display.getUpperDisplayLimit())
                .add("units", display.getUnits()));
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
        } else {
            throw new UnsupportedOperationException("Class " + o.getClass() + " not supported");
        }
    
        return this;
    }
    
}
