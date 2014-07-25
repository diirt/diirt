/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import java.io.Writer;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;

/**
 *
 * @author carcassi
 */
public class MessageValueEvent extends Message {
    
    private final Object value;

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
        
        if (getValue() instanceof Number) {
            gen.write("value", ((Number) getValue()).doubleValue());
        } else if (getValue() instanceof String) {
            gen.write("value", (String) getValue());
        } else if (getValue() instanceof VNumber) {
            gen.write("value", ((VNumber) getValue()).getValue().doubleValue());
        } else if (getValue() instanceof VString) {
            gen.write("value", ((VString) getValue()).getValue());
        }
        
        gen.writeEnd().close();
    }
    
}
