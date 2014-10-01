/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author carcassi
 */
public class MessageEncoder implements Encoder.TextStream<Message> {

    @Override
    public void encode(Message object, Writer writer) throws EncodeException, IOException {
        object.toJson(writer);
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
    
}
