/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import java.util.List;

import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.vtype.DataTypeSupport;
import java.time.Duration;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.diirt.datasource.ExpressionLanguage.*;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static org.diirt.vtype.ValueFactory.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.time.Instant;

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
        ReadExpressionTester exp = new ReadExpressionTester(timedCacheOf(vDouble("x"), Duration.ofMillis(100)));

        Instant reference = Instant.now();
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(0)))));
        assertThat(((List) exp.getValue()).size(), equalTo(1));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(10)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(20)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(30)))));
        assertThat(((List) exp.getValue()).size(), equalTo(4));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(40)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(50)))));
        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(60)))));
        assertThat(((List) exp.getValue()).size(), equalTo(7));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(115)))));
        assertThat(((List) exp.getValue()).size(), equalTo(6));

        exp.writeValue("x", newVDouble(0.0, newTime(reference.plus(Duration.ofMillis(155)))));
        assertThat(((List) exp.getValue()).size(), equalTo(3));
    }

}