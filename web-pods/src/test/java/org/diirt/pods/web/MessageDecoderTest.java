/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diirt.pods.web;

import java.io.Reader;
import java.io.StringReader;
import javax.websocket.EndpointConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
        MessageDecoder decoder = new MessageDecoder();
        MessageSubscribe result = (MessageSubscribe) decoder.decode(new StringReader(
            "{ "
            + "    \"message\" : \"subscribe\","
            + "    \"id\" : 1,"
            + "    \"pv\" : \"sim://noise\""
            + "}"));
                
        assertThat(result.getMessage(), equalTo(Message.MessageType.SUBSCRIBE));
        assertThat(result.getId(), equalTo(1));
        assertThat(result.getPv(), equalTo("sim://noise"));
        assertThat(result.getMaxRate(), equalTo(-1));
        assertThat(result.getType(), equalTo(null));
        assertThat(result.isReadOnly(), equalTo(true));
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
    
}
