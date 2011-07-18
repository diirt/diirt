/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.loc.LocalDataSource;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class PVSyntaxTest {

    @Test
    public void readAndWriteBlackBox1() throws Exception {
        String channelName = "test";
        DataSource dataSource = new LocalDataSource();
        
        PV<Object, Object> pv = PVManager.readAndWrite(channel(channelName)).from(dataSource).synchWriteAndReadAtHz(50);
        assertThat(pv.getValue(), nullValue());
        
        pv.write(10);
        Thread.sleep(50);
        assertThat(pv.getValue(), not(nullValue()));
        assertThat(((VDouble) pv.getValue()).getValue(), equalTo(10.0));
    }

}
