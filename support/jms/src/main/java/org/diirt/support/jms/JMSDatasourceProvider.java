package org.diirt.support.jms;

import org.diirt.datasource.ConfigurableDataSourceProvider;

/**
 * DatasourceProvider for jms access
 *
 * @author Kunal Shroff
 *
 */
public class JMSDatasourceProvider extends ConfigurableDataSourceProvider<JMSDatasource, JMSDataSourceConfiguration> {

    protected JMSDatasourceProvider() {
        super(JMSDataSourceConfiguration.class);
    }

    @Override
    public String getName() {
        return "jms";
    }

}
