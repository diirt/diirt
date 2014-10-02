/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import java.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.web.pods.common.MessageTestConstants.*;

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
    public void encodeConnectionEvent1() throws Exception {
        testEncoding(connectionEvent1Message, connectionEvent1Json);
    }

    @Test
    public void encodeConnectionEvent2() throws Exception {
        testEncoding(connectionEvent2Message, connectionEvent2Json);
    }

    @Test
    public void encodeValueEvent1() throws Exception {
        testEncoding(valueEvent1Message, valueEvent1Json);
    }

    @Test
    public void encodeValueEvent2() throws Exception {
        testEncoding(valueEvent2Message, valueEvent2Json);
    }

    @Test
    public void encodeErrorEvent1() throws Exception {
        testEncoding(errorEvent1Message, errorEvent1Json);
    }

    @Test
    public void encodeWriteCompletedEvent1() throws Exception {
        testEncoding(writeEvent1Message, writeEvent1Json);
    }

    @Test
    public void encodeWriteCompletedEvent2() throws Exception {
        testEncoding(writeEvent2Message, writeEvent2Json);
    }

    @Test
    public void encode1Subscribe() throws Exception {
        testEncoding(subscribe1Message, subscribe1Json);
    }

    @Test
    public void encode2Subscribe() throws Exception {
        testEncoding(subscribe2Message, subscribe2Json);
    }

    @Test
    public void encode1Unsubscribe() throws Exception {
        testEncoding(unsubscribe1Message, unsubscribe1Json);
    }

    @Test
    public void encode1Write() throws Exception {
        testEncoding(write1Message, write1Json);
    }

    @Test
    public void encode2Write() throws Exception {
        testEncoding(write2Message, write2Json);
    }

    @Test
    public void encode3Write() throws Exception {
        testEncoding(write3Message, write3Json);
    }
    
}
