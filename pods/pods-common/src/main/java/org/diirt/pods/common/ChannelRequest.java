/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

/**
 * The input for a translation.
 *
 * @author carcassi
 */
public class ChannelRequest {

    private final String channel;
    private final String user;
    private final String role;
    private final String group;
    private final String address;

    public ChannelRequest(String channel) {
        this(channel, null, null, null, null);
    }



    public ChannelRequest(String channel, String user, String role, String group, String address) {
        this.channel = channel;
        this.user = user;
        this.role = role;
        this.group = group;
        this.address = address;
    }

    public String getChannel() {
        return channel;
    }

    public String getUser() {
        return user;
    }

    public String getGroup() {
        return group;
    }

    public String getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

}
