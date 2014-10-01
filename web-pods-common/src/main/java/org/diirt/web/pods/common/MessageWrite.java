/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.epics.vtype.VType;
import org.epics.vtype.json.VTypeToJson;
import static org.epics.vtype.json.JsonArrays.*;

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
        } else if (msgValue instanceof JsonArray){
            JsonArray array = (JsonArray) msgValue;
            if (isNumericArray(array)) {
                value = toListDouble(array);
            } else if (isStringArray(array)) {
                value = toListString(array);
            } else {
                value = null;
            }
        } else {
            value = null;
        }
    }

    public MessageWrite(int id, Object value) {
        super(MessageType.WRITE, id);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    
    
    
}
