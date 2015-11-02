/**
 * 
 */
package org.diirt.support.jms;

import java.io.InputStream;

import javax.jms.JMSException;

import org.diirt.datasource.DataSourceConfiguration;

/**
 * Configuration for {@link JMSDatasource}
 * 
 * @author Kunal Shroff
 *
 */
public class JMSDataSourceConfiguration extends DataSourceConfiguration<JMSDatasource> {

    private static final String BROKER_URL = "tcp://localhost:61616?jms.prefetchPolicy.all=1000";

    @Override
    public DataSourceConfiguration read(InputStream stream) {
        // TODO read a configuration file
        return new JMSDataSourceConfiguration();
    }

    @Override
    public JMSDatasource create() {
        return new JMSDatasource(this);
    }

    public String getBrokerUrl() {
        return BROKER_URL;
    }

}
