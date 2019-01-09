/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.diirt.util.config.TimeStampFormatter;
import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VString;

/**
 *
 * @author carcassi
 */
class TimeChannelHandler extends SystemChannelHandler {

    private static final DateTimeFormatter timeFormat = TimeStampFormatter.TIMESTAMP_FORMAT;
    public TimeChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        Instant time = Instant.now();
        String formatted = timeFormat.format(ZonedDateTime.ofInstant(time, ZoneId.systemDefault()));
        return VString.of(formatted, Alarm.none(), Time.of(time));
    }

}
