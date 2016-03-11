/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import java.util.List;

import org.diirt.datasource.ReadExpressionTester;
import org.diirt.util.time.TimeDuration;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.diirt.datasource.ExpressionLanguage.*;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static org.diirt.vtype.ValueFactory.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.diirt.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class TimedCacheCollectorTest {

    public TimedCacheCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Force type support loading
        DataTypeSupport.install();
    }

    @Test
    public void correctNumberOfValuesInCache() throws InterruptedException {
        ReadExpressionTester exp = new ReadExpressionTester(timedCacheOf(vDouble("x"), TimeDuration.ofMillis(100)));

        Timestamp reference = Timestamp.now();
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(0)))));
        assertThat(((List) exp.getValue()).size(), equalTo(1));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(10)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(20)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(30)))));
        assertThat(((List) exp.getValue()).size(), equalTo(4));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(40)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(50)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(60)))));
        assertThat(((List) exp.getValue()).size(), equalTo(7));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(115)))));
        assertThat(((List) exp.getValue()).size(), equalTo(6));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(TimeDuration.ofMillis(155)))));
        assertThat(((List) exp.getValue()).size(), equalTo(3));
    }

}