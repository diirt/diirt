/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.Context;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.vtype.DataTypeSupport;
import com.cosylab.epics.caj.CAJContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.diirt.util.concurrent.Executors.namedPool;

/**
 * A data source that uses jca.
 * <p>
 * Type support can be configured by passing a custom {@link JCATypeSupport}
 * to the constructor.
 *
 * @author carcassi
 */
public class JCADataSource extends DataSource {

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

    private static final Logger log = Logger.getLogger(JCADataSource.class.getName());

    private final Context ctxt;
    private final int monitorMask;
    private final boolean varArraySupported;
    private final boolean dbePropertySupported;
    private final JCATypeSupport typeSupport;
    private final boolean rtypValueOnly;
    private final boolean honorZeroPrecision;

    /**
     * Creates a new data source using the parameters given in the configuration.
     *
     * @param configuration the configuration of the new data source
     */
    JCADataSource(JCADataSourceConfiguration configuration) {
        super(true);
        // Retrive data source properties

        if (configuration == null) {
            configuration = new JCADataSourceConfiguration();
        }

        ctxt = configuration.createContext();

        try {
            if (ctxt instanceof CAJContext) {
                ((CAJContext) ctxt).setDoNotShareChannels(true);
            }
        } catch (Throwable t) {
            log.log(Level.WARNING, "Couldn't change CAJContext to doNotShareChannels: this may cause some rare notification problems.", t);
        }

        // Default type support are the VTypes
        if (configuration.typeSupport == null) {
            typeSupport = new JCATypeSupport(new JCAVTypeAdapterSet());
        } else {
            typeSupport = configuration.typeSupport;
        }

        // Default support for var array needs to be detected
        if (configuration.varArraySupported == null) {
            varArraySupported = JCADataSourceConfiguration.isVarArraySupported(ctxt);
        } else {
            varArraySupported = configuration.varArraySupported;
        }

        monitorMask = configuration.monitorMask;
        dbePropertySupported = configuration.dbePropertySupported;
        rtypValueOnly = configuration.rtypValueOnly;
        honorZeroPrecision = configuration.honorZeroPrecision;

        if (useContextSwitchForAccessRightCallback()) {
            contextSwitch = Executors.newSingleThreadExecutor(namedPool("PVMgr JCA Workaround "));
        } else {
            contextSwitch = null;
        }

    }

    @Override
    public void close() {
        super.close();
        ctxt.dispose();
    }

    /**
     * The context used by the data source.
     *
     * @return the data source context
     */
    public Context getContext() {
        return ctxt;
    }

    /**
     * The monitor mask used for this data source.
     *
     * @return the monitor mask
     */
    public int getMonitorMask() {
        return monitorMask;
    }

    /**
     * Whether the metadata monitor should be established.
     *
     * @return true if using metadata monitors
     */
    public boolean isDbePropertySupported() {
        return dbePropertySupported;
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        return new JCAChannelHandler(channelName, this);
    }

    JCATypeSupport getTypeSupport() {
        return typeSupport;
    }

    /**
     * True whether the context can use variable arrays (all
     * array monitor request will have an element count of 0).
     *
     * @return true if variable size arrays are supported
     */
    public boolean isVarArraySupported() {
        return varArraySupported;
    }

    /**
     * True if should only ask value for RTYP fields.
     *
     * @return true if asking for value only
     */
    public boolean isRtypValueOnly() {
        return rtypValueOnly;
    }

    /**
     * True if zero precision should be honored, or disregarded.
     *
     * @return true if zero precision setting is honored
     */
    public boolean isHonorZeroPrecision() {
        return honorZeroPrecision;
    }

    final boolean useContextSwitchForAccessRightCallback() {
        return false;
    }

    ExecutorService getContextSwitch() {
        return contextSwitch;
    }

    private final ExecutorService contextSwitch;

}
