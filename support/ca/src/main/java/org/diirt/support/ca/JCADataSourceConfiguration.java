/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.CAException;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.configuration.Configurable;
import gov.aps.jca.configuration.ConfigurationException;
import gov.aps.jca.configuration.DefaultConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.diirt.datasource.DataSourceConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.cosylab.epics.caj.CAJContext;

/**
 * Configuration for {@link JCADataSource}. This object is mutable and not
 * thread-safe.
 *
 * @author carcassi
 */
public final class JCADataSourceConfiguration extends DataSourceConfiguration<JCADataSource> {
    private static final Logger log = Logger.getLogger(JCADataSource.class.getName());

    // Package private so we don't need getters
    Context jcaContext;
    int monitorMask = Monitor.VALUE | Monitor.ALARM;
    JCATypeSupport typeSupport;
    boolean dbePropertySupported = false;
    Boolean varArraySupported;
    boolean rtypValueOnly = false;
    boolean honorZeroPrecision = true;
    String jcaContextClass = null;

    Map<String, String> jcaContextProperties = new HashMap<>();

    @Override
    public JCADataSourceConfiguration read(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String ver = xPath.evaluate("/ca/@version", document);
            if (!ver.equals("1")) {
                throw new IllegalArgumentException("Unsupported version " + ver);
            }

            String monitorMask = xPath.evaluate("/ca/dataSourceOptions/@monitorMask", document);
            if (monitorMask != null && !monitorMask.isEmpty()) {
                switch (monitorMask.toUpperCase()) {
                case "VALUE":
                    monitorMask(Monitor.VALUE | Monitor.ALARM);
                    break;
                case "ARCHIVE":
                    monitorMask(Monitor.LOG);
                    break;
                case "ALARM":
                    monitorMask(Monitor.ALARM);
                    break;
                default:
                    monitorMask(Integer.parseInt(monitorMask));
                }
            }

            String dbePropertySupported = xPath.evaluate("/ca/dataSourceOptions/@dbePropertySupported", document);
            if (dbePropertySupported != null && !dbePropertySupported.isEmpty()) {
                switch (dbePropertySupported.toUpperCase()) {
                case "TRUE":
                    dbePropertySupported(true);
                    break;
                default:
                    dbePropertySupported(false);
                }
            }

            String honorZeroPrecision = xPath.evaluate("/ca/dataSourceOptions/@honorZeroPrecision", document);
            if (honorZeroPrecision != null && !honorZeroPrecision.isEmpty()) {
                switch (honorZeroPrecision.toUpperCase()) {
                case "TRUE":
                    honorZeroPrecision(true);
                    break;
                default:
                    honorZeroPrecision(false);
                }
            }

            String rtypValueOnly = xPath.evaluate("/ca/dataSourceOptions/@rtypValueOnly", document);
            if (rtypValueOnly != null && !rtypValueOnly.isEmpty()) {
                switch (rtypValueOnly.toUpperCase()) {
                case "TRUE":
                    rtypValueOnly(true);
                    break;
                default:
                    rtypValueOnly(false);
                }
            }

            String varArraySupported = xPath.evaluate("/ca/dataSourceOptions/@varArraySupported", document);
            if (varArraySupported != null && !varArraySupported.isEmpty()) {
                switch (varArraySupported.toUpperCase()) {
                case "AUTO":
                    // Do nothing
                    break;
                case "TRUE":
                    varArraySupported(true);
                    break;
                default:
                    varArraySupported(false);
                }
            }

            String pureJava = xPath.evaluate("/ca/jcaContext/@pureJava", document);
            if (pureJava != null && !pureJava.isEmpty()) {
                switch (pureJava.toUpperCase()) {
                case "TRUE":
                    jcaContextClass(JCALibrary.CHANNEL_ACCESS_JAVA);
                    break;
                default:
                    jcaContextClass(JCALibrary.JNI_THREAD_SAFE);
                }
            }

            Node jcaContext = (Node) xPath.evaluate("/ca/jcaContext", document, XPathConstants.NODE);
            if (jcaContext != null) {
                NamedNodeMap attributes = jcaContext.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attribute = attributes.item(i);
                    if (!"pureJava".equals(attribute.getNodeName())) {
                        addContextProperty(attribute.getNodeName(), attribute.getNodeValue());
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(JCADataSourceConfiguration.class.getName()).log(Level.FINEST,
                    "Couldn't load file configuration", ex);
            throw new IllegalArgumentException("Couldn't load file configuration", ex);
        }
        return this;
    }

    /**
     * The class name for the implementation of JCA.
     * <p>
     * Default is {@link JCALibrary#CHANNEL_ACCESS_JAVA}.
     *
     * @param className
     *            the class name of the jca implementation
     * @return this
     */
    public JCADataSourceConfiguration jcaContextClass(String className) {
        if (jcaContext != null || jcaContextClass != null) {
            throw new IllegalStateException("You should call either jcaContextClass or jcaContext once.");
        }
        this.jcaContextClass = className;
        return this;
    }

    /**
     * The jca context to use. This allows complete customization of the jca
     * context.
     * <p>
     * By default, will be automatically created from the
     * {@link #jcaContextClass(java.lang.String) }.
     *
     * @param jcaContext
     *            the context
     * @return this
     */
    public JCADataSourceConfiguration jcaContext(Context jcaContext) {
        if (jcaContext != null || jcaContextClass != null) {
            throw new IllegalStateException("You should call once either jcaContextClass or jcaContext.");
        }
        this.jcaContext = jcaContext;
        return this;
    }

    /**
     * The mask used for the monitor notifications. This should be a combination
     * of {@link Monitor#VALUE}, {@link Monitor#ALARM}, ...
     * <p>
     * Default is {@code Monitor.VALUE | Monitor.ALARM }.
     *
     * @param monitorMask
     *            the monitor mask
     * @return this
     */
    public JCADataSourceConfiguration monitorMask(int monitorMask) {
        this.monitorMask = monitorMask;
        return this;
    }

    /**
     * Changes the way JCA DBR types are mapped to types supported in pvamanger.
     * <p>
     * Default includes support for the VTypes (i.e. {@link JCAVTypeAdapterSet}
     * ).
     *
     * @param typeSupport
     *            the custom type support
     * @return this
     */
    public JCADataSourceConfiguration typeSupport(JCATypeSupport typeSupport) {
        this.typeSupport = typeSupport;
        return this;
    }

    /**
     * Whether a separate monitor should be used for listening to metadata
     * changes.
     * <p>
     * Default is false.
     *
     * @param dbePropertySupported
     *            if true, metadata changes will trigger notification
     * @return this
     */
    public JCADataSourceConfiguration dbePropertySupported(boolean dbePropertySupported) {
        this.dbePropertySupported = dbePropertySupported;
        return this;
    }

    /**
     * If true, monitors will setup using "0" length, which will make the server
     * a variable length array in return (if supported) or a "0" length array
     * (if not supported). Variable array support was added to EPICS 3.14.12.2
     * and to CAJ 1.1.10.
     * <p>
     * By default it tries to auto-detected whether the client library
     * implements the proper checks.
     *
     * @param varArraySupported
     *            true will enable
     * @return this
     */
    public JCADataSourceConfiguration varArraySupported(boolean varArraySupported) {
        this.varArraySupported = varArraySupported;
        return this;
    }

    /**
     * If true, for fields that match ".*\.RTYP.*" only the value will be read;
     * alarm and time will be created at client side. Version of EPICS before
     * 3.14.11 do not send correct data (would send only the value), which would
     * make the client behave incorrectly.
     * <p>
     * Default is false.
     *
     * @param rtypValueOnly
     *            true will enable
     * @return this
     */
    public JCADataSourceConfiguration rtypValueOnly(boolean rtypValueOnly) {
        this.rtypValueOnly = rtypValueOnly;
        return this;
    }

    /**
     * If true, the formatter returned by the VType will show no decimal digits
     * (assumes it was configured); if false, it will return all the digit
     * (assumes it wasn't configured).
     * <p>
     * Default is true.
     *
     * @param honorZeroPrecision
     *            whether the formatter should treat 0 precision as meaningful
     * @return this
     */
    public JCADataSourceConfiguration honorZeroPrecision(boolean honorZeroPrecision) {
        this.honorZeroPrecision = honorZeroPrecision;
        return this;
    }

    /**
     * Adds a new property that is passed directly to the JCALibrary when
     * creating the context.
     *
     * @param name
     *            the name of the property
     * @param value
     *            the value of the property
     * @return this
     */
    public JCADataSourceConfiguration addContextProperty(String name, String value) {
        this.jcaContextProperties.put(name, value);
        return this;
    }

    /**
     * Determines whether the context supports variable arrays or not.
     *
     * @param context
     *            a JCA Context
     * @return true if supports variable sized arrays
     */
    static boolean isVarArraySupported(Context context) {
        try {
            Class cajClazz = Class.forName("com.cosylab.epics.caj.CAJContext");
            if (cajClazz.isInstance(context)) {
                return !(context.getVersion().getMajorVersion() <= 1 && context.getVersion().getMinorVersion() <= 1
                        && context.getVersion().getMaintenanceVersion() <= 9);
            }
        } catch (ClassNotFoundException ex) {
            // Can't be CAJ, fall back to JCA
        }

        return true;
    }

    /**
     * Creates a context from the configuration information.
     *
     * @param className
     *            the class name
     * @return a new context
     */
    Context createContext() {
        if (jcaContext != null) {
            return jcaContext;
        }

        if (jcaContextClass == null) {
            jcaContextClass = JCALibrary.CHANNEL_ACCESS_JAVA;
        }

        try {
            DefaultConfiguration conf = new DefaultConfiguration("CONTEXT");
            conf.setAttribute("class", jcaContextClass);
            jcaContextProperties.entrySet().stream().forEach((entry) -> {
                conf.setAttribute(entry.getKey(), entry.getValue());
            });
            if (conf.getAttribute("class").equals(JCALibrary.CHANNEL_ACCESS_JAVA)) {
                Context cajContext = new CAJContext();
                ((Configurable) cajContext).configure(conf);
                return cajContext;
            } else {
                JCALibrary jca = JCALibrary.getInstance();
                return jca.createContext(conf);
            }
        } catch (ConfigurationException | CAException ex) {
            log.log(Level.SEVERE, "JCA context creation failed", ex);
            throw new RuntimeException("JCA context creation failed", ex);
        }
    }

    /**
     * Creates a new JCADataSource with the current configuration.
     *
     * @return a new data source
     */
    @Override
    public JCADataSource create() {
        return new JCADataSource(this);
    }
}
