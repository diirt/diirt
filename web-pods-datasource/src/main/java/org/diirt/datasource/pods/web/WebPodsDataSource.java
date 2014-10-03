/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.vtype.DataTypeSupport;

/**
 * Data source for locally written data. Each instance of this
 * data source will have its own separate channels and values.
 *
 * @author carcassi
 */
public final class WebPodsDataSource extends DataSource {

    static {
	// Install type support for the types it generates.
	DataTypeSupport.install();
    }
    
    private final WebPodsClient client;

    /**
     * Creates a new data source.
     */
    public WebPodsDataSource() {
        super(true);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        client = new WebPodsClient();
        try {
            container.connectToServer(client, new URI("ws://localhost:8080/web-pods/socket"));
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            Logger.getLogger(WebPodsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    WebPodsClient getClient() {
        return client;
    }
   
    @Override
    protected ChannelHandler createChannel(String channelName) {	
	return new WebPodsChannelHandler(this, channelName);
    }
    
}
