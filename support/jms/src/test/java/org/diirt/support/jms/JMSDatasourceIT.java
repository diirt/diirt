/**
 *
 */
package org.diirt.support.jms;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.util.time.TimeDuration.ofHertz;
import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.newVString;
import static org.diirt.vtype.ValueFactory.timeNow;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import org.junit.AfterClass;
import org.junit.Assert;
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
     * A basic test to send a jms message and check if it was received by the
     * diirt jms datasource
     */
    @Test
    public void topicSubscribe() {
        final JMSDatasource jms = new JMSDatasourceProvider().createInstance();
        PVManager.setDefaultDataSource(jms);

        final PVReader<Object> pv = PVManager.read(channel(topic)).from(jms).readListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
                log.info("reading jms message: " + event.getPvReader().getValue());
            }
        }).maxRate(ofHertz(100));

        try {
            writeTextMessage(topic, "topicSubscribe", 10);
        } catch (Exception e) {

        }
//        pv.close();
    }

    /**
     * A basic test to write to a jms topic using the diirt jms datasource
     */
    @Test
    public void topicPublish(){
        final JMSDatasource jms = new JMSDatasourceProvider().createInstance();
        PVManager.setDefaultDataSource(jms);

        final PV<Object, Object> pv = PVManager.readAndWrite(channel(topic))
                .writeListener(new PVWriterListener<Object>() {
                    @Override
                    public void pvChanged(PVWriterEvent<Object> event) {
                        log.info("writing completed: " + event.isWriteSucceeded());
                    }
                })
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        if(event.isValueChanged()){
                            log.info("reading written message: " + event.getPvReader().getValue());
                        }
                    }
                }).asynchWriteAndMaxReadRate(ofHertz(100));

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pv.write(newVString("Hello", alarmNone(), timeNow()));
        }
        pv.close();
    }

    /**
     *
     */
    @Test
    public void topicMultipleSubscribers(){
        final JMSDatasource jms = new JMSDatasourceProvider().createInstance();
        PVManager.setDefaultDataSource(jms);

        List<String> eventList1 = new ArrayList<String>();
        List<String> eventList2 = new ArrayList<String>();

        final PVReader<?> pv1 = PVManager.read(channel(topic)).readListener((event) -> {
            eventList1.add(event.getPvReader().getValue().toString());
            log.info("pv1 event" + event.getPvReader().getValue());
        }).maxRate(ofHertz(100));

        final PVReader<?> pv2 = PVManager.read(channel(topic)).readListener((event) -> {
            eventList2.add(event.getPvReader().getValue().toString());
            log.info("pv2 event" + event.getPvReader().getValue());
        }).maxRate(ofHertz(100));

        try {
           writeTextMessage(topic, "topicMultipleSubscribers", 10);
        } catch (Exception e) {

        } finally {
            pv1.close();
            pv2.close();
        }

        Assert.assertSame("events reveived by both pvs were not the same", eventList1, eventList2);

    }

    /**
     * Parse the name to support defining the read and write types / the
     * sytax is as follows
     *
     * jms://topic_name<readType, writeType>{filter}
     **/
    @Test
    public void topicSubscribersWithFilter(){
        final JMSDatasource jms = new JMSDatasourceProvider().createInstance();
        PVManager.setDefaultDataSource(jms);

        List<String> eventList1 = new ArrayList<String>();

        final PVReader<?> pv1 = PVManager.read(channel(topic + "{property = 'ID1'}")).readListener((event) -> {
            if (event.isValueChanged()) {
                eventList1.add(event.getPvReader().getValue().toString());
                log.info("pv1 event" + event.getPvReader().getValue());
            }
        }).maxRate(ofHertz(100));

        try {
           writeTextMessageWithProperty(topic, "ID1", "ID1", 10);
           writeTextMessageWithProperty(topic, "ID2", "ID2", 10);
        } catch (Exception e) {

        } finally {
            pv1.close();
        }

        Assert.assertSame("events reveived by both pvs were not the same", eventList1.size(), 10);

    }

    /**
     * A helper method used by tests to write plain text message/s to a topic
     *
     * @param topic The topic to write the message to
     * @param count The number of times the message should be written
     * @throws JMSException
     * @throws InterruptedException
     */
    private static void writeTextMessage(String topic, String textID, int count) throws JMSException, InterruptedException {
        writeTextMessageWithProperty(topic, null, textID, count);
    }

    /**
     * A helper method used by tests to write plain text message/s with a property to a topic
     *
     * @param topic The topic to write the message to
     * @param count The number of times the message should be written
     * @throws JMSException
     * @throws InterruptedException
     */
    private static void writeTextMessageWithProperty(String topic, String Property, String textID, int count) throws JMSException, InterruptedException {
        // Create the destination (Topic or Queue)
        Destination destination = session.createTopic(topic);

        // Create a MessageProducer from the Session to the Topic or
        // Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a messages
        //String text = "SimpleTopicProducer - From: " + Thread.currentThread().getName() + " : " + textID;
        String text = textID;
        TextMessage message = session.createTextMessage(text);
        if (Property != null) {
            message.setStringProperty("property", Property);
        }
        for (int i = 0; i < count; i++) {

            Thread.sleep(100);
            // Tell the producer to send the message
            log.fine("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);
        }
    }
}
