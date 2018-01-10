/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
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
 * Configuration for {@link FileDataSource}. This object is mutable, and
 * therefore not thread-safe.
 *
 * @author carcassi
 */
public final class FileDataSourceConfiguration extends DataSourceConfiguration<FileDataSource> {

    // Package private so we don't need getters
    boolean pollEnabled = false;
    Duration pollInterval = Duration.ofSeconds(5);

    @Override
    public FileDataSourceConfiguration read(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String ver = xPath.evaluate("/file/@version", document);
            if (!ver.equals("1")) {
                throw new IllegalArgumentException("Unsupported version " + ver);
            }

            String intervalDuration = xPath.evaluate("/file/poll/@interval", document);
            if (intervalDuration != null && !intervalDuration.isEmpty()) {
                pollEnabled = true;
                pollInterval = Duration.ofSeconds(Long.parseLong(intervalDuration));
            }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(FileDataSourceConfiguration.class.getName()).log(Level.FINEST, "Couldn't load file configuration", ex);
            throw new IllegalArgumentException("Couldn't load file configuration", ex);
        }

        return this;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }

    public FileDataSourceConfiguration pollInterval(Duration pollInterval) {
        this.pollInterval = pollInterval;
        return this;
    }

    public boolean isPollEnabled() {
        return pollEnabled;
    }

    public FileDataSourceConfiguration pollEnabled(boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
        return this;
    }

    @Override
    public FileDataSource create() {
        return new FileDataSource(this);
    }

}
