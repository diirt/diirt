/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 *
 * @author carcassi
 */
public class MessageConnectionEvent extends Message {
    
    private final boolean connected;
    private final boolean writeConnected;

    public MessageConnectionEvent(int id, boolean connected, boolean writeConnected) {
        super(MessageType.EVENT, id);
        this.connected = connected;
        this.writeConnected = writeConnected;
    }

    public boolean isConnected() {
        return connected;
    }

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
