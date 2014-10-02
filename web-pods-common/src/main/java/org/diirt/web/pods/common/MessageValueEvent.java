/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import java.io.Writer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author carcassi
 */
public class MessageValueEvent extends Message {
    
    private final Object value;
    
    public MessageValueEvent(JsonObject obj) {
        super(obj);
        value = MessageWrite.readValueFromJson(obj.get("value"));
    }

    public MessageValueEvent(int id, Object value) {
        super(MessageType.EVENT, id);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    
    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject()
                .write("message", getMessage().toString().toLowerCase())
                .write("id", getId())
                .write("type", "value");
        MessageWrite.writeValueToJson(gen, "value", value);        
        gen.writeEnd().close();
    }
    
}
