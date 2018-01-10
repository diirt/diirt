/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Message event for change of connection status.
 *
 * @author carcassi
 */
public class MessageConnectionEvent extends Message {

    private final boolean connected;
    private final boolean writeConnected;

    /**
     * Creates a new message based on the JSON representation.
     *
     * @param obj JSON object
     * @throws MessageDecodeException if json format is incorrect
     */
    public MessageConnectionEvent(JsonObject obj) throws MessageDecodeException {
        super(obj);
        this.connected = booleanMandatory(obj, "connected");
        this.writeConnected = booleanMandatory(obj, "writeConnected");
    }

    /**
     * Creates a new message based on the given parameters.
     *
     * @param id the channel id
     * @param connected whether it's connected
     * @param writeConnected whether the write is connected
     */
    public MessageConnectionEvent(int id, boolean connected, boolean writeConnected) {
        super(MessageType.EVENT, id);
        this.connected = connected;
        this.writeConnected = writeConnected;
    }

    /**
     * Whether the channel is connected.
     *
     * @return true if connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Whether the channel can be written to.
     *
     * @return true if write connected
     */
    public boolean isWriteConnected() {
        return writeConnected;
    }

    @Override
    public void toJson(Writer writer) {
        Json.createGenerator(writer).writeStartObject()
                .write("message", getMessage().toString().toLowerCase())
                .write("id", getId())
                .write("type", "connection")
                .write("connected", isConnected())
                .write("writeConnected", isWriteConnected())
                .writeEnd()
                .close();
    }

}
