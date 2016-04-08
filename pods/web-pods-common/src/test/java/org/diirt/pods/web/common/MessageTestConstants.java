/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.time.Instant;
import java.util.Arrays;

import org.diirt.util.array.ArrayDouble;
import org.diirt.vtype.AlarmSeverity;

import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class MessageTestConstants {
    public static String subscribe1Json = "{"
            + "\"message\":\"subscribe\","
            + "\"id\":1,"
            + "\"channel\":\"sim://noise\""
            + "}";
    public static MessageSubscribe subscribe1Message = new MessageSubscribe(1, "sim://noise", null, -1, true);

    public static String subscribe2Json = "{"
            + "\"message\":\"subscribe\","
            + "\"id\":1,"
            + "\"channel\":\"sim://noise\","
            + "\"type\":\"VDouble\","
            + "\"maxRate\":10,"
            + "\"readOnly\":false"
            + "}";
    public static MessageSubscribe subscribe2Message = new MessageSubscribe(1, "sim://noise", "VDouble", 10, false);

    public static String unsubscribe1Json = "{"
            + "\"message\":\"unsubscribe\","
            + "\"id\":1"
            + "}";
    public static MessageUnsubscribe unsubscribe1Message = new MessageUnsubscribe(1);

    public static String write1Json = "{"
            + "\"message\":\"write\","
            + "\"id\":1,"
            + "\"value\":3.14"
            + "}";
    public static MessageWrite write1Message = new MessageWrite(1, 3.14);

    public static String write2Json = "{"
            + "\"message\":\"write\","
            + "\"id\":1,"
            + "\"value\":\"Green\""
            + "}";
    public static MessageWrite write2Message = new MessageWrite(1, "Green");

    public static String write3Json = "{"
            + "\"message\":\"write\","
            + "\"id\":1,"
            + "\"value\":{\"type\":{\"name\":\"VDouble\",\"version\":1},"
                + "\"value\":3.14,"
                + "\"alarm\":{\"severity\":\"MINOR\",\"status\":\"LOW\"},"
                + "\"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
                + "\"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}"
            + "}";
    public static MessageWrite write3Message = new MessageWrite(1, newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Instant.ofEpochSecond(0, 0)), displayNone()));

    public static String write4Json = "{"
            + "\"message\":\"write\","
            + "\"id\":1,"
            + "\"value\":[1.0,2.0,3.0,4.0,5.0]"
            + "}";
    public static MessageWrite write4Message = new MessageWrite(1, new ArrayDouble(1,2,3,4,5));

    public static String write5Json = "{"
            + "\"message\":\"write\","
            + "\"id\":1,"
            + "\"value\":[\"A\",\"B\",\"C\"]"
            + "}";
    public static MessageWrite write5Message = new MessageWrite(1, Arrays.asList("A", "B", "C"));

    public static String pause1Json = "{"
            + "\"message\":\"pause\","
            + "\"id\":3"
            + "}";
    public static MessagePause pause1Message = new MessagePause(3);

    public static String resume1Json = "{"
            + "\"message\":\"resume\","
            + "\"id\":4"
            + "}";
    public static MessageResume resume1Message = new MessageResume(4);

    public static String connectionEvent1Json = "{"
            + "\"message\":\"event\","
            + "\"id\":12,"
            + "\"type\":\"connection\","
            + "\"connected\":true,"
            + "\"writeConnected\":false"
            + "}";
    public static MessageConnectionEvent connectionEvent1Message = new MessageConnectionEvent(12, true, false);

    public static String connectionEvent2Json = "{"
            + "\"message\":\"event\","
            + "\"id\":15,"
            + "\"type\":\"connection\","
            + "\"connected\":false,"
            + "\"writeConnected\":true"
            + "}";
    public static MessageConnectionEvent connectionEvent2Message = new MessageConnectionEvent(15, false, true);

    public static String valueEvent1Json = "{"
            + "\"message\":\"event\","
            + "\"id\":12,"
            + "\"type\":\"value\","
            + "\"value\":3.14"
            + "}";
    public static MessageValueEvent valueEvent1Message = new MessageValueEvent(12, 3.14);

    public static String valueEvent2Json = "{"
            + "\"message\":\"event\","
            + "\"id\":12,"
            + "\"type\":\"value\","
            + "\"value\":\"Hello\""
            + "}";
    public static MessageValueEvent valueEvent2Message = new MessageValueEvent(12, "Hello");

    public static String errorEvent1Json = "{"
            + "\"message\":\"event\","
            + "\"id\":12,"
            + "\"type\":\"error\","
            + "\"error\":\"Mayday\""
            + "}";
    public static MessageErrorEvent errorEvent1Message = new MessageErrorEvent(12, "Mayday");

    public static String writeEvent1Json = "{"
            + "\"message\":\"event\","
            + "\"id\":12,"
            + "\"type\":\"writeCompleted\","
            + "\"successful\":true"
            + "}";
    public static MessageWriteCompletedEvent writeEvent1Message = new MessageWriteCompletedEvent(12);

    public static String writeEvent2Json = "{"
            + "\"message\":\"event\","
            + "\"id\":12,"
            + "\"type\":\"writeCompleted\","
            + "\"successful\":false,"
            + "\"error\":\"Value too big\""
            + "}";
    public static MessageWriteCompletedEvent writeEvent2Message = new MessageWriteCompletedEvent(12, "Value too big");

}
