/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 * Message event for the response to a write.
 *
 * @author carcassi
 */
public class MessageWriteCompletedEvent extends Message {

    private final String error;
    private final boolean successful;

    /**
     * Creates a new message based on the JSON representation.
     *
     * @param obj JSON object
     * @throws MessageDecodeException if json format is incorrect
     */
    public MessageWriteCompletedEvent(JsonObject obj) throws MessageDecodeException {
        super(obj);
        this.error = stringOptional(obj, "error", null);
        this.successful = booleanMandatory(obj, "successful");
    }

    /**
     * Creates a new message based on the given parameters.
     * Creates a successful write response.
     *
     * @param id the channel id
     */
    public MessageWriteCompletedEvent(int id) {
        super(MessageType.EVENT, id);
        this.successful = true;
        this.error = null;
    }

    /**
     * Creates a new message based on the given parameters.
     * Creates an unsuccessful write response.
     *
     * @param id the channel id
     * @param error the error message
     */
    public MessageWriteCompletedEvent(int id, String error) {
        super(MessageType.EVENT, id);
        this.successful = false;
        this.error = error;
    }

    /**
     * The error message if the write was unsuccessful or null.
     *
     * @return an error message or null
     */
    public String getError() {
        return error;
    }

    /**
     * Whether the write was successful.
     *
     * @return true if successful
     */
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject()
                .write("message", getMessage().toString().toLowerCase())
                .write("id", getId())
                .write("type", "writeCompleted")
                .write("successful", isSuccessful());

        if (getError() != null) {
            gen.write("error", getError());
        }

        gen.writeEnd().close();
    }

}
