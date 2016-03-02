/**
 *
 */
package org.diirt.support.jms;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.diirt.datasource.DataSourceConfiguration;
import org.w3c.dom.Document;

/**
 * Configuration for {@link JMSDatasource}
 *
 * @author Kunal Shroff
 *
 */
public class JMSDataSourceConfiguration extends DataSourceConfiguration<JMSDatasource> {

    private String brokerUrl = "tcp://localhost:61616?jms.prefetchPolicy.all=1000";

    @Override
    public JMSDataSourceConfiguration read(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String ver = xPath.evaluate("/jms/@version", document);
            if (!ver.equals("1")) {
                throw new IllegalArgumentException("Unsupported version " + ver);
            }

            String monitorMask = xPath.evaluate("/jms/dataSourceOptions/@brokerURL", document);
            if (monitorMask != null && !monitorMask.isEmpty()) {
                this.brokerUrl = monitorMask;
            }else{
                Logger.getLogger(JMSDataSourceConfiguration.class.getName()).log(Level.FINEST, "Couldn't load brokerURL from jms file configuration");
            }
        } catch (Exception e) {
            Logger.getLogger(JMSDataSourceConfiguration.class.getName()).log(Level.FINEST, "Couldn't load file configuration", e);
            throw new IllegalArgumentException("Couldn't load file configuration", e);
        }
        return this;
    }

    @Override
    public JMSDatasource create() {
        return new JMSDatasource(this);
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

}
