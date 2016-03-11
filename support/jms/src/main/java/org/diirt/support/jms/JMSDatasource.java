/**
 *
 */
package org.diirt.support.jms;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.diirt.datasource.ChannelHandler;
import org.diirt.datasource.ChannelReadRecipe;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.ReadRecipe;
import org.diirt.datasource.util.FunctionParser;
import org.diirt.datasource.vtype.DataTypeSupport;

/**
 * @author Kunal Shroff
 *
 */
public class JMSDatasource extends DataSource {

    private static final Logger log = Logger.getLogger(JMSDatasource.class.getName());

    private final JMSDataSourceConfiguration configuration;

    private Connection connection;
    private Session session;

    static {
        // Install type support for the types it generates.
        DataTypeSupport.install();
    }

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
        /**
         * Parse the name to support defining the read and write types / the
         * sytax is as follows
         *
         * jms://topic_name<readType, writeType>{filter}
         **/
        List<Object> parsedTokens = parseName(channelName);
        return new JMSChannelHandler(parsedTokens.get(0).toString(), this);
    }

    @Override
    public void connectRead(ReadRecipe readRecipe) {
        super.connectRead(readRecipe);

        // Initialize all values
        for (ChannelReadRecipe channelReadRecipe : readRecipe.getChannelReadRecipes()) {
            initialize(channelReadRecipe.getChannelName());
        }
    }


    private void initialize(String channelName) {
        System.out.println("initilaizing :"+channelName);
        List<Object> parsedTokens = parseName(channelName);

        JMSChannelHandler channel = (JMSChannelHandler) getChannels().get(channelHandlerLookupName(channelName));
        if (parsedTokens.size() > 2) {
            if (channel != null) {
                System.out.println("setting selection" + parsedTokens.get(1).toString());
                channel.setSelectors(parsedTokens.get(1).toString());
            }
        }
    }

    private List<Object> parseName(String channelName) {
        List<Object> tokens = FunctionParser.parseFunctionAnyParameter(".+", channelName);
        String nameAndTypeAndFilter = tokens.get(0).toString();
        String name = nameAndTypeAndFilter;
        String filter = null;
        String type = null;
        int index = nameAndTypeAndFilter.lastIndexOf('{');
        if (nameAndTypeAndFilter.endsWith("}") && index != -1) {
            name = nameAndTypeAndFilter.substring(0, index);
            filter = nameAndTypeAndFilter.substring(index + 1, nameAndTypeAndFilter.length() - 1);
        }
        index = nameAndTypeAndFilter.lastIndexOf('<');
        if (nameAndTypeAndFilter.endsWith(">") && index != -1) {
            name = nameAndTypeAndFilter.substring(0, index);
            type = nameAndTypeAndFilter.substring(index + 1, nameAndTypeAndFilter.length() - 1);
        }
        List<Object> newTokens = new ArrayList<>();
        newTokens.add(name);
        newTokens.add(filter);
        newTokens.add(type);
        return newTokens;
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
