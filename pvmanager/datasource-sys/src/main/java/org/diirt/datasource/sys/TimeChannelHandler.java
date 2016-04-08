/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import static org.diirt.vtype.ValueFactory.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author carcassi
 */
class TimeChannelHandler extends SystemChannelHandler {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

    public TimeChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        Instant time = Instant.now();
        String formatted = timeFormat.format(ZonedDateTime.ofInstant(time, ZoneId.systemDefault()));
        return newVString(formatted, alarmNone(), newTime(time));
    }

}
