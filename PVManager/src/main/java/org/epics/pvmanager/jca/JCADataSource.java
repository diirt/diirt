/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.CAException;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.data.DataTypeSupport;
import com.cosylab.epics.caj.CAJContext;
import gov.aps.jca.jni.JNIContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

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

    /**
     * Creates a new data source using pure Java implementation
     * @deprecated use {@link JCADataSourceBuilder}
     */
    @Deprecated
    public JCADataSource() {
        this(JCALibrary.CHANNEL_ACCESS_JAVA, Monitor.VALUE | Monitor.ALARM);
    }
    
    /**
     * Creates a new data source using the given context. The context will
     * never be closed.
     * 
     * @param jcaContext the context to be used
     * @param monitorMask Monitor.VALUE, ...
     * @deprecated use {@link JCADataSourceBuilder}
     */
    @Deprecated
    public JCADataSource(Context jcaContext, int monitorMask) {
        this(jcaContext, monitorMask, new JCATypeSupport(new JCAVTypeAdapterSet()));
    }

    /**
     * Creates a new data source using the className to create the context.
     *
     * @param className JCALibrary.CHANNEL_ACCESS_JAVA, JCALibrary.JNI_THREAD_SAFE, ...
     * @param monitorMask Monitor.VALUE, ...
     * @deprecated use {@link JCADataSourceBuilder}
     */
    @Deprecated
    public JCADataSource(String className, int monitorMask) {
        this(createContext(className), monitorMask);
    }
    
    private static Context createContext(String className) {
        return JCADataSourceBuilder.createContext(className);
    }
    
    /**
     * Creates a new data source using the given context. The context will
     * never be closed. The type mapping con be configured with a custom
     * type support.
     * 
     * @param jcaContext the context to be used
     * @param monitorMask Monitor.VALUE, ...
     * @param typeSupport type support to be used
     * @deprecated use {@link JCADataSourceBuilder}
     */
    @Deprecated
    public JCADataSource(Context jcaContext, int monitorMask, JCATypeSupport typeSupport) {
        this(jcaContext, monitorMask, typeSupport, false, isVarArraySupported(jcaContext));
    }
    
    /**
     * Creates a new data source using the given context. The context will
     * never be closed. The type mapping con be configured with a custom
     * type support.
     * 
     * @param jcaContext the context to be used
     * @param monitorMask Monitor.VALUE, ...
     * @param typeSupport type support to be used
     * @param dbePropertySupported whether metadata monitors should be used
     * @param varArraySupported true if var array should be used 
     * @deprecated use {@link JCADataSourceBuilder}
     */
    @Deprecated
    public JCADataSource(Context jcaContext, int monitorMask, JCATypeSupport typeSupport, boolean dbePropertySupported, boolean varArraySupported) {
        super(true);
        this.ctxt = jcaContext;
        this.monitorMask = monitorMask;
        this.typeSupport = typeSupport;
        this.dbePropertySupported = dbePropertySupported;
        this.varArraySupported = varArraySupported;
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
     * Determines whether the context supports variable arrays
     * or not.
     * 
     * @param context a JCA Context
     * @return true if supports variable sized arrays
     */
    @Deprecated
    public static boolean isVarArraySupported(Context context) {
        return JCADataSourceBuilder.isVarArraySupported(context);
    }
    
}
