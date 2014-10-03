/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Executors;

import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.vtype.DataTypeSupport;
import org.epics.util.time.TimeDuration;

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

    /**
     * Creates a new data source.
     */
    public WebPodsDataSource() {
        super(true);
    }

    @Override
    public void close() {
        super.close();
    }
    
    @Override
    protected ChannelHandler createChannel(String channelName) {	
	return new WebPodsChannelHandler(this, channelName);
    }
    
}
