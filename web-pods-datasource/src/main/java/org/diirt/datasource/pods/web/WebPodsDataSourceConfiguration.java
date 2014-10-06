/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.diirt.pods.common.ChannelTranslation;
import org.diirt.pods.common.ChannelTranslator;
import static org.diirt.pods.common.ChannelTranslator.compositeTranslator;
import static org.diirt.pods.common.ChannelTranslator.regexTranslator;

import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.vtype.DataTypeSupport;
import org.epics.util.config.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Configuration for {@link WebPodsDataSource}.
 *
 * @author carcassi
 */
public final class WebPodsDataSourceConfiguration {
    
    private final URI socketLocation;

    private WebPodsDataSourceConfiguration() {
        this.socketLocation = null;
    }
    
    public static WebPodsDataSourceConfiguration readConfiguration(String confPath) {
        try (InputStream input = Configuration.getFileAsStream(confPath + "/wp.xml", new WebPodsDataSourceConfiguration(), "wp.default.xml")) {
            WebPodsDataSourceConfiguration conf = new WebPodsDataSourceConfiguration(input);
            return conf;
        } catch (Exception ex) {
            Logger.getLogger(WebPodsDataSourceFactory.class.getName()).log(Level.SEVERE, "Couldn't load DIIRT_HOME/" + confPath + "/wp.xml", ex);
            return null;
        }
    }

    public WebPodsDataSourceConfiguration(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);
            
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();
            
            String ver = xPath.evaluate("/wp/@version", document);
            if (!ver.equals("1")) {
                throw new IllegalArgumentException("Unsupported version " + ver);
            }
            
            String socketLocationString = xPath.evaluate("/wp/connection/@socketLocation", document);
            socketLocation = URI.create(socketLocationString);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(ChannelTranslator.class.getName()).log(Level.FINEST, "Couldn't load wp configuration", ex);
            throw new IllegalArgumentException("Couldn't load wp configuration", ex);
        }
    }

    public URI getSocketLocation() {
        return socketLocation;
    }
    
}
