/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
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
import org.diirt.web.pods.common.Message;
import org.diirt.web.pods.common.MessageConnectionEvent;
import org.diirt.web.pods.common.MessageDecoder;
import org.diirt.web.pods.common.MessageEncoder;
import org.diirt.web.pods.common.MessageErrorEvent;
import org.diirt.web.pods.common.MessageValueEvent;
import org.diirt.web.pods.common.MessageWriteCompletedEvent;

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
    private volatile Session session;
 
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }
 
    @OnMessage
    public void onMessage(Message message, Session session) {
        int channelId = message.getId();
        WebPodsChannelListener listener = listeners.get(channelId);
        if (listener == null) {
            log.log(Level.WARNING, "Received an event for id " + channelId + " but no listener was found");
            return;
        }
        
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
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info(String.format("Session %s close because of %s", session.getId(), closeReason));
    }
    
    private AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, WebPodsChannelListener> listeners = new ConcurrentHashMap<>();
    
    public WebPodsChannel subscribe(String channelName, WebPodsChannelListener listener) {
        int id = counter.incrementAndGet();
        WebPodsChannel channel = new WebPodsChannel(channelName, id, session, listener);
        listeners.put(id, listener);
        return channel;
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
