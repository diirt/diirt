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
 * Message event for change of value.
 *
 * @author carcassi
 */
public class MessageValueEvent extends Message {

    private final Object value;

    /**
     * Creates a new message based on the JSON representation.
     *
     * @param obj JSON object
     * @throws MessageDecodeException if json format is incorrect
     */
    public MessageValueEvent(JsonObject obj) throws MessageDecodeException {
        super(obj);
        value = readValueFromJson(obj.get("value"));
    }

    /**
     * Creates a new message based on the given parameters.
     *
     * @param id the channel id
     * @param value the new value
     */
    public MessageValueEvent(int id, Object value) {
        super(MessageType.EVENT, id);
        this.value = value;
    }

    /**
     * The new value.
     *
     * @return the new value
     */
    public Object getValue() {
        return value;
    }

    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject()
                .write("message", getMessage().toString().toLowerCase())
                .write("id", getId())
                .write("type", "value");
        writeValueToJson(gen, "value", value);
        gen.writeEnd().close();
    }

}
