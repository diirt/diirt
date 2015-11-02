/**
 * 
 */
package org.diirt.support.jms;

import java.util.logging.Logger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import org.diirt.datasource.ChannelWriteCallback;
import org.diirt.datasource.MultiplexedChannelHandler;
import org.diirt.util.time.Timestamp;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.Time;
import org.diirt.vtype.VString;
import org.diirt.vtype.ValueFactory;

/**
 * @author Kunal Shroff
 *
 */
public class JMSChannelHandler extends MultiplexedChannelHandler<JMSConnectionPayload, Object> {

    private static final Logger log = Logger.getLogger(JMSChannelHandler.class.getName());

    private JMSDatasource jmsDatasource;
    private MessageConsumer consumer;

    public JMSChannelHandler(String channelName, JMSDatasource jmsDatasource) {
        super(channelName);
        this.jmsDatasource = jmsDatasource;
    }

    @Override
    protected void connect() {

        try {
            Destination destination = jmsDatasource.getSession().createTopic(getChannelName());
            // Create a MessageConsumer from the Session to the Topic or
            // Queue
            consumer = jmsDatasource.getSession().createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    // TODO Auto-generated method stub
                    log.fine("message event: " + message.toString());
                    Object newValue;
                    try {
                        log.fine("creating new values");
                        Alarm alarm = ValueFactory.alarmNone();
                        Time time = ValueFactory.timeNow();
                        newValue = ValueFactory.newVString(message.toString(), ValueFactory.alarmNone(),
                                ValueFactory.timeNow());
                        log.info("new Value: " + newValue);

                        processMessage(newValue);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // processMessage(new JMSMessagePayload(message));
                }
            });
        } catch (JMSException e) {
            reportExceptionToAllReadersAndWriters(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void disconnect() {
        try {
            System.out.println("channel close");
            consumer.close();
        } catch (JMSException e) {
            reportExceptionToAllReadersAndWriters(e);
            // TODO cleanup
            e.printStackTrace();
        }
    }

    @Override
    protected void write(Object newValue, ChannelWriteCallback callback) {
        // TODO Auto-generated method stub

    }

}
