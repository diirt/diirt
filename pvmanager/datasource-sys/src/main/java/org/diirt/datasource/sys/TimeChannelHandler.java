/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import org.diirt.util.time.Timestamp;
import org.diirt.util.time.TimestampFormat;
import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
class TimeChannelHandler extends SystemChannelHandler {

    private static final TimestampFormat timeFormat = new TimestampFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public TimeChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        Timestamp time = Timestamp.now();
        String formatted = timeFormat.format(time);
        return newVString(formatted, alarmNone(), newTime(time));
    }

}
