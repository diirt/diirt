/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.HashMap;
import java.util.Map;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VString;
import org.diirt.datasource.loc.LocalDataSource;
import static org.diirt.util.time.TimeDuration.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class BlackBoxTest {

    @Test
    public void readAndWriteBlackBox1() throws Exception {
        String channelName = "test(0)";
        DataSource dataSource = new LocalDataSource();

        PV<Object, Object> pv = PVManager.readAndWrite(channel(channelName)).from(dataSource).synchWriteAndMaxReadRate(ofHertz(50));
        Thread.sleep(50);
        assertThat(pv.getValue(), not(nullValue()));
        assertThat(pv.isConnected(), equalTo(true));
        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(0.0));

        pv.write(10.0);
        Thread.sleep(50);
        assertThat(pv.getValue(), not(nullValue()));
        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(10.0));
        pv.close();
    }

    @Test
    public void readAndWriteBlackBox2() throws Exception {
        String channelName = "test(0)";
        DataSource dataSource = new LocalDataSource();

        PV<Object, Object> pv1 = PVManager.readAndWrite(channel(channelName)).from(dataSource).synchWriteAndMaxReadRate(ofHertz(50));
        PV<Object, Object> pv2 = PVManager.readAndWrite(channel(channelName)).from(dataSource).synchWriteAndMaxReadRate(ofHertz(50));
        Thread.sleep(50);
        assertThat(pv1.getValue(), not(nullValue()));
        assertThat(pv2.getValue(), not(nullValue()));
        assertThat(pv1.isConnected(), equalTo(true));
        assertThat(pv2.isConnected(), equalTo(true));
        assertThat(((VDouble) pv1.getValue()).getValue(), equalTo(0.0));
        assertThat(((VDouble) pv2.getValue()).getValue(), equalTo(0.0));

        pv1.write(10.0);
        Thread.sleep(50);
        assertThat(pv1.getValue(), not(nullValue()));
        assertThat(pv2.getValue(), not(nullValue()));
        assertThat(((VDouble) pv1.getValue()).getValue(), equalTo(10.0));
        assertThat(((VDouble) pv2.getValue()).getValue(), equalTo(10.0));
        pv1.close();
        pv2.close();
    }

    @Test
    public void readAndWriteMap() throws Exception {
        String channel1 = "channel1";
        String channel2 = "channel2";
        DataSource dataSource = new LocalDataSource();

        final PV<Map<String, Object>, Map<String, Object>> pv =
                PVManager.readAndWrite(mapOf(latestValueOf(channel("channel1")).and(latestValueOf(channel("channel2")))))
                .from(dataSource).synchWriteAndMaxReadRate(ofHertz(50));

        Map<String, Object> newValues = new HashMap<String, Object>();
        newValues.put(channel1, "test");
        newValues.put(channel2, 10.0);
        pv.write(newValues);
        Thread.sleep(50);
        assertThat(pv.getValue(), not(nullValue()));
        assertThat(((VString) pv.getValue().get(channel1)).getValue(), equalTo("test"));
        assertThat(((VDouble) pv.getValue().get(channel2)).getValue(), equalTo(10.0));
        pv.close();
    }

}
