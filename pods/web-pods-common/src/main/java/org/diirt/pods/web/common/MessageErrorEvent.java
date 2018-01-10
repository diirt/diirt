/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Message event for errors.
 *
 * @author carcassi
 */
public class MessageErrorEvent extends Message {

    private final String error;

    /**
     * Creates a new message based on the JSON representation.
     *
     * @param obj JSON object
     * @throws MessageDecodeException if json format is incorrect
     */
    public MessageErrorEvent(JsonObject obj) throws MessageDecodeException {
        super(obj);
        this.error = stringMandatory(obj, "error");
    }

    /**
     * Creates a new message based on the given parameters.
     *
     * @param id the channel id
     * @param error the error message
     */
    public MessageErrorEvent(int id, String error) {
        super(MessageType.EVENT, id);
        this.error = error;
    }

    /**
     * The error message.
     *
     * @return error message
     */
    public String getError() {
        return error;
    }

    @Override
    public void toJson(Writer writer) {
        Json.createGenerator(writer).writeStartObject()
                .write("message", getMessage().toString().toLowerCase())
                .write("id", getId())
                .write("type", "error")
                .write("error", getError())
                .writeEnd()
                .close();
    }

}
