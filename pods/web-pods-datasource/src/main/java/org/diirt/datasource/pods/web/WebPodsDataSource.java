/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import static org.diirt.util.concurrent.Executors.namedPool;
import org.diirt.datasource.vtype.DataTypeSupport;

/**
 * Data source for web=pods.
 *
 * @author carcassi
 */
public final class WebPodsDataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    private final WebPodsClient client;
    private final URI socketLocation;
    private static final ExecutorService exec = Executors.newSingleThreadExecutor(namedPool("diirt web-pods datasource connection "));
    private static final Logger log = Logger.getLogger(WebPodsDataSource.class.getName());

    /**
     * Creates a new data source with the given configuration.
     *
     * @param configuration data source configuration
     */
    WebPodsDataSource(WebPodsDataSourceConfiguration configuration) {
        super(true);
        client = new WebPodsClient();
        this.socketLocation = configuration.getSocketLocation();
    }

    @Override
    public void close() {
        // TODO: close client
        super.close();
    }

    private void reconnect() {
        exec.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    if (client.isConnected()) {
                        return;
                    }
                    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                    container.connectToServer(client, getSocketLocation());
                    log.info("Web-pods datasource connected");
                } catch (DeploymentException | IOException ex) {
                    log.log(Level.WARNING, "Web-pods datasource connection problems", ex);
                }
            }
        });
    }

    WebPodsClient getClient() {
        if (!client.isConnected()) {
            reconnect();
        }
        return client;
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        return new WebPodsChannelHandler(this, channelName);
    }

    /**
     * The URI for the web pods server (e.g. ws://my.server.org/web-pods/socket).
     *
     * @return URI for the socket
     */
    public URI getSocketLocation() {
        return socketLocation;
    }

}
