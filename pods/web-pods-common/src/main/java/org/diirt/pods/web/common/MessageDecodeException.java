/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web.common;

import javax.json.JsonObject;
import javax.websocket.DecodeException;

/**
 * Exception for decoded message that do match the spec.
 *
 * @author carcassi
 */
public class MessageDecodeException extends DecodeException {

    private final JsonObject jObject;

    public MessageDecodeException(String encodedString, String message, JsonObject jObject) {
        super(encodedString, message);
        this.jObject = jObject;
    }

    public JsonObject getJObject() {
        return jObject;
    }

    public String getMessageType() {
        if (jObject == null) {
            return null;
        }

        return jObject.getString("message", null);
    }

    public int getId() {
        if (jObject == null) {
            return -1;
        }

        return jObject.getInt("id", -1);
    }

    public static MessageDecodeException unsupportedMessage(JsonObject jObject) {
        String messageType = jObject.getString("message", null);
        if (messageType != null) {
            return new MessageDecodeException("message", "Message " + messageType + " is not supported", jObject);
        } else {
            return new MessageDecodeException("message", "Message field is missing", jObject);
        }
    }

    public static MessageDecodeException missingMandatoryAttribute(JsonObject jObject, String name) {
        return new MessageDecodeException("name", "Missing message attribute '" + name + "'", jObject);
    }

    public static MessageDecodeException wrongAttributeType(JsonObject jObject, String name, String type) {
        return new MessageDecodeException("name", "Message attribute '" + name + "' is not a " + type, jObject);
    }

}
