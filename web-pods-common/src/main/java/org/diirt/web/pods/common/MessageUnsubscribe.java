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
public class MessageUnsubscribe extends Message {

    public MessageUnsubscribe(JsonObject obj) {
        super(obj);
    }

    public MessageUnsubscribe(int id) {
        super(MessageType.UNSUBSCRIBE, id);
    }
    
    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject();
        gen.write("message", getMessage().toString().toLowerCase())
            .write("id", getId())
            .writeEnd()
            .close();
    }
    
}
