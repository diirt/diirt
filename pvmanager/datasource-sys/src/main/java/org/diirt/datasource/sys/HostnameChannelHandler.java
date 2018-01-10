/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
class HostnameChannelHandler extends SystemChannelHandler {

    private String previousValue = null;

    public HostnameChannelHandler(String channelName) {
        super(channelName);
    }

    @Override
    protected Object createValue() {
        String hostname;
        Alarm alarm;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            alarm = alarmNone();
        } catch (UnknownHostException ex) {
            hostname = "Unknown host";
            alarm = newAlarm(AlarmSeverity.INVALID, "Undefined");
        }
        if (!Objects.equals(hostname, previousValue)) {
            previousValue = hostname;
            return newVString(hostname, alarm, timeNow());
        } else {
            return null;
        }
    }

}
