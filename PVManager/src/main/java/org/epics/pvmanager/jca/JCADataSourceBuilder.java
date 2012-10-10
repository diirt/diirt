/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.CAException;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.jni.JNIContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
public class JCADataSourceBuilder {
    private static final Logger log = Logger.getLogger(JCADataSource.class.getName());
    
    private Context jcaContext;
    private int monitorMask = Monitor.VALUE | Monitor.ALARM;
    private JCATypeSupport typeSupport;
    private boolean dbePropertySupported  = false;
    private Boolean varArraySupported;

    public JCADataSourceBuilder jcaContextClass(String className) {
        this.jcaContext = createContext(className);
        return this;
    }
    
    public JCADataSourceBuilder jcaContext(Context jcaContext) {
        this.jcaContext = jcaContext;
        return this;
    }
    
    public JCADataSourceBuilder monitorMask(int monitorMask) {
        this.monitorMask = monitorMask;
        return this;
    }
    
    public JCADataSourceBuilder typeSupport(JCATypeSupport typeSupport) {
        this.typeSupport = typeSupport;
        return this;
    }
    
    public JCADataSourceBuilder dbePropertySupported(boolean dbePropertySupported) {
        this.dbePropertySupported = dbePropertySupported;
        return this;
    }
    
    public JCADataSourceBuilder varArraySupported(boolean varArraySupported) {
        this.varArraySupported = varArraySupported;
        return this;
    }
    
    public JCADataSource build() {
        // Some properties are not pre-initialized to the default,
        // so if they were not set, we should initialize them.
        
        // Default JCA context is pure Java
        if (jcaContext == null) {
            jcaContext = createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
        }
        
        // Default type support are the VTypes
        if (typeSupport == null) {
            typeSupport = new JCATypeSupport(new JCAVTypeAdapterSet());
        }

        // Default support for var array needs to be detected
        if (varArraySupported == null) {
            varArraySupported = JCADataSource.isVarArraySupported(jcaContext);
        }
        
        return new JCADataSource(jcaContext, monitorMask, typeSupport, dbePropertySupported, varArraySupported);
    }
    
    /**
     * Determines whether the context supports variable arrays
     * or not.
     * 
     * @param context a JCA Context
     * @return true if supports variable sized arrays
     */
    static boolean isVarArraySupported(Context context) {
        try {
            Class cajClazz = Class.forName("com.cosylab.epics.caj.CAJContext");
            if (cajClazz.isInstance(context)) {
                return !(context.getVersion().getMajorVersion() <= 1 && context.getVersion().getMinorVersion() <= 1 && context.getVersion().getMaintenanceVersion() <=9);
            }
        } catch (ClassNotFoundException ex) {
            // Can't be CAJ, fall back to JCA
        }
        
        if (context instanceof JNIContext) {
            try {
                Class<?> jniClazz = Class.forName("gov.aps.jca.jni.JNI");
                final Method method = jniClazz.getDeclaredMethod("_ca_getRevision", new Class<?>[0]);
                // The field is actually private, so we need to make it accessible
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    @Override
                    public Object run() {
                        method.setAccessible(true);
                        return null;
                    }
                    
                });
                Integer integer = (Integer) method.invoke(null, new Object[0]);
                return (integer >= 13);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Cannot detect: no CAJContext or JNI classes can be loaded.", ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("Cannot detect: no CAJContext or JNI._ca_getRevision found.", ex);
            } catch (SecurityException ex) {
                throw new RuntimeException("Cannot detect: no CAJContext and no permission to access JNI._ca_getRevision.", ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Cannot detect: no CAJContext and cannot invoke JNI._ca_getRevision.", ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Cannot detect: no CAJContext and cannot invoke JNI._ca_getRevision.", ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException("Cannot detect: no CAJContext and cannot invoke JNI._ca_getRevision.", ex);
            }
            
        }
        
        throw new RuntimeException("Couldn't detect");
    }
    
    static Context createContext(String className) {
        try {
            JCALibrary jca = JCALibrary.getInstance();
            return jca.createContext(className);
        } catch (CAException ex) {
            log.log(Level.SEVERE, "JCA context creation failed", ex);
            throw new RuntimeException("JCA context creation failed", ex);
        }
    }    
}
