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
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.vtype.DataTypeSupport;

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
    private final WebPodsDataSourceConfiguration configuration;

    public WebPodsDataSource() {
        this(WebPodsDataSourceConfiguration.readConfiguration(new WebPodsDataSourceFactory().getDefaultConfPath()));
    }

    /**
     * Creates a new data source.
     * 
     * @param configuration datasource configuration
     */
    public WebPodsDataSource(WebPodsDataSourceConfiguration configuration) {
        super(true);
        client = new WebPodsClient();
        this.configuration = configuration;
        PVManager.getReadScannerExecutorService().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                    container.connectToServer(client, WebPodsDataSource.this.configuration.getSocketLocation());
                } catch (DeploymentException | IOException ex) {
                    Logger.getLogger(WebPodsDataSource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
