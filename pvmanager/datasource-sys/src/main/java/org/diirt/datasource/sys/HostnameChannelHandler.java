/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.AlarmStatus;
import org.epics.vtype.Time;
import org.epics.vtype.VString;

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
            alarm = Alarm.none();
        } catch (UnknownHostException ex) {
            hostname = "Unknown host";
            alarm = Alarm.of(AlarmSeverity.INVALID, AlarmStatus.NONE, "Undefined");
        }
        if (!Objects.equals(hostname, previousValue)) {
            previousValue = hostname;
            return VString.of(hostname, alarm, Time.now());
        } else {
            return null;
        }
    }

}
