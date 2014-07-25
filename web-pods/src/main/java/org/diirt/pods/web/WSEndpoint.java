/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.PVWriterEvent;
import org.epics.pvmanager.PVWriterListener;
import static org.epics.pvmanager.formula.ExpressionLanguage.*;
import org.epics.pvmanager.loc.LocalDataSource;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.util.time.TimeDuration;

/**
 *
 * @author carcassi
 */
@ServerEndpoint(value = "/socket", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WSEndpoint {
    
    // TODO: understand lifecycle of whole web application and put
    // configuration there, including closing datasources.
    static {
        CompositeDataSource datasource = new CompositeDataSource();
        datasource.putDataSource("sim", new SimulationDataSource());
        datasource.putDataSource("loc", new LocalDataSource());
        PVManager.setDefaultDataSource(datasource);
    }
    
    // XXX: need to understand how state can actually be used
    private final Map<Integer, PVReader<?>> pvs = new ConcurrentHashMap<Integer, PVReader<?>>();

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getMessage()) {
            case SUBSCRIBE:
                onSubscribe(session, (MessageSubscribe) message);
                return;
            case UNSUBSCRIBE:
                onUnsubscribe(session, (MessageSubscribe) message);
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
        
        PVReader<?> reader;
        if (message.isReadOnly()) {
            reader = PVManager.read(formula(message.getPv()))
                .readListener(new ReadOnlyListener(session, message))
                .maxRate(TimeDuration.ofHertz(maxRate));
        } else {
            ReadWriteListener readWriteListener = new ReadWriteListener(session, message);
            reader = PVManager.readAndWrite(formula(message.getPv()))
                .readListener(readWriteListener)
                .writeListener(readWriteListener)
                .asynchWriteAndMaxReadRate(TimeDuration.ofHertz(maxRate));
        }
        pvs.put(message.getId(), reader);
    }

    private void onUnsubscribe(Session session, MessageSubscribe message) {
        PVReader<?> pv = pvs.get(message.getId());
        if (pv != null) {
            pv.close();
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println(session.getRequestURI() + ": OPEN");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        for (Map.Entry<Integer, PVReader<?>> entry : pvs.entrySet()) {
            PVReader<?> pvReader = entry.getValue();
            pvReader.close();
        }
    }

    @OnError
    public void onError(Session session, Throwable cause) {
        System.out.println(session.getRequestURI() + ": ERROR");
        cause.printStackTrace();
    }

    private static class ReadOnlyListener implements PVReaderListener<Object> {

        private final Session session;
        private final MessageSubscribe message;

        public ReadOnlyListener(Session session, MessageSubscribe message) {
            this.session = session;
            this.message = message;
        }

        @Override
        public void pvChanged(PVReaderEvent<Object> event) {
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
        PV<Object, Object> pv = (PV<Object, Object>) reader;
        return pv;
    }

    private static class ReadWriteListener implements PVReaderListener<Object>, PVWriterListener<Object> {

        private final Session session;
        private final MessageSubscribe message;

        public ReadWriteListener(Session session, MessageSubscribe message) {
            this.session = session;
            this.message = message;
        }

        @Override
        public void pvChanged(PVReaderEvent<Object> event) {
            if (event.isValueChanged()) {
                session.getAsyncRemote().sendObject(new MessageValueEvent(message.getId(), event.getPvReader().getValue()));
            }
            if (event.isExceptionChanged()) {
                session.getAsyncRemote().sendObject(new MessageErrorEvent(message.getId(), event.getPvReader().lastException().getMessage()));
            }
        }

        @Override
        public void pvChanged(PVWriterEvent<Object> event) {
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
