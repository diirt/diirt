/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.pods.web.common.MessageTestConstants.*;

/**
 *
 * @author carcassi
 */
public class MessageEncoderTest {

    public static void testEncoding(Message message, String json) throws Exception {
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(message, writer);
        assertThat(writer.toString(), equalTo(json));
    }

    @Test
    public void subscribe1Encode() throws Exception {
        testEncoding(subscribe1Message, subscribe1Json);
    }

    @Test
    public void subscribe2Encode() throws Exception {
        testEncoding(subscribe2Message, subscribe2Json);
    }

    @Test
    public void unsubscribe1Encode() throws Exception {
        testEncoding(unsubscribe1Message, unsubscribe1Json);
    }

    @Test
    public void write1Encode() throws Exception {
        testEncoding(write1Message, write1Json);
    }

    @Test
    public void write2Encode() throws Exception {
        testEncoding(write2Message, write2Json);
    }

    @Test
    public void write3Encode() throws Exception {
        testEncoding(write3Message, write3Json);
    }

    @Test
    public void write4Encode() throws Exception {
        testEncoding(write4Message, write4Json);
    }

    @Test
    public void write5Encode() throws Exception {
        testEncoding(write5Message, write5Json);
    }

    @Test
    public void pause1Encode() throws Exception {
        testEncoding(pause1Message, pause1Json);
    }

    @Test
    public void resume1Encode() throws Exception {
        testEncoding(resume1Message, resume1Json);
    }

    @Test
    public void connectionEvent1Encode() throws Exception {
        testEncoding(connectionEvent1Message, connectionEvent1Json);
    }

    @Test
    public void connectionEvent2Encode() throws Exception {
        testEncoding(connectionEvent2Message, connectionEvent2Json);
    }

    @Test
    public void valueEvent1Encode() throws Exception {
        testEncoding(valueEvent1Message, valueEvent1Json);
    }

    @Test
    public void valueEvent2Encode() throws Exception {
        testEncoding(valueEvent2Message, valueEvent2Json);
    }

    @Test
    public void errorEvent1Encode() throws Exception {
        testEncoding(errorEvent1Message, errorEvent1Json);
    }

    @Test
    public void writeEvent1Encode() throws Exception {
        testEncoding(writeEvent1Message, writeEvent1Json);
    }

    @Test
    public void writeEvent2Encode() throws Exception {
        testEncoding(writeEvent2Message, writeEvent2Json);
    }

}
