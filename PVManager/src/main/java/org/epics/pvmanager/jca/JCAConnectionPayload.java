/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.Channel;

/**
 * Represents the connection payload, which consists of the actual JCA
 * Channel and the JCADataSource (which can be used to extract
 * configuration parameters).
 *
 * @author carcassi
 */
public class JCAConnectionPayload {
    private JCADataSource jcaDataSource;
    private Channel channel;

    public JCAConnectionPayload(JCADataSource jcaDataSource, Channel channel) {
        this.jcaDataSource = jcaDataSource;
        this.channel = channel;
    }

    /**
     * The JCADataSource that is using the channel.
     * 
     * @return the JCA data source
     */
    public JCADataSource getJcaDataSource() {
        return jcaDataSource;
    }

    /**
     * The JCA channel.
     * 
     * @return JCA channel
     */
    public Channel getChannel() {
        return channel;
    }
    
}
