/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.web.pods.common;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.VDouble;
import org.epics.vtype.VTypeValueEquals;
import static org.epics.vtype.ValueFactory.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.web.pods.common.MessageTestConstants.*;

/**
 *
 * @author carcassi
 */
public class MessageDecoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void decodeMissingAttribute() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\""
            + "}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeMismatchIntAttribute1() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\","
            + "    \"id\" : \"subscribe\""
            + "}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeMismatchIntAttribute2() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\","
            + "    \"id\" : 3.14"
            + "}"));
    }

    @Test
    public void decodeSubscribe1() throws Exception {
        testDecoding(subscribe1Message, subscribe1Json);
    }

    @Test
    public void decodeSubscribe2() throws Exception {
        testDecoding(subscribe2Message, subscribe2Json);
    }
    
    public static void testDecoding(Message message, String json) throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = (Message) decoder.decode(new StringReader(json));
        compareMessage(message, result);
    }
    
    public static void compareMessage(Message expected, Message result) {
        if (expected instanceof MessageSubscribe && result instanceof MessageSubscribe) {
            compareMessage((MessageSubscribe) expected, (MessageSubscribe) result);
        } else {
            throw new UnsupportedOperationException("Can't compare " + expected.getClass() + " with " + result.getClass());
        }
    }
    
    public static void compareMessage(MessageSubscribe expected, MessageSubscribe result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        assertThat(result.getPv(), equalTo(expected.getPv()));
        assertThat(result.getMaxRate(), equalTo(expected.getMaxRate()));
        assertThat(result.getType(), equalTo(expected.getType()));
        assertThat(result.isReadOnly(), equalTo(expected.isReadOnly()));
    }

    @Test
    public void decodeUnsubscribe1() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageUnsubscribe result = (MessageUnsubscribe) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"unsubscribe\","
            + "    \"id\" : 1"
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.UNSUBSCRIBE));
        assertThat(result.getId(), equalTo(1));
    }

    @Test
    public void decodeWrite1() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageWrite result = (MessageWrite) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"write\","
            + "    \"id\" : 1,"
            + "    \"value\" : 3.14"
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.WRITE));
        assertThat(result.getId(), equalTo(1));
        assertThat((Double) result.getValue(), equalTo(3.14));
    }

    @Test
    public void decodeWrite2() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageWrite result = (MessageWrite) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"write\","
            + "    \"id\" : 1,"
            + "    \"value\" : \"Green\""
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.WRITE));
        assertThat(result.getId(), equalTo(1));
        assertThat((String) result.getValue(), equalTo("Green"));
    }

    @Test
    public void decodeWrite3() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageWrite result = (MessageWrite) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"write\","
            + "    \"id\" : 1,"
            + "    \"value\" : {\"type\":{\"name\":\"VDouble\",\"version\":1},"
            + "        \"value\":3.14,"
            + "        \"alarm\":{\"severity\":\"MINOR\",\"status\":\"LOW\"},"
            + "        \"time\":{\"unixSec\":0,\"nanoSec\":0,\"userTag\":null},"
            + "        \"display\":{\"lowAlarm\":null,\"highAlarm\":null,\"lowDisplay\":null,\"highDisplay\":null,\"lowWarning\":null,\"highWarning\":null,\"units\":\"\"}}"
            + "}"));
        VDouble expected = newVDouble(3.14, newAlarm(AlarmSeverity.MINOR, "LOW"), newTime(Timestamp.of(0, 0)), displayNone());
        assertThat(result.getMessage(), equalTo(Message.MessageType.WRITE));
        assertThat(result.getId(), equalTo(1));
        assertThat(VTypeValueEquals.valueEquals(expected, result.getValue()), equalTo(true));
    }

    @Test
    public void decodeWrite4() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageWrite result = (MessageWrite) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"write\","
            + "    \"id\" : 1,"
            + "    \"value\" : [1,2,3,4,5]"
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.WRITE));
        assertThat(result.getId(), equalTo(1));
        assertThat((ListDouble) result.getValue(), equalTo((ListDouble) new ArrayDouble(1,2,3,4,5)));

    }

    @Test
    public void decodeWrite5() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageWrite result = (MessageWrite) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"write\","
            + "    \"id\" : 1,"
            + "    \"value\" : [\"A\",\"B\",\"C\"]"
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.WRITE));
        assertThat(result.getId(), equalTo(1));
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) result.getValue();
        assertThat(list, equalTo(Arrays.asList("A", "B", "C")));

    }

    @Test
    public void decodePause1() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessagePause result = (MessagePause) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"pause\","
            + "    \"id\" : 1"
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.PAUSE));
        assertThat(result.getId(), equalTo(1));
    }

    @Test
    public void decodeResume1() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        MessageResume result = (MessageResume) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"resume\","
            + "    \"id\" : 1"
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.RESUME));
        assertThat(result.getId(), equalTo(1));
    }
    
}
