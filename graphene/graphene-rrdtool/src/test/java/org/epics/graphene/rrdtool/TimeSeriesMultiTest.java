/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListNumber;
import org.epics.util.time.Timestamp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.util.time.Timestamp.*;

/**
 *
 * @author carcassi
 */
public class TimeSeriesMultiTest {

    public TimeSeriesMultiTest() {
    }

    @Test
    public void synchronizeSeries1() {
        TimeSeries series1 = new TimeSeries(Arrays.asList(of(0,0), of(1,0), of(2,0), of(3,0), of(4,0), of(5,0), of(6,0), of(7,0), of(8,0), of(9,0), of(10,0)),
                new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        TimeSeries series2 = new TimeSeries(Arrays.asList(of(0,0), of(2,0), of(4,0), of(6,0), of(8,0), of(10,0)),
                new ArrayDouble(0, 1, 2, 3, 4, 5));
        Map<String, TimeSeries> map = new HashMap<>();
        map.put("series1", series1);
        map.put("series2", series2);
        TimeSeriesMulti multi = TimeSeriesMulti.synchronizeSeries(map);
        assertThat(multi.getTime().get(0), equalTo(of(0,0)));
        assertThat(multi.getTime().get(1), equalTo(of(2,0)));
        assertThat(multi.getTime().get(2), equalTo(of(4,0)));
        assertThat(multi.getTime().get(3), equalTo(of(6,0)));
        assertThat(multi.getTime().get(4), equalTo(of(8,0)));
        assertThat(multi.getTime().get(5), equalTo(of(10,0)));
        ListNumber data = multi.getValues().get("series1");
        assertThat(data.getDouble(0), equalTo(0.0));
        assertThat(data.getDouble(1), equalTo(2.0));
        assertThat(data.getDouble(2), equalTo(4.0));
        assertThat(data.getDouble(3), equalTo(6.0));
        assertThat(data.getDouble(4), equalTo(8.0));
        assertThat(data.getDouble(5), equalTo(10.0));
        data = multi.getValues().get("series2");
        assertThat(data.getDouble(0), equalTo(0.0));
        assertThat(data.getDouble(1), equalTo(1.0));
        assertThat(data.getDouble(2), equalTo(2.0));
        assertThat(data.getDouble(3), equalTo(3.0));
        assertThat(data.getDouble(4), equalTo(4.0));
        assertThat(data.getDouble(5), equalTo(5.0));
    }
}
