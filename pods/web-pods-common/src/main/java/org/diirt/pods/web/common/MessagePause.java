/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.Writer;
import javax.json.JsonObject;

/**
 * Message to pause notifications from a channel.
 *
 * @author carcassi
 */
public class MessagePause extends Message {

    /**
     * Creates a new message based on the JSON representation.
     *
     * @param obj JSON object
     * @throws MessageDecodeException if json format is incorrect
     */
    public MessagePause(JsonObject obj) throws MessageDecodeException {
        super(obj);
    }

    /**
     * Creates a new message based on the given parameters.
     *
     * @param id the channel id
     */
    public MessagePause(int id) {
        super(MessageType.PAUSE, id);
    }

    @Override
    public void toJson(Writer writer) {
        basicToJson(writer);
    }

}
