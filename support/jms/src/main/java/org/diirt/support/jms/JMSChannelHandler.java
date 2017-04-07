/**
 *
 */
package org.diirt.support.jms;

import java.util.logging.Logger;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.MultiplexedChannelHandler;

import static org.diirt.vtype.ValueFactory.*;
/**
 * @author Kunal Shroff
 *
 */
public class JMSChannelHandler extends MultiplexedChannelHandler<Object, Object> {

    private static final Logger log = Logger.getLogger(JMSChannelHandler.class.getName());

    private JMSDatasource jmsDatasource;
    private MessageConsumer consumer;

    private String selector;
    private String readType;
    private String writeType;

    public JMSChannelHandler(String channelName, JMSDatasource jmsDatasource) {
        super(channelName);
        this.jmsDatasource = jmsDatasource;
    }

    public void setSelectors(String selector) {
        this.selector = selector;
    }

    @Override
    protected void connect() {
System.out.println("connecting "+selector);
        try {
            Destination destination = jmsDatasource.getSession().createTopic(getChannelName());
            // Create a MessageConsumer from the Session to the Topic or
            // Queue
            if (selector != null && !selector.isEmpty()) {
                consumer = jmsDatasource.getSession().createConsumer(destination, selector);
            } else {
                consumer = jmsDatasource.getSession().createConsumer(destination);
            }
            consumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    log.info("message event: " + message.toString());
                    Object newValue;
                    try {
                        log.fine("creating new values");
                        newValue = newVString(message.toString(), alarmNone(), timeNow());
                        log.fine("new Value: " + newValue);
                        processMessage(newValue);
                    } catch (Exception e) {
                        reportExceptionToAllReadersAndWriters(e);
                    }
                    // processMessage(new JMSMessagePayload(message));
                }
            });
        } catch (JMSException e) {
            reportExceptionToAllReadersAndWriters(e);
            e.printStackTrace();
        }
        processConnection(new Object());
    }

    @Override
    protected void disconnect() {
        try {
            System.out.println("channel close");
            consumer.close();
            processConnection(null);
        } catch (JMSException e) {
            reportExceptionToAllReadersAndWriters(e);
            // TODO cleanup
            e.printStackTrace();
        }
    }

    @Override
    protected void write(Object newValue, ChannelWriteCallback callback) {
        try {
            Destination destination = jmsDatasource.getSession().createTopic(getChannelName());
            // Create a MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = jmsDatasource.getSession().createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // Create a messages
            String text = newValue.toString();
            TextMessage message = jmsDatasource.getSession().createTextMessage(text);
            // Tell the producer to send the message
            System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);
            callback.channelWritten(null);
        } catch (JMSException e) {
            reportExceptionToAllReadersAndWriters(e);
            callback.channelWritten(e);
            e.printStackTrace();
        }

    }


}
