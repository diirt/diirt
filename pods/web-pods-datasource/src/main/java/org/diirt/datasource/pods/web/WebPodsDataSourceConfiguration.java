/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.diirt.datasource.DataSourceConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Configuration for {@link WebPodsDataSource}. This object is mutable, and
 * therefore not thread-safe.
 *
 * @author carcassi
 */
public final class WebPodsDataSourceConfiguration extends DataSourceConfiguration<WebPodsDataSource> {

    // Package private so we don't need getters
    URI socketLocation;

    public WebPodsDataSourceConfiguration() {
        this.socketLocation = null;
    }

    @Override
    public DataSourceConfiguration<WebPodsDataSource> read(InputStream input) {
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
            Logger.getLogger(WebPodsDataSourceConfiguration.class.getName()).log(Level.FINEST, "Couldn't load wp configuration", ex);
            throw new IllegalArgumentException("Couldn't load wp configuration", ex);
        }
        return this;
    }

    public URI getSocketLocation() {
        return socketLocation;
    }

    public WebPodsDataSourceConfiguration socketLocation(URI socketLocation) {
        this.socketLocation = socketLocation;
        return this;
    }

    @Override
    public WebPodsDataSource create() {
        return new WebPodsDataSource(this);
    }

}
