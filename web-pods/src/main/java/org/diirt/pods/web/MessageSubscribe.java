/*
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.web;

import javax.json.JsonObject;

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
    
}
