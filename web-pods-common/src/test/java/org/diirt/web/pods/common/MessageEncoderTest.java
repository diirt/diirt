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

    @Test
    public void encodeConnectionEvent1() throws Exception {
        MessageConnectionEvent event = new MessageConnectionEvent(12, true, false);
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":12,\"type\":\"connection\",\"connected\":true,\"writeConnected\":false}"));
    }

    @Test
    public void encodeConnectionEvent2() throws Exception {
        MessageConnectionEvent event = new MessageConnectionEvent(15, false, true);
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":15,\"type\":\"connection\",\"connected\":false,\"writeConnected\":true}"));
    }

    @Test
    public void encodeValueEvent1() throws Exception {
        MessageValueEvent event = new MessageValueEvent(12, 3.14);
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":12,\"type\":\"value\",\"value\":3.14}"));
    }

    @Test
    public void encodeValueEvent2() throws Exception {
        MessageValueEvent event = new MessageValueEvent(12, "Hello");
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":12,\"type\":\"value\",\"value\":\"Hello\"}"));
    }

    @Test
    public void encodeErrorEvent1() throws Exception {
        MessageErrorEvent event = new MessageErrorEvent(12, "Mayday");
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":12,\"type\":\"error\",\"error\":\"Mayday\"}"));
    }

    @Test
    public void encodeWriteCompletedEvent1() throws Exception {
        MessageWriteCompletedEvent event = new MessageWriteCompletedEvent(12);
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":12,\"type\":\"writeCompleted\",\"successful\":true}"));
    }

    @Test
    public void encodeWriteCompletedEvent2() throws Exception {
        MessageWriteCompletedEvent event = new MessageWriteCompletedEvent(12, "Value too big");
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(event, writer);
        assertThat(writer.toString(), equalTo("{\"message\":\"event\",\"id\":12,\"type\":\"writeCompleted\",\"successful\":false,\"error\":\"Value too big\"}"));
    }

    @Test
    public void encodeSubscribe() throws Exception {
        MessageEncoder encoder = new MessageEncoder();
        StringWriter writer = new StringWriter();
        encoder.encode(subscribeMessage, writer);
        assertThat(writer.toString(), equalTo(subscribeJson));
    }
    
}
