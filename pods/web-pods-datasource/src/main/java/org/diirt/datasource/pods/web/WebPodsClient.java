/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.diirt.pods.web.common.Message;
import org.diirt.pods.web.common.MessageConnectionEvent;
import org.diirt.pods.web.common.MessageDecoder;
import org.diirt.pods.web.common.MessageEncoder;
import org.diirt.pods.web.common.MessageErrorEvent;
import org.diirt.pods.web.common.MessagePause;
import org.diirt.pods.web.common.MessageResume;
import org.diirt.pods.web.common.MessageSubscribe;
import org.diirt.pods.web.common.MessageUnsubscribe;
import org.diirt.pods.web.common.MessageValueEvent;
import org.diirt.pods.web.common.MessageWriteCompletedEvent;

/**
 *
 * @author carcassi
 */
@ClientEndpoint (
    decoders = { MessageDecoder.class },
    encoders = { MessageEncoder.class }
)
public class WebPodsClient {

    private static final Logger log = Logger.getLogger(WebPodsClient.class.getName());
    private final Object lock = new Object();
    private Session session;
    private boolean connected = false;
    private String disconnectReason = "Connection failed";

    @OnOpen
    public void onOpen(Session session) {
        synchronized(lock) {
            this.session = session;
            connected = true;
            disconnectReason = null;
        }
        for (Map.Entry<Integer, WebPodsChannel> entrySet : channels.entrySet()) {
            WebPodsChannel channel = entrySet.getValue();
            subscribeChannel(channel);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        synchronized(lock) {
            this.session = null;
            connected = false;
            disconnectReason = closeReason.getReasonPhrase();
        }

        for (Map.Entry<Integer, WebPodsChannel> entrySet : channels.entrySet()) {
            WebPodsChannel channel = entrySet.getValue();
            channel.getListener().onDisconnect(closeReason);
        }
    }

    public boolean isConnected() {
        synchronized(lock) {
            return connected;
        }
    }

    public String getDisconnectReason() {
        synchronized(lock) {
            return disconnectReason;
        }
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        int channelId = message.getId();
        WebPodsChannel channel = channels.get(channelId);
        if (channel == null) {
            log.log(Level.WARNING, "Received an event for id " + channelId + " but no listener was found");
            return;
        }
        WebPodsChannelListener listener = channel.getListener();
        if (message instanceof MessageValueEvent) {
            MessageValueEvent event = (MessageValueEvent) message;
            listener.onValueEvent(event.getValue());
        } else if (message instanceof MessageConnectionEvent) {
            MessageConnectionEvent event = (MessageConnectionEvent) message;
            listener.onConnectionEvent(event.isConnected(), event.isWriteConnected());
        } else if (message instanceof MessageErrorEvent) {
            MessageErrorEvent event = (MessageErrorEvent) message;
            listener.onErrorEvent(event.getError());
        } else if (message instanceof MessageWriteCompletedEvent) {
            MessageWriteCompletedEvent event = (MessageWriteCompletedEvent) message;
            listener.onWriteCompletedEvent(event.isSuccessful(), event.getError());
        } else {
            log.log(Level.WARNING, "Received unsupported message " + message.getMessage() + " for id " + channelId);
        }
    }


    private final AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, WebPodsChannel> channels = new ConcurrentHashMap<>();

    public WebPodsChannel subscribe(String channelName, WebPodsChannelListener listener) {
        int id = counter.incrementAndGet();
        WebPodsChannel channel = new WebPodsChannel(channelName, id, this, listener);
        channels.put(id, channel);
        subscribeChannel(channel);
        return channel;
    }

    void subscribeChannel(WebPodsChannel channel) {
        Session currentSession;
        synchronized(lock) {
            currentSession = session;
        }
        if (currentSession == null) {
            return;
        }
        currentSession.getAsyncRemote().sendObject(new MessageSubscribe(channel.getId(), channel.getChannelName(), null, -1, true));
    }

    void unsubscribeChannel(WebPodsChannel channel) {
        channels.remove(channel.getId());
        Session currentSession;
        synchronized(lock) {
            currentSession = session;
        }
        if (currentSession == null) {
            return;
        }
        currentSession.getAsyncRemote().sendObject(new MessageUnsubscribe(channel.getId()));
    }

    void pauseChannel(WebPodsChannel channel) {
        channels.remove(channel.getId());
        Session currentSession;
        synchronized(lock) {
            currentSession = session;
        }
        if (currentSession == null) {
            return;
        }
        session.getAsyncRemote().sendObject(new MessagePause(channel.getId()));
    }

    void resumeChannel(WebPodsChannel channel) {
        channels.remove(channel.getId());
        Session currentSession;
        synchronized(lock) {
            currentSession = session;
        }
        if (currentSession == null) {
            return;
        }
        session.getAsyncRemote().sendObject(new MessageResume(channel.getId()));
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebPodsClient client = new WebPodsClient();
        container.connectToServer(client, new URI("ws://localhost:8080/web-pods/socket"));
        WebPodsChannel channel = client.subscribe("sim://noise", new WebPodsChannelListener() {

            @Override
            public void onConnectionEvent(boolean connected, boolean writeConnected) {
                System.out.println("Connected: " + connected + "; Write connected: " + writeConnected);
            }

            @Override
            public void onValueEvent(Object value) {
                System.out.println("New value: " + value);
            }

        });
        WebPodsChannel channel2 = client.subscribe("sim://table", new WebPodsChannelListener() {

            @Override
            public void onConnectionEvent(boolean connected, boolean writeConnected) {
                System.out.println("Connected: " + connected + "; Write connected: " + writeConnected);
            }

            @Override
            public void onValueEvent(Object value) {
                System.out.println("New value: " + value);
            }

        });
        Thread.sleep(5000);
        channel.unsubscribe();
        Thread.sleep(5000);
    }
}
