/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author carcassi
 */
public class MessageWriteCompletedEvent extends Message {
    
    private final String error;
    private final boolean successful;

    public MessageWriteCompletedEvent(JsonObject obj) {
        super(obj);
        this.error = stringOptional(obj, "error", null);
        this.successful = booleanAttribute(obj, "successful");
    }

    public MessageWriteCompletedEvent(int id) {
        super(MessageType.EVENT, id);
        this.successful = true;
        this.error = null;
    }

    public MessageWriteCompletedEvent(int id, String error) {
        super(MessageType.EVENT, id);
        this.successful = false;
        this.error = error;
    }

    public String getError() {
        return error;
    }

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
