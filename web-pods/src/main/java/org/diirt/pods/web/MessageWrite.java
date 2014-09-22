/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.epics.vtype.VType;
import org.epics.vtype.json.VTypeToJson;

/**
 *
 * @author carcassi
 */
public class MessageWrite extends Message {

    private final Object value;

    public MessageWrite(JsonObject obj) {
        super(obj);
        JsonValue msgValue = obj.get("value");
        // Parse value
        if (msgValue instanceof JsonObject) {
            value = VTypeToJson.toVType((JsonObject) msgValue);
        } else if (msgValue instanceof JsonNumber) {
            value = ((JsonNumber) msgValue).doubleValue();
        } else if (msgValue instanceof JsonString){
            value = ((JsonString) msgValue).getString();
        } else {
            value = null;
        }
    }

    public Object getValue() {
        return value;
    }
    
    
    
}
