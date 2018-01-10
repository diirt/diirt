/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encodes messages from a Message object to a JSON text stream.
 *
 * @author carcassi
 */
public class MessageEncoder implements Encoder.TextStream<Message> {
    private final static Logger log = Logger.getLogger(MessageEncoder.class.getName());

    @Override
    public void encode(Message object, Writer writer) throws EncodeException, IOException {
        try {
           object.toJson(writer);
        } catch (RuntimeException ex) {
            log.log(Level.SEVERE, "Error while preparing event for subscription " + object.getId(), ex);
            throw ex;
        }
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
