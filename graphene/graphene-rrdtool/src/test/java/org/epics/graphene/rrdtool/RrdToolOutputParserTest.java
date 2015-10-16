/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class RrdToolOutputParserTest {

    public RrdToolOutputParserTest() {
    }

    @Test
    public void parse1() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("rrdtool1.out")));
        RrdToolOutputParser instance = new RrdToolOutputParser();
        TimeSeriesMulti result = instance.parse(reader);
        assertThat(result.getTime().size(), equalTo(241));
        assertThat(result.getValues().size(), equalTo(1));
        ListDouble values = result.getValues().get("load_1min");
        assertThat(values.size(), equalTo(241));
        assertThat(result.getTime().get(0), equalTo(Timestamp.of(1349877960, 0)));
        assertThat(values.getDouble(0), equalTo(Double.NaN));
        assertThat(result.getTime().get(150), equalTo(Timestamp.of(1349931960, 0)));
        assertThat(values.getDouble(150), equalTo(1.1737083333e+00));
        assertThat(result.getTime().get(240), equalTo(Timestamp.of(1349964360, 0)));
        assertThat(values.getDouble(240), equalTo(Double.NaN));
    }

    @Test
    public void parse2() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("rrdtool2.out")));
        RrdToolOutputParser instance = new RrdToolOutputParser();
        TimeSeriesMulti result = instance.parse(reader);
        assertThat(result.getTime().size(), equalTo(241));
        assertThat(result.getValues().size(), equalTo(18));
        assertThat(result.getValues().keySet(), equalTo((Set<String>) new HashSet<String>(
                Arrays.asList("Setpoint", "Fan1cfm", "Temp1", "Temp3", "Fan2Rpm", "TempOvrSet", "Fan2cfm", "Totalcfm", "Fan1Status", "Temp2", "Fan1Rpm", "Fan2Status", "Fan3cfm", "Fan3Rpm", "Fan3Status", "Fan4cfm", "Fan4Rpm", "Fan4Status"))));
        ListDouble values = result.getValues().get("Temp1");
        assertThat(values.size(), equalTo(241));
        assertThat(result.getTime().get(0), equalTo(Timestamp.of(1355416920, 0)));
        assertThat(values.getDouble(0), equalTo(9.2500000000e+01));
        assertThat(result.getTime().get(150), equalTo(Timestamp.of(1355470920, 0)));
        assertThat(values.getDouble(150), equalTo(9.1000000000e+01));
        assertThat(result.getTime().get(240), equalTo(Timestamp.of(1355503320, 0)));
        assertThat(values.getDouble(240), equalTo(Double.NaN));
    }
}
