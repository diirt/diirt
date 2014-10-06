/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;


import org.epics.pvmanager.ChannelWriteCallback;
import org.epics.pvmanager.MultiplexedChannelHandler;

/**
 * Implementation for channels of a {@link LocalDataSource}.
 *
 * @author carcassi
 */
class WebPodsChannelHandler extends MultiplexedChannelHandler<WebPodsChannelHandler.ConnectionPayload, Object> {
    
    private final WebPodsDataSource dataSource;
    private final WebPodsChannelListener listener = new WebPodsChannelListener() {

        @Override
        public void onConnectionEvent(boolean connected, boolean writeConnected) {
            processConnection(new ConnectionPayload(connected, writeConnected));
        }

        @Override
        public void onValueEvent(Object value) {
            processMessage(value);
        }
        
    };
    private WebPodsChannel channel;

    WebPodsChannelHandler(WebPodsDataSource dataSource, String channelName) {
        super(channelName);
        this.dataSource = dataSource;
    }
    
    @Override
    public void connect() {
        channel = dataSource.getClient().subscribe(getChannelName(), listener);
    }

    @Override
    public void disconnect() {
        channel.unsubscribe();
        processConnection(null);
    }

    @Override
    protected boolean isConnected(WebPodsChannelHandler.ConnectionPayload payload) {
        return payload != null && payload.connected;
    }

    @Override
    protected boolean isWriteConnected(WebPodsChannelHandler.ConnectionPayload payload) {
        return payload != null && payload.writeConnected;
    }

    @Override
    protected void write(Object newValue, ChannelWriteCallback callback) {
    }
    
    static class ConnectionPayload {
        final boolean connected;
        final boolean writeConnected;

        public ConnectionPayload(boolean connected, boolean writeConnected) {
            this.connected = connected;
            this.writeConnected = writeConnected;
        }
    }
}
