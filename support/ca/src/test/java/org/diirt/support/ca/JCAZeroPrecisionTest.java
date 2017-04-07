/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.CAStatus;
import gov.aps.jca.Channel;
import gov.aps.jca.dbr.DBR_CTRL_Double;
import gov.aps.jca.dbr.DBR_Double;
import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.aps.jca.dbr.Severity;
import gov.aps.jca.dbr.Status;
import gov.aps.jca.event.MonitorEvent;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import org.diirt.datasource.ValueCache;
import org.diirt.datasource.ValueCacheImpl;
import org.diirt.vtype.VDouble;
import java.time.Instant;
import static org.diirt.support.ca.JCAVTypeAdapterSetTest.*;

/**
 *
 * @author carcassi
 */
public class JCAZeroPrecisionTest {

    @Test
    public void honorZeroPrecision1() {
        ValueCache<VDouble> cache = new ValueCacheImpl<VDouble>(VDouble.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;

        JCAConnectionPayload connPayload = mockJCAConnectionPayload(DBR_Double.TYPE, 1, Channel.ConnectionState.CONNECTED);
        when(connPayload.getJcaDataSource().isHonorZeroPrecision()).thenReturn(true);

        Instant timestamp = Instant.ofEpochSecond(1234567,1234);
        DBR_TIME_Double value = createDBRTimeDouble(new double[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        meta.setPrecision((short) 0);
        MonitorEvent event = new MonitorEvent(connPayload.getChannel(), value, CAStatus.NORMAL);

        adapter.updateCache(cache, connPayload, new JCAMessagePayload(meta, event));

        assertThat(cache.readValue().getFormat().format(cache.readValue().getValue()), equalTo("3"));
    }

    @Test
    public void honorZeroPrecision2() {
        ValueCache<VDouble> cache = new ValueCacheImpl<VDouble>(VDouble.class);
        JCATypeAdapter adapter = JCAVTypeAdapterSet.DBRDoubleToVDouble;

        JCAConnectionPayload connPayload = mockJCAConnectionPayload(DBR_Double.TYPE, 1, Channel.ConnectionState.CONNECTED);
        when(connPayload.getJcaDataSource().isHonorZeroPrecision()).thenReturn(false);

        Instant timestamp = Instant.ofEpochSecond(1234567,1234);
        DBR_TIME_Double value = createDBRTimeDouble(new double[]{3.25F}, Severity.MINOR_ALARM, Status.HIGH_ALARM, timestamp);
        DBR_CTRL_Double meta = createNumericMetadata();
        meta.setPrecision((short) 0);
        MonitorEvent event = new MonitorEvent(connPayload.getChannel(), value, CAStatus.NORMAL);

        adapter.updateCache(cache, connPayload, new JCAMessagePayload(meta, event));

        assertThat(cache.readValue().getFormat().format(cache.readValue().getValue()), equalTo("3.25"));
    }
}
