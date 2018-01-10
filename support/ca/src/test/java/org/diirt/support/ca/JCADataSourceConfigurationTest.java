/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import com.cosylab.epics.caj.CAJContext;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class JCADataSourceConfigurationTest {

    @Test
    public void readDefaultConfiguration() {
        JCADataSourceConfiguration conf = new JCADataSourceConfiguration().read(this.getClass().getResourceAsStream("ca.default.xml"));
        assertThat(conf.monitorMask, equalTo(5));
        assertThat(conf.dbePropertySupported, equalTo(false));
        assertThat(conf.honorZeroPrecision, equalTo(true));
        assertThat(conf.rtypValueOnly, equalTo(false));
        assertThat(conf.varArraySupported, nullValue());
        assertThat(conf.jcaContextClass, nullValue());
        assertThat(conf.jcaContextProperties.isEmpty(), equalTo(true));

        Context context = conf.createContext();
        assertThat(context, instanceOf(CAJContext.class));
        CAJContext cajContext = (CAJContext) context;
        assertThat(cajContext.getAddressList(), equalTo(""));
        assertThat(cajContext.isAutoAddressList(), equalTo(true));
        assertThat(cajContext.getConnectionTimeout(), equalTo(30.0F));
        assertThat(cajContext.getBeaconPeriod(), equalTo(15.0F));
        assertThat(cajContext.getRepeaterPort(), equalTo(5065));
        assertThat(cajContext.getServerPort(), equalTo(5064));
        assertThat(cajContext.getMaxArrayBytes(), equalTo(16384));
        context.dispose();
    }

    @Test
    public void readConfiguration1() {
        JCADataSourceConfiguration conf = new JCADataSourceConfiguration().read(this.getClass().getResourceAsStream("ca1.xml"));
        assertThat(conf.monitorMask, equalTo(4));
        assertThat(conf.dbePropertySupported, equalTo(true));
        assertThat(conf.honorZeroPrecision, equalTo(false));
        assertThat(conf.rtypValueOnly, equalTo(true));
        assertThat(conf.varArraySupported, equalTo(false));
        assertThat(conf.jcaContextClass, equalTo(JCALibrary.CHANNEL_ACCESS_JAVA));
        assertThat(conf.jcaContextProperties.size(), equalTo(7));
        assertThat(conf.jcaContextProperties, hasEntry("addr_list", "192.168.1.0"));
        assertThat(conf.jcaContextProperties, hasEntry("auto_addr_list", "false"));
        assertThat(conf.jcaContextProperties, hasEntry("connection_timeout", "60.0"));
        assertThat(conf.jcaContextProperties, hasEntry("beacon_period", "30.0"));
        assertThat(conf.jcaContextProperties, hasEntry("repeater_port", "6065"));
        assertThat(conf.jcaContextProperties, hasEntry("server_port", "6064"));
        assertThat(conf.jcaContextProperties, hasEntry("max_array_bytes", "163840"));

        Context context = conf.createContext();
        assertThat(context, instanceOf(CAJContext.class));
        CAJContext cajContext = (CAJContext) context;
        assertThat(cajContext.getAddressList(), equalTo("192.168.1.0"));
        assertThat(cajContext.isAutoAddressList(), equalTo(false));
        assertThat(cajContext.getConnectionTimeout(), equalTo(60.0F));
        assertThat(cajContext.getBeaconPeriod(), equalTo(30.0F));
        assertThat(cajContext.getRepeaterPort(), equalTo(6065));
        assertThat(cajContext.getServerPort(), equalTo(6064));
        assertThat(cajContext.getMaxArrayBytes(), equalTo(163840));
        context.dispose();
    }

    @Test
    public void readConfiguration2() {
        JCADataSourceConfiguration conf = new JCADataSourceConfiguration().read(this.getClass().getResourceAsStream("ca2.xml"));
        assertThat(conf.monitorMask, equalTo(5));
        assertThat(conf.dbePropertySupported, equalTo(false));
        assertThat(conf.honorZeroPrecision, equalTo(true));
        assertThat(conf.rtypValueOnly, equalTo(false));
        assertThat(conf.varArraySupported, nullValue());
        assertThat(conf.jcaContextClass, equalTo(JCALibrary.CHANNEL_ACCESS_JAVA));
        assertThat(conf.jcaContextProperties.size(), equalTo(7));
        assertThat(conf.jcaContextProperties, hasEntry("addr_list", ""));
        assertThat(conf.jcaContextProperties, hasEntry("auto_addr_list", "true"));
        assertThat(conf.jcaContextProperties, hasEntry("connection_timeout", "30.0"));
        assertThat(conf.jcaContextProperties, hasEntry("beacon_period", "15.0"));
        assertThat(conf.jcaContextProperties, hasEntry("repeater_port", "5065"));
        assertThat(conf.jcaContextProperties, hasEntry("server_port", "5064"));
        assertThat(conf.jcaContextProperties, hasEntry("max_array_bytes", "16384"));

        Context context = conf.createContext();
        assertThat(context, instanceOf(CAJContext.class));
        CAJContext cajContext = (CAJContext) context;
        assertThat(cajContext.getAddressList(), equalTo(""));
        assertThat(cajContext.isAutoAddressList(), equalTo(true));
        assertThat(cajContext.getConnectionTimeout(), equalTo(30.0F));
        assertThat(cajContext.getBeaconPeriod(), equalTo(15.0F));
        assertThat(cajContext.getRepeaterPort(), equalTo(5065));
        assertThat(cajContext.getServerPort(), equalTo(5064));
        assertThat(cajContext.getMaxArrayBytes(), equalTo(16384));
        context.dispose();
    }
}
