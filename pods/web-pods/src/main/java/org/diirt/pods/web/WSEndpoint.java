/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import org.diirt.pods.web.common.MessageValueEvent;
import org.diirt.pods.web.common.MessageConnectionEvent;
import org.diirt.pods.web.common.Message;
import org.diirt.pods.web.common.MessageWriteCompletedEvent;
import org.diirt.pods.web.common.MessageWrite;
import org.diirt.pods.web.common.MessageSubscribe;
import org.diirt.pods.web.common.MessageDecoder;
import org.diirt.pods.web.common.MessageErrorEvent;
import org.diirt.pods.web.common.MessageUnsubscribe;
import org.diirt.pods.web.common.MessageEncoder;
import org.diirt.pods.web.common.MessageResume;
import org.diirt.pods.web.common.MessagePause;
import java.io.InputStream;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.diirt.pods.common.ChannelTranslation;
import org.diirt.pods.common.ChannelTranslator;
import org.diirt.util.config.Configuration;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import static org.diirt.datasource.formula.ExpressionLanguage.*;
import org.diirt.util.time.TimeDuration;

/**
 *
 * @author carcassi
 */
@ServerEndpoint(value = "/socket", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class}, configurator = WSEndpointConfigurator.class)
public class WSEndpoint {
    
    // TODO: understand lifecycle of whole web application and put
    // configuration there, including closing datasources.
    static {
        ChannelTranslator temp = null;
        try (InputStream input = Configuration.getFileAsStream("pods/web/mappings.xml", new WSEndpoint(), "mappings.default.xml")) {
            temp = ChannelTranslator.loadTranslator(input);
        } catch (Exception ex) {
            Logger.getLogger(WSEndpoint.class.getName()).log(Level.SEVERE, "Couldn't load DIIRT_HOME/pods/web/mappings", ex);
        }
        channelTranslator = temp;
    }
    
    private static Logger log = Logger.getLogger(WSEndpoint.class.getName());
    private static final ChannelTranslator channelTranslator;
    
    // XXX: need to understand how state can actually be used
    private final Map<Integer, PVReader<?>> channels = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getMessage()) {
            case SUBSCRIBE:
                onSubscribe(session, (MessageSubscribe) message);
                return;
            case UNSUBSCRIBE:
                onUnsubscribe(session, (MessageUnsubscribe) message);
                return;
            case PAUSE:
                onPause(session, (MessagePause) message);
                return;
            case RESUME:
                onResume(session, (MessageResume) message);
                return;
            case WRITE:
                onWrite(session, (MessageWrite) message);
                return;
            default:
                throw new UnsupportedOperationException("Message '" + message.getMessage() + "' not yet supported");
        }
    }

    private void onSubscribe(final Session session, final MessageSubscribe message) {
        // TODO: check id already used
        double maxRate = 1;
        if (message.getMaxRate() != -1) {
            maxRate = message.getMaxRate();
        }
        
        ChannelTranslation translation = channelTranslator.translate(message.getChannel());
        
        PVReader<?> reader;
        if (message.isReadOnly()) {
            reader = PVManager.read(formula(translation.getFormula()))
                .readListener(new ReadOnlyListener(session, message))
                .maxRate(TimeDuration.ofHertz(maxRate));
        } else {
            ReadWriteListener readWriteListener = new ReadWriteListener(session, message);
            reader = PVManager.readAndWrite(formula(translation.getFormula()))
                .readListener(readWriteListener)
                .writeListener(readWriteListener)
                .asynchWriteAndMaxReadRate(TimeDuration.ofHertz(maxRate));
        }
        channels.put(message.getId(), reader);
    }

    private void onUnsubscribe(Session session, MessageUnsubscribe message) {
        PVReader<?> channel = channels.get(message.getId());
        if (channel != null) {
            channel.close();
        }
    }

    private void onPause(Session session, MessagePause message) {
        PVReader<?> channel = channels.get(message.getId());
        if (channel != null) {
            channel.setPaused(true);
        }
    }

    private void onResume(Session session, MessageResume message) {
        PVReader<?> channel = channels.get(message.getId());
        if (channel != null) {
            channel.setPaused(false);
        }
    }

    private void onWrite(Session session, MessageWrite message) {
        PVReader<?> channel = channels.get(message.getId());
        if (channel instanceof PVWriter) {
            @SuppressWarnings("unchecked")
            PVWriter<Object> channelWriter = (PVWriter<Object>) channel;
            channelWriter.write(message.getValue());
        } else {
            if (channel == null) {
                sendError(session, message.getId(), "Channel id '" + message.getId() + "' is not open");
            } else {
                sendError(session, message.getId(), "Channel id '" + message.getId() + "' is read-only");
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("session");
        String remoteHost = (String) httpSession.getAttribute("remoteHost");
        Principal user = session.getUserPrincipal();
        System.out.println("Address " + remoteHost);
        System.out.println("User " + user);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        for (Map.Entry<Integer, PVReader<?>> entry : channels.entrySet()) {
            PVReader<?> channel = entry.getValue();
            channel.close();
        }
        closed = true;
    }
    
    private volatile boolean closed = false;

    @OnError
    public void onError(Session session, Throwable cause) {
        System.out.println(session.getRequestURI() + ": ERROR");
        cause.printStackTrace();
    }
    
    public void sendError(Session session, int id, String message) {
        session.getAsyncRemote().sendObject(new MessageErrorEvent(id, message));
    }

    private class ReadOnlyListener implements PVReaderListener<Object> {

        private final Session session;
        private final MessageSubscribe message;

        public ReadOnlyListener(Session session, MessageSubscribe message) {
            this.session = session;
            this.message = message;
        }

        @Override
        public void pvChanged(PVReaderEvent<Object> event) {
            if (closed) {
                log.log(Level.SEVERE, "Getting event after channel was closed for " + event.getPvReader().getName());
                event.getPvReader().close();
                return;
            }
            if (event.isConnectionChanged()) {
                session.getAsyncRemote().sendObject(new MessageConnectionEvent(message.getId(), event.getPvReader().isConnected(), false));
            }
            if (event.isValueChanged()) {
                session.getAsyncRemote().sendObject(new MessageValueEvent(message.getId(), event.getPvReader().getValue()));
            }
            if (event.isExceptionChanged()) {
                session.getAsyncRemote().sendObject(new MessageErrorEvent(message.getId(), event.getPvReader().lastException().getMessage()));
            }
        }
    }
    
    private static PV<Object, Object> pv(PVWriter<Object> reader) {
        @SuppressWarnings("unchecked")
        PV<Object, Object> channel = (PV<Object, Object>) reader;
        return channel;
    }

    private class ReadWriteListener implements PVReaderListener<Object>, PVWriterListener<Object> {

        private final Session session;
        private final MessageSubscribe message;

        public ReadWriteListener(Session session, MessageSubscribe message) {
            this.session = session;
            this.message = message;
        }

        @Override
        public void pvChanged(PVReaderEvent<Object> event) {
            if (closed) {
                log.log(Level.SEVERE, "Getting event after channel was closed for " + event.getPvReader().getName());
                event.getPvReader().close();
                return;
            }
            if (event.isValueChanged()) {
                session.getAsyncRemote().sendObject(new MessageValueEvent(message.getId(), event.getPvReader().getValue()));
            }
            if (event.isExceptionChanged()) {
                session.getAsyncRemote().sendObject(new MessageErrorEvent(message.getId(), event.getPvReader().lastException().getMessage()));
            }
        }

        @Override
        public void pvChanged(PVWriterEvent<Object> event) {
            if (closed) {
                log.log(Level.SEVERE, "Getting event after channel was closed for " + event.getPvWriter());
                event.getPvWriter().close();
                return;
            }
            if (event.isConnectionChanged()) {
                session.getAsyncRemote().sendObject(new MessageConnectionEvent(message.getId(), pv(event.getPvWriter()).isConnected(), event.getPvWriter().isWriteConnected()));
            }
            if (event.isWriteSucceeded()) {
                session.getAsyncRemote().sendObject(new MessageWriteCompletedEvent(message.getId()));
            }
            if (event.isWriteFailed()) {
                session.getAsyncRemote().sendObject(new MessageWriteCompletedEvent(message.getId(), event.getPvWriter().lastWriteException().getMessage()));
            }
            if (event.isExceptionChanged()) {
                session.getAsyncRemote().sendObject(new MessageErrorEvent(message.getId(), event.getPvWriter().lastWriteException().getMessage()));
            }
        }
    }
}
