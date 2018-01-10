/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
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
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.VType;
import static org.diirt.vtype.json.JsonArrays.*;
import org.diirt.vtype.json.VTypeToJson;

/**
 * A message being sent as part of the WebPods protocol.
 * <p>
 * Subclasses provide serialization (through {@link #toJson(java.io.Writer)} and
 * de-serialization (through the constructor) of JSON messages. Each instance of
 * the message is immutable.
 *
 * @author carcassi
 */
public abstract class Message {

    /**
     * The type of message.
     */
    public static enum MessageType {SUBSCRIBE, CONNECTION, EVENT, WRITE, PAUSE, RESUME, UNSUBSCRIBE};

    private final MessageType message;
    private final int id;

    /**
     * Constructor for JSON parsing. Retrieves message and id from payload.
     *
     * @param obj the JSON message
     * @throws MessageDecodeException if json format is incorrect
     */
    Message(JsonObject obj) throws MessageDecodeException {
        this(Message.MessageType.valueOf(obj.getString("message").toUpperCase()), intMandatory(obj, "id"));
    }

    /**
     * Constructor for direct message creation.
     *
     * @param message the type of the message
     * @param id the id for the channel
     */
    Message(MessageType message, int id) {
        if (message == null)
            throw new NullPointerException("The message type cannot be null");
        this.message = message;
        this.id = id;
    }

    /**
     * The message type. Can't be null.
     *
     * @return the message type
     */
    public MessageType getMessage() {
        return message;
    }

    /**
     * The id of the channel this message refers to.
     *
     * @return the channel id
     */
    public int getId() {
        return id;
    }

    /**
     * Serializes this message as JSON onto the given writer.
     *
     * @param writer the destination where to serialize the message
     */
    public void toJson(Writer writer) {
        // Default implementation just throws an exception, so it is easy
        // to find messages with unimplemented serializations
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Utility to serialize just the message and id. More then one message
     * only serialize those two parameters, so it is useful to make it common.
     *
     * @param writer the destination where to serialize the message
     */
    void basicToJson(Writer writer) {
        JsonGenerator gen = Json.createGenerator(writer).writeStartObject();
        gen.write("message", getMessage().toString().toLowerCase())
            .write("id", getId())
            .writeEnd()
            .close();
    }

    /**
     * Un-marshals the message type, or throws an exception if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the type is stored
     * @return the message type
     * @throws MessageDecodeException if the field is missing or of the wrong type
     */
    static MessageType typeMandatory(JsonObject jObject, String name) throws MessageDecodeException {
        String message = stringMandatory(jObject, name);
        try {
            return MessageType.valueOf(message);
        } catch (IllegalArgumentException e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "valid message type");
        }
    }

    /**
     * Un-marshals a string, or throws an exception if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the string is stored
     * @return the message string
     * @throws MessageDecodeException if the field is missing or of the wrong type
     */
    static String stringMandatory(JsonObject jObject, String name) throws MessageDecodeException {
        try {
            JsonString jsonString = jObject.getJsonString(name);
            if (jsonString == null) {
                throw MessageDecodeException.missingMandatoryAttribute(jObject, name);
            } else {
                return jsonString.getString();
            }
        } catch (ClassCastException  e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "string");
        }
    }

    /**
     * Un-marshals a string, or returns the default value if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the string is stored
     * @param defaultValue the value to use if no attribute is found
     * @return the message string
     * @throws MessageDecodeException if the field is of the wrong type
     */
    static String stringOptional(JsonObject jObject, String name, String defaultValue) throws MessageDecodeException {
        try {
            JsonString jsonString = jObject.getJsonString(name);
            if (jsonString == null) {
                return defaultValue;
            } else {
                return jsonString.getString();
            }
        } catch (ClassCastException  e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "string");
        }
    }

    /**
     * Un-marshals an integer, or throws an exception if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the integer is stored
     * @return the message integer
     * @throws MessageDecodeException if the field is missing or of the wrong type
     */
    static int intMandatory(JsonObject jObject, String name) throws MessageDecodeException {
        try {
            JsonNumber jsonNumber = jObject.getJsonNumber(name);
            if (jsonNumber == null) {
                throw MessageDecodeException.missingMandatoryAttribute(jObject, name);
            } else {
                return jsonNumber.intValueExact();
            }
        } catch (ClassCastException | ArithmeticException e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "integer");
        }
    }

    /**
     * Un-marshals an integer, or returns the default value if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the integer is stored
     * @param defaultValue the value to use if no attribute is found
     * @return the message integer
     * @throws MessageDecodeException if the field is of the wrong type
     */
    static int intOptional(JsonObject jObject, String name, int defaultValue) throws MessageDecodeException {
        try {
            JsonNumber jsonNumber = jObject.getJsonNumber(name);
            if (jsonNumber == null) {
                return defaultValue;
            } else {
                return jsonNumber.intValueExact();
            }
        } catch (ClassCastException | ArithmeticException e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "integer");
        }
    }

    /**
     * Un-marshals a boolean, or throws an exception if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the boolean is stored
     * @return the message boolean
     * @throws MessageDecodeException if the field is missing or of the wrong type
     */
    static boolean booleanMandatory(JsonObject jObject, String name) throws MessageDecodeException {
        try {
            return jObject.getBoolean(name);
        } catch (NullPointerException  e) {
            throw MessageDecodeException.missingMandatoryAttribute(jObject, name);
        } catch (ClassCastException  e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "boolean");
        }
    }

    /**
     * Un-marshals an boolean, or returns the default value if it's not able to.
     *
     * @param jObject the JSON object
     * @param name the attribute name where the boolean is stored
     * @param defaultValue the value to use if no attribute is found
     * @return the message boolean
     * @throws MessageDecodeException if the field is of the wrong type
     */
    static boolean booleanOptional(JsonObject jObject, String name, boolean defaultValue) throws MessageDecodeException {
        try {
            return jObject.getBoolean(name, defaultValue);
        } catch (NullPointerException  e) {
            throw MessageDecodeException.wrongAttributeType(jObject, name, "boolean");
        }
    }

    /**
     * Converts the given JSON value to either a vtype, a Java time or a
     * ListNumber.
     *
     * @param msgValue the JSON value
     * @return the converted type
     */
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

    /**
     * Converts the given value to a JSON representation.
     *
     * @param gen the object to create/store the JSON representation
     * @param name the JSON name to store the value as
     * @param value the value to store
     */
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
