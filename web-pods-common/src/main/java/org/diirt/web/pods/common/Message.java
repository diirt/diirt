/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.web.pods.common;

import java.io.Writer;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;

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
    
}
