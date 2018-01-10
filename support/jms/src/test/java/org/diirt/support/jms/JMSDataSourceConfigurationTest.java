/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.jms;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class JMSDataSourceConfigurationTest {

    @Test
    public void readDefaultConfiguration() {
        JMSDataSourceConfiguration conf = new JMSDataSourceConfiguration().read(this.getClass().getResourceAsStream("jms.default.xml"));
        assertThat(conf.getBrokerUrl(), equalTo("tcp://localhost:61616"));
    }

    @Test
    public void readConfiguration1() {
        JMSDataSourceConfiguration conf = new JMSDataSourceConfiguration().read(this.getClass().getResourceAsStream("jms.xml"));
        assertThat(conf.getBrokerUrl(), equalTo("tcp://localhost:61616"));
    }

}
