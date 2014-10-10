/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author carcassi
 */
public class MessageErrorEvent extends Message {
    
    private final String error;

    public MessageErrorEvent(JsonObject obj) {
        super(obj);
        this.error = stringMandatory(obj, "error");
    }
    
    public MessageErrorEvent(int id, String error) {
        super(MessageType.EVENT, id);
        this.error = error;
    }

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
