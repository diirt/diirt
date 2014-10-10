/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

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
import static org.epics.vtype.json.JsonArrays.*;
import org.epics.vtype.json.VTypeToJson;

/**
 *
 * @author carcassi
 */
public abstract class Message {
    public static enum MessageType {SUBSCRIBE, CONNECTION, EVENT, WRITE, PAUSE, RESUME, UNSUBSCRIBE};
    
    private final MessageType message;
    private final int id;

    public Message(JsonObject obj) {
        this(Message.MessageType.valueOf(obj.getString("message").toUpperCase()), intMandatory(obj, "id"));
    }
    
    public Message(MessageType message, int id) {
        this.message = message;
        this.id = id;
    }

    public MessageType getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
    
    public void toJson(Writer writer) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    void basicToJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject();
        gen.write("message", getMessage().toString().toLowerCase())
            .write("id", getId())
            .writeEnd()
            .close();
    }
    
    static MessageType typeMandatory(JsonObject jObject, String name) {
        String message = stringMandatory(jObject, name);
        try {
            return MessageType.valueOf(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' must be a valid message type (was '" + message + "')", e);
        }
    }
    
    static String stringMandatory(JsonObject jObject, String name) {
        try {
            JsonString jsonString = jObject.getJsonString(name);
            if (jsonString == null) {
                throw new IllegalArgumentException("Missing message attribute '" + name + "'");
            } else {
                return jsonString.getString();
            }
        } catch (ClassCastException  e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' is not a string", e);
        }
    }
    
    static String stringOptional(JsonObject jObject, String name, String defaultValue) {
        try {
            JsonString jsonString = jObject.getJsonString(name);
            if (jsonString == null) {
                return defaultValue;
            } else {
                return jsonString.getString();
            }
        } catch (ClassCastException  e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' is not a string", e);
        }
    }
    
    static int intMandatory(JsonObject jObject, String name) {
        try {
            JsonNumber jsonNumber = jObject.getJsonNumber(name);
            if (jsonNumber == null) {
                throw new IllegalArgumentException("Missing message attribute '" + name + "'");
            } else {
                return jsonNumber.intValueExact();
            }
        } catch (ClassCastException | ArithmeticException e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' is not an integer", e);
        }
    }
    
    static int intOptional(JsonObject jObject, String name, int defaultValue) {
        try {
            JsonNumber jsonNumber = jObject.getJsonNumber(name);
            if (jsonNumber == null) {
                return defaultValue;
            } else {
                return jsonNumber.intValueExact();
            }
        } catch (ClassCastException | ArithmeticException e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' is not an integer", e);
        }
    }
    
    static boolean booleanAttribute(JsonObject jObject, String name) {
        try {
            return jObject.getBoolean(name);
        } catch (NullPointerException  e) {
            throw new IllegalArgumentException("Missing message attribute '" + name + "'");
        } catch (ClassCastException  e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' is not a boolean", e);
        }
    }
    
    static boolean booleanOptional(JsonObject jObject, String name, boolean defaultValue) {
        try {
            return jObject.getBoolean(name, defaultValue);
        } catch (NullPointerException  e) {
            throw new IllegalArgumentException("Message attribute '" + name + "' is not a boolean", e);
        }
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
