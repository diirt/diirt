/*
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.web;

import java.io.IOException;
import java.io.Reader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author carcassi
 */
public class MessageDecoder implements Decoder.TextStream<Message> {

    @Override
    public Message decode(Reader reader) throws DecodeException, IOException {
        JsonReader jReader = Json.createReader(reader);
        JsonObject jObject = jReader.readObject();
        String messageType = jObject.getString("message", null);
        switch (messageType) {
            case "subscribe":
                return new MessageSubscribe(jObject);
            case "write":
                return new MessageWrite(jObject);
            case "pause":
                return new MessagePause(jObject);
            case "unsubscribe":
                return new MessageUnsubscribe(jObject);
            default:
                throw new UnsupportedOperationException("Message " + messageType + " is not supported");
        }
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
    
}
