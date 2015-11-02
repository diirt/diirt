/**
 * 
 */
package org.diirt.support.jms;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.DataSource;

/**
 * @author Kunal Shroff
 *
 */
public class JMSDatasource extends DataSource {

    private static final Logger log = Logger.getLogger(JMSDatasource.class.getName());

    private final JMSDataSourceConfiguration configuration;

    private Connection connection;
    private Session session;

    public JMSDatasource(JMSDataSourceConfiguration configuration) {
        super(true);
        this.configuration = configuration;

        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(configuration.getBrokerUrl());

        // Create a Connection
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected ChannelHandler createChannel(String channelName) {
        return new JMSChannelHandler(channelName, this);
    }

    @Override
    public void close() {
        super.close();
        System.out.println("closing");
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }

}
