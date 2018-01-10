/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.IOException;
import java.io.Reader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decodes messages from a JSON text stream to a Message object.
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
            case "resume":
                return new MessageResume(jObject);
            case "unsubscribe":
                return new MessageUnsubscribe(jObject);
            case "event":
                String eventType = jObject.getString("type");
                switch(eventType) {
                    case "connection":
                        return new MessageConnectionEvent(jObject);
                    case "value":
                        return new MessageValueEvent(jObject);
                    case "writeCompleted":
                        return new MessageWriteCompletedEvent(jObject);
                    case "error":
                        return new MessageErrorEvent(jObject);
                    default:
                        throw new DecodeException("", "Event " + eventType + " is not supported");
                }
            default:
                throw MessageDecodeException.unsupportedMessage(jObject);
        }
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
