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
 * Message to subscribe to a channel.
 *
 * @author carcassi
 */
public class MessageSubscribe extends Message {

    private final String channel;
    private final String type;
    private final int maxRate;
    private final boolean readOnly;

    /**
     * Creates a new message based on the JSON representation.
     *
     * @param obj JSON object
     * @throws MessageDecodeException if json format is incorrect
     */
    public MessageSubscribe(JsonObject obj) throws MessageDecodeException {
        super(obj);
        this.channel = stringMandatory(obj, "channel");
        this.type = stringOptional(obj, "type", null);
        this.maxRate = intOptional(obj, "maxRate", -1);
        this.readOnly = booleanOptional(obj, "readOnly", true);
    }

    /**
     * Creates a new message based on the given parameters.
     *
     * @param id the channel id
     * @param channel the channel name
     * @param type the type for the value
     * @param maxRate the maximum notification rate
     * @param readOnly whether it's read only
     */
    public MessageSubscribe(int id, String channel, String type, int maxRate, boolean readOnly) {
        super(MessageType.SUBSCRIBE, id);
        this.channel = channel;
        this.type = type;
        this.maxRate = maxRate;
        this.readOnly = readOnly;
    }

    /**
     * The channel name.
     *
     * @return the channel name
     */
    public String getChannel() {
        return channel;
    }

    /**
     * The requested type for values.
     *
     * @return the type name
     */
    public String getType() {
        return type;
    }

    /**
     * The maximum notification rate.
     *
     * @return min time in ms between sample
     */
    public int getMaxRate() {
        return maxRate;
    }

    /**
     * Whether the channel should be read-only.
     *
     * @return true if read-only connection
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject();
        gen.write("message", getMessage().toString().toLowerCase())
            .write("id", getId())
            .write("channel", getChannel());
        if (getType() != null) {
            gen.write("type", getType());
        }
        if (getMaxRate() != -1) {
            gen.write("maxRate", getMaxRate());
        }
        if (!isReadOnly()) {
            gen.write("readOnly", isReadOnly());
        }
        gen.writeEnd()
            .close();
    }

}
