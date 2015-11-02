/**
 * 
 */
package org.diirt.support.jms;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.util.time.TimeDuration.ofHertz;

import java.net.URL;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Kunal Shroff
 *
 */
public class JMSDatasourceIT {

    private static final Logger log = Logger.getLogger(JMSDatasourceIT.class.getName());
    private static final String BROKER_URL = "tcp://localhost:61616?jms.prefetchPolicy.all=1000";

    private static final String topic = "test_topic";

    private static Connection connection;
    private static Session session;

    @BeforeClass
    public static void initialize() {
        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

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

    @AfterClass
    public static void cleanup() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * A basic test to send a jms message and check if it was recieved by the
     * diirt jms datasource
     */
    @Test
    public void topicPublishSubscribe() {
        final JMSDatasource jms = new JMSDatasourceProvider().createInstance();
        PVManager.setDefaultDataSource(jms);

        final PVReader<?> pv = PVManager.read(channel(topic)).readListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
                // TODO Auto-generated method stub
                log.fine("pv event" + event.toString());
            }
        }).maxRate(ofHertz(100));

        try {
            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(topic);

            // Create a MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            String text = "SimpleTopicProducer - From: " + Thread.currentThread().getName() + " : " + this.hashCode();
            TextMessage message = session.createTextMessage(text);
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                // Tell the producer to send the message
                log.fine("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);
            }

        } catch (Exception e) {

        }
    }
}
