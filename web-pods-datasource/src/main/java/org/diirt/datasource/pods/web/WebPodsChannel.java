/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import javax.websocket.Session;
import org.diirt.web.pods.common.MessagePause;
import org.diirt.web.pods.common.MessageResume;
import org.diirt.web.pods.common.MessageSubscribe;
import org.diirt.web.pods.common.MessageUnsubscribe;

/**
 *
 * @author carcassi
 */
public class WebPodsChannel {

    private final int id;
    private final Session session;
    private final WebPodsChannelListener listener;

    public WebPodsChannel(String channelName, int id, Session session, WebPodsChannelListener listener) {
        this.id = id;
        this.session = session;
        this.listener = listener;
        session.getAsyncRemote().sendObject(new MessageSubscribe(id, channelName, null, -1, true));
    }
    
    public void pause() {
        session.getAsyncRemote().sendObject(new MessagePause(id));
    }
    
    public void resume() {
        session.getAsyncRemote().sendObject(new MessageResume(id));
    }
    
    public void unsubscribe() {
        session.getAsyncRemote().sendObject(new MessageUnsubscribe(id));
    }
}
