/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.vtype.seds;

import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VDouble;
import org.epics.vtype.VInt;
import org.epics.vtype.ValueFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.vtype.ValueFactory.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class Seds2VTypeTest {

    @Test
    public void testVDouble() {
        VDouble vDouble = newVDouble(3.14, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        JsonObject json = Seds2VType.toJson(vDouble);
        compareJson(json, "{\"value\":3.14,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}");
    }

    public void compareJson(JsonObject json, String text) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(json);
        assertThat(writer.toString(), equalTo(text));
    }

    @Test
    public void testVInt() {
        VInt vInt = newVInt(314, alarmNone(), newTime(Timestamp.of(0, 0)), displayNone());
        JsonObject json = Seds2VType.toJson(vInt);
        compareJson(json, "{\"value\":314,\"alarm\":{\"severity\":\"NONE\",\"status\":\"NONE\"},\"time\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}");
    }
    
}
