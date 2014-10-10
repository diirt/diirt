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
public class MessageSubscribe extends Message {

    private final String pv;
    private final String type;
    private final int maxRate;
    private final boolean readOnly;

    public MessageSubscribe(JsonObject obj) {
        super(obj);
        this.pv = stringMandatory(obj, "pv");
        this.type = stringOptional(obj, "type", null);
        this.maxRate = intOptional(obj, "maxRate", -1);
        this.readOnly = booleanOptional(obj, "readOnly", true);
    }
    
    public MessageSubscribe(int id, String pv, String type, int maxRate, boolean readOnly) {
        super(MessageType.SUBSCRIBE, id);
        this.pv = pv;
        this.type = type;
        this.maxRate = maxRate;
        this.readOnly = readOnly;
    }

    public String getPv() {
        return pv;
    }

    public String getType() {
        return type;
    }

    public int getMaxRate() {
        return maxRate;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject();
        gen.write("message", getMessage().toString().toLowerCase())
            .write("id", getId())
            .write("pv", getPv());
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
