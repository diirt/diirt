/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

/**
 *
 * @author carcassi
 */
public class WebPodsChannel {

    private final int id;
    private final WebPodsChannelListener listener;
    private final WebPodsClient client;
    private final String channelName;

    public WebPodsChannel(String channelName, int id, WebPodsClient client, WebPodsChannelListener listener) {
        this.id = id;
        this.client = client;
        this.listener = listener;
        this.channelName = channelName;
    }

    public void pause() {
        client.pauseChannel(this);
    }

    public void resume() {
        client.resumeChannel(this);
    }

    public void unsubscribe() {
        client.unsubscribeChannel(this);
    }

    public WebPodsChannelListener getListener() {
        return listener;
    }

    public int getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

}
