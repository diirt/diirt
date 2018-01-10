/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.web.common;

import java.io.StringReader;
import org.diirt.vtype.VTypeValueEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.pods.web.common.MessageTestConstants.*;
import org.diirt.vtype.VType;

/**
 *
 * @author carcassi
 */
public class MessageDecoderTest {

    @Test(expected = MessageDecodeException.class)
    public void decodeMissingAttribute() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\""
            + "}"));
    }

    @Test(expected = MessageDecodeException.class)
    public void decodeMismatchIntAttribute1() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\","
            + "    \"id\" : \"subscribe\""
            + "}"));
    }

    @Test(expected = MessageDecodeException.class)
    public void decodeMismatchIntAttribute2() throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\","
            + "    \"id\" : 3.14"
            + "}"));
    }

    public static void testDecoding(Message message, String json) throws Exception {
        MessageDecoder decoder = new MessageDecoder();
        Message result = (Message) decoder.decode(new StringReader(json));
        compareMessage(message, result);
    }

    public static void compareMessage(Message expected, Message result) {
        if (expected instanceof MessageSubscribe && result instanceof MessageSubscribe) {
            compareMessage((MessageSubscribe) expected, (MessageSubscribe) result);
        } else if (expected instanceof MessageUnsubscribe && result instanceof MessageUnsubscribe) {
            compareBaseMessage(expected, result);
        } else if (expected instanceof MessagePause && result instanceof MessagePause) {
            compareBaseMessage(expected, result);
        } else if (expected instanceof MessageResume && result instanceof MessageResume) {
            compareBaseMessage(expected, result);
        } else if (expected instanceof MessageWrite && result instanceof MessageWrite) {
            compareMessage((MessageWrite) expected, (MessageWrite) result);
        } else if (expected instanceof MessageConnectionEvent && result instanceof MessageConnectionEvent) {
            compareMessage((MessageConnectionEvent) expected, (MessageConnectionEvent) result);
        } else if (expected instanceof MessageValueEvent && result instanceof MessageValueEvent) {
            compareMessage((MessageValueEvent) expected, (MessageValueEvent) result);
        } else if (expected instanceof MessageWriteCompletedEvent && result instanceof MessageWriteCompletedEvent) {
            compareMessage((MessageWriteCompletedEvent) expected, (MessageWriteCompletedEvent) result);
        } else if (expected instanceof MessageErrorEvent && result instanceof MessageErrorEvent) {
            compareMessage((MessageErrorEvent) expected, (MessageErrorEvent) result);
        } else {
            throw new UnsupportedOperationException("Can't compare " + expected.getClass() + " with " + result.getClass());
        }
    }

    public static void compareMessage(MessageSubscribe expected, MessageSubscribe result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        assertThat(result.getChannel(), equalTo(expected.getChannel()));
        assertThat(result.getMaxRate(), equalTo(expected.getMaxRate()));
        assertThat(result.getType(), equalTo(expected.getType()));
        assertThat(result.isReadOnly(), equalTo(expected.isReadOnly()));
    }

    public static void compareBaseMessage(Message expected, Message result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
    }

    public static void compareMessage(MessageWrite expected, MessageWrite result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        if (expected.getValue() instanceof VType) {
            assertThat(VTypeValueEquals.valueEquals(expected.getValue(), result.getValue()), equalTo(true));
        } else {
            assertThat(result.getValue(), equalTo(expected.getValue()));
        }
    }

    public static void compareMessage(MessageConnectionEvent expected, MessageConnectionEvent result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        assertThat(result.isConnected(), equalTo(expected.isConnected()));
        assertThat(result.isWriteConnected(), equalTo(expected.isWriteConnected()));
    }

    public static void compareMessage(MessageValueEvent expected, MessageValueEvent result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        if (expected.getValue() instanceof VType) {
            assertThat(VTypeValueEquals.valueEquals(expected.getValue(), result.getValue()), equalTo(true));
        } else {
            assertThat(result.getValue(), equalTo(expected.getValue()));
        }
    }

    public static void compareMessage(MessageWriteCompletedEvent expected, MessageWriteCompletedEvent result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        assertThat(result.isSuccessful(), equalTo(expected.isSuccessful()));
        assertThat(result.getError(), equalTo(expected.getError()));
    }

    public static void compareMessage(MessageErrorEvent expected, MessageErrorEvent result) {
        assertThat(result.getMessage(), equalTo(expected.getMessage()));
        assertThat(result.getId(), equalTo(expected.getId()));
        assertThat(result.getError(), equalTo(expected.getError()));
    }

    @Test
    public void subscribe1Decode() throws Exception {
        testDecoding(subscribe1Message, subscribe1Json);
    }

    @Test
    public void subscribe2Decode() throws Exception {
        testDecoding(subscribe2Message, subscribe2Json);
    }

    @Test
    public void unsubscribe1Decode() throws Exception {
        testDecoding(unsubscribe1Message, unsubscribe1Json);
    }

    @Test
    public void write1Decode() throws Exception {
        testDecoding(write1Message, write1Json);
    }

    @Test
    public void write2Decode() throws Exception {
        testDecoding(write2Message, write2Json);
    }

    @Test
    public void write3Decode() throws Exception {
        testDecoding(write3Message, write3Json);
    }

    @Test
    public void write4Decode() throws Exception {
        testDecoding(write4Message, write4Json);

    }

    @Test
    public void write5Decode() throws Exception {
        testDecoding(write5Message, write5Json);
    }

    @Test
    public void pause1Decode() throws Exception {
        testDecoding(pause1Message, pause1Json);
    }

    @Test
    public void resume1Decode() throws Exception {
        testDecoding(resume1Message, resume1Json);
    }

    @Test
    public void connectionEvent1Decode() throws Exception {
        testDecoding(connectionEvent1Message, connectionEvent1Json);
    }

    @Test
    public void connectionEvent2Decode() throws Exception {
        testDecoding(connectionEvent2Message, connectionEvent2Json);
    }

    @Test
    public void valueEvent1Decode() throws Exception {
        testDecoding(valueEvent1Message, valueEvent1Json);
    }

    @Test
    public void valueEvent2Decode() throws Exception {
        testDecoding(valueEvent2Message, valueEvent2Json);
    }

    @Test
    public void errorEvent1Decode() throws Exception {
        testDecoding(errorEvent1Message, errorEvent1Json);
    }

    @Test
    public void writeEvent1Decode() throws Exception {
        testDecoding(writeEvent1Message, writeEvent1Json);
    }

    @Test
    public void writeEvent2Decode() throws Exception {
        testDecoding(writeEvent2Message, writeEvent2Json);
    }

}
