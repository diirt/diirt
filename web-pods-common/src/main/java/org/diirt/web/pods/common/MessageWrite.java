/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import java.io.Writer;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import org.epics.util.array.ListNumber;
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
        value = readValueFromJson(obj.get("value"));
    }

    public MessageWrite(int id, Object value) {
        super(MessageType.WRITE, id);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    
    @Override
    public void toJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject();
        gen.write("message", getMessage().toString().toLowerCase())
            .write("id", getId());
        writeValueToJson(gen, "value", value);
        gen.writeEnd()
            .close();
    }
    
    public static Object readValueFromJson(JsonValue msgValue) {
        if (msgValue instanceof JsonObject) {
            return VTypeToJson.toVType((JsonObject) msgValue);
        } else if (msgValue instanceof JsonNumber) {
            return ((JsonNumber) msgValue).doubleValue();
        } else if (msgValue instanceof JsonString){
            return ((JsonString) msgValue).getString();
        } else if (msgValue instanceof JsonArray){
            JsonArray array = (JsonArray) msgValue;
            if (isNumericArray(array)) {
                return toListDouble(array);
            } else if (isStringArray(array)) {
                return toListString(array);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static void writeValueToJson(JsonGenerator gen, String name, Object value) {
        if (value instanceof Number) {
            gen.write(name, ((Number) value).doubleValue());
        } else if (value instanceof String) {
            gen.write(name, (String) value);
        } else if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> strings = (List<String>) value;
            gen.write(name, fromListString(strings).build());
        } else if (value instanceof ListNumber) {
            gen.write(name, fromListNumber((ListNumber) value).build());
        } else if (value instanceof VType) {
            gen.write(name, VTypeToJson.toJson((VType) value));
        } else {
            throw new UnsupportedOperationException("Value " + value.getClass().getSimpleName() + " is not supported");
        }
    }
}
