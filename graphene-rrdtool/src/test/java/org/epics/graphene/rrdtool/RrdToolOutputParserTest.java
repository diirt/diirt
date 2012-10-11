/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    /**
     * Test of parse method, of class RrdToolOutputParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("rrdtool1.out")));
        RrdToolOutputParser instance = new RrdToolOutputParser();
        TimeSeries result = instance.parse(reader);
        assertThat(result.getTime().size(), equalTo(241));
        assertThat(result.getValues().size(), equalTo(241));
        assertThat(result.getTime().get(0), equalTo(Timestamp.of(1349877960, 0)));
        assertThat(result.getValues().getDouble(0), equalTo(Double.NaN));
        assertThat(result.getTime().get(150), equalTo(Timestamp.of(1349931960, 0)));
        assertThat(result.getValues().getDouble(150), equalTo(1.1737083333e+00));
        assertThat(result.getTime().get(240), equalTo(Timestamp.of(1349964360, 0)));
        assertThat(result.getValues().getDouble(240), equalTo(Double.NaN));
    }
}
