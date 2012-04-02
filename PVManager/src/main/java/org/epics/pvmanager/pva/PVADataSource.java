/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import gov.aps.jca.Channel;
import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;
import java.util.logging.Logger;
import org.epics.ca.client.impl.remote.ClientContextImpl;
import org.epics.ca.impl.remote.Context;
import org.epics.pvmanager.data.DataTypeSupport;

/**
 * 
 * @author carcassi
 */
public class PVADataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    private static final Logger logger = Logger.getLogger(PVADataSource.class.getName());

    private volatile Context pvaContext;
    private final short defaultPriority;

    public PVADataSource() {
        this(new ClientContextImpl(), Channel.PRIORITY_DEFAULT);
    }

    public PVADataSource(Context pvaContext, short defaultPriority) {
        super(true);
        this.pvaContext = pvaContext;
        // TODO Do I need to call this or not?
        // pvaContext.initialize();
        this.defaultPriority = defaultPriority;
    }

    
    protected Context getContext() {
        return pvaContext;
    }

    public short getDefaultPriority() {
        return defaultPriority;
    }
    
    public void close() {
        //pvaContext.dispose();
    }

    @Override
    protected ChannelHandler<?> createChannel(String channelName) {
        return new PVAChannelHandler(channelName, pvaContext, defaultPriority);
    }

}
