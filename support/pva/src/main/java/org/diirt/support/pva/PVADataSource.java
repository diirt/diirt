/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.formula.FormulaRegistry;
import org.epics.pvaccess.PVAException;
import org.epics.pvaccess.client.ChannelProvider;
import org.epics.pvaccess.client.impl.remote.ClientContextImpl;
import org.diirt.support.pva.formula.NTNDArrayFunctionSet;
import org.diirt.datasource.vtype.DataTypeSupport;

/**
 * 
 * @author msekoranja
 */
public class PVADataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
        
        // TODO move this out
        // Install formulas
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new NTNDArrayFunctionSet());
    }

    //private static final Logger logger = Logger.getLogger(PVADataSource.class.getName());

    private final short defaultPriority;
    private final ChannelProvider pvaChannelProvider;
    
    // this grabs internal implementation (and does not get ChannelProvider via ChannelAccess)
    // to allow clean shutdown (no such API for now)
    private final ClientContextImpl pvaContext;
    
    private final PVATypeSupport pvaTypeSupport = new PVATypeSupport(new PVAVTypeAdapterSet());
    
    public PVADataSource() {
    	this(ChannelProvider.PRIORITY_DEFAULT);  	
    }
    
    public PVADataSource(short defaultPriority) {
    	super(true);
        this.pvaContext = new ClientContextImpl();
        this.pvaChannelProvider = pvaContext.getProvider();
        this.defaultPriority = defaultPriority;
        
        // force initialization now
        try {
			pvaContext.initialize();
		} catch (PVAException e) {
			throw new RuntimeException("Failed to intialize pvAccess context.", e);
		}
    }

    public PVADataSource(ChannelProvider channelProvider, short defaultPriority) {
        super(true);
        this.pvaContext = null;
        this.pvaChannelProvider = channelProvider;
        this.defaultPriority = defaultPriority;
    }

    public short getDefaultPriority() {
        return defaultPriority;
    }
    
    public void close() {
    	// TODO destroy via ChannelProvider when API supports it
    	if (pvaContext != null)
    		pvaContext.dispose();
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        return new PVAChannelHandler(channelName, pvaChannelProvider, defaultPriority, pvaTypeSupport);
    }

}
