/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.vtype.DataTypeSupport;
import org.epics.pvaccess.ClientFactory;
import org.epics.pvaccess.client.ChannelProvider;
import org.epics.pvaccess.client.ChannelProviderRegistry;
import org.epics.pvaccess.client.ChannelProviderRegistryFactory;

/**
 *
 * @author msekoranja
 */
public class PVADataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    //private static final Logger logger = Logger.getLogger(PVADataSource.class.getName());

    private final short defaultPriority;
    private final ChannelProvider pvaChannelProvider;

    private final PVATypeSupport pvaTypeSupport = new PVATypeSupport(new PVAVTypeAdapterSet());

    public PVADataSource() {
        this(ChannelProvider.PRIORITY_DEFAULT);
    }

    public PVADataSource(short defaultPriority) {
        super(true);

        this.defaultPriority = defaultPriority;

                try {
                        ClientFactory.start();
                    final ChannelProviderRegistry registry = ChannelProviderRegistryFactory.getChannelProviderRegistry();
                    this.pvaChannelProvider = registry.createProvider("pva");
                    if (this.pvaChannelProvider == null)
                        throw new RuntimeException("pvAccess ChannelProvider not installed");

                } catch (Throwable th) {
                        throw new RuntimeException("Failed to intialize pvAccess context.", th);
                }
    }

    public PVADataSource(ChannelProvider channelProvider, short defaultPriority) {
        super(true);
        this.pvaChannelProvider = channelProvider;
        this.defaultPriority = defaultPriority;
    }

    public short getDefaultPriority() {
        return defaultPriority;
    }

    public void close() {
        if (this.pvaChannelProvider != null)
                pvaChannelProvider.destroy();
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        return PVAChannelHandler.create(channelName, pvaChannelProvider, defaultPriority, pvaTypeSupport);
    }

}
