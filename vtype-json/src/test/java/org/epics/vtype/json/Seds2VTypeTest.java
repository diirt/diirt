/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.vtype.json;

import org.epics.vtype.json.Seds2VType;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VDouble;
import org.epics.vtype.VInt;
import org.epics.vtype.VString;
import org.epics.vtype.VType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.vtype.ValueFactory.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class Seds2VTypeTest {

    public void compareJson(JsonObject json, String text) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(json);
        assertThat(writer.toString(), equalTo(text));
    }

    @Test
    public void testVDouble() {
        VDouble vDouble = newVDouble(3.14, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        JsonObject json = Seds2VType.toJson(vDouble);
        compareJson(json, "{\"value\":3.14,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}");
    }

    @Test
    public void testVInt() {
        VInt vInt = newVInt(314, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        JsonObject json = Seds2VType.toJson((VType) vInt);
        compareJson(json, "{\"value\":314,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}");
    }

    @Test
    public void testVBoolean() {
        VBoolean vBoolean = newVBoolean(true, alarmNone(), newTime(Timestamp.of(0, 0)));
        JsonObject json = Seds2VType.toJson((VType) vBoolean);
        compareJson(json, "{\"value\":true,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}");
    }

    @Test
    public void testVString() {
        VString vString = newVString("Flower", alarmNone(), newTime(Timestamp.of(0, 0)));
        JsonObject json = Seds2VType.toJson((VType) vString);
        compareJson(json, "{\"value\":\"Flower\",\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null}}");
    }
    
}
