/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import java.util.Objects;

import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VString;


/**
 *
 * @author carcassi
 */
class SystemPropertyChannelHandler extends SystemChannelHandler {

    private final String propertyName;
    static final String PREFIX = "system.";
    private String previousValue = null;

    public SystemPropertyChannelHandler(String channelName) {
        super(channelName);
        propertyName = channelName.substring(PREFIX.length());
    }

    @Override
    protected Object createValue() {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = "";
        }
        if (!Objects.equals(value, previousValue)) {
            previousValue = value;
            return VString.of(value, Alarm.none(), Time.now());
        } else {
            return null;
        }
    }

}
