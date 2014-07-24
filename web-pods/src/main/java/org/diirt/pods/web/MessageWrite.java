/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import javax.json.JsonObject;

/**
 *
 * @author carcassi
 */
public class MessageWrite extends Message {

    private final Object value;

    public MessageWrite(JsonObject obj) {
        super(obj);
        // TODO: parse the value
        value = null;
    }

    public Object getValue() {
        return value;
    }
    
    
    
}
