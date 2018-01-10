/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.vtype.VMultiDouble;
import java.util.Arrays;
import org.diirt.datasource.ReadExpressionTester;
import static org.diirt.vtype.ValueFactory.*;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static java.time.Duration.*;
import java.time.Instant;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class SynchronizedArrayTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        DataTypeSupport.install();
    }

    @Test
    public void missingValues() throws InterruptedException {
        ReadExpressionTester exp = new ReadExpressionTester(synchronizedArrayOf(ofNanos(10), ofMillis(10), vDoubles(Arrays.asList("pv1","pv2","pv3","pv4","pv5"))));

        Instant reference = Instant.now();
        Instant secondPass = reference.plus(ofMillis(1));
        Instant thirdPass = reference.plus(ofMillis(2));

        // Set values
        exp.writeValue("pv1", newVDouble(0.0, newTime(reference)));
        exp.writeValue("pv1", newVDouble(1.0, newTime(secondPass)));

        // Set values
        exp.writeValue("pv2", newVDouble(0.0, newTime(reference)));
        exp.writeValue("pv2", newVDouble(1.0, newTime(secondPass)));
        exp.writeValue("pv2", newVDouble(2.0, newTime(thirdPass)));

        // Set values
        exp.writeValue("pv3", newVDouble(1.0, newTime(secondPass)));
        exp.writeValue("pv3", newVDouble(2.0, newTime(thirdPass)));

        // Set values
        exp.writeValue("pv4", newVDouble(0.0, newTime(reference)));
        exp.writeValue("pv4", newVDouble(2.0, newTime(thirdPass)));

        // Set values
        exp.writeValue("pv5", newVDouble(0.0, newTime(reference)));

        VMultiDouble array = (VMultiDouble) exp.getValue();
        assertEquals(0.0, array.getValues().get(0).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(1).getValue(), 0.0);
        assertNull(array.getValues().get(2));
        assertEquals(0.0, array.getValues().get(3).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(4).getValue(), 0.0);

    }

    @Test
    public void reconstructArray() throws InterruptedException {
        ReadExpressionTester exp = new ReadExpressionTester(synchronizedArrayOf(ofMillis(5), ofMillis(10), vDoubles(Arrays.asList("pv1","pv2","pv3","pv4","pv5"))));

        Instant reference = Instant.now();
        Instant future1 = reference.plus(ofMillis(1));
        Instant past1 = reference.minus(ofMillis(1));
        Instant past2 = reference.minus(ofMillis(2));

        // Set values
        exp.writeValue("pv1", newVDouble(0.0, newTime(reference)));
        exp.writeValue("pv1", newVDouble(1.0, newTime(future1)));

        // Set values
        exp.writeValue("pv2", newVDouble(1.0, newTime(past2)));
        exp.writeValue("pv2", newVDouble(1.0, newTime(past1)));
        exp.writeValue("pv2", newVDouble(0.0, newTime(reference)));

        // Set values
        exp.writeValue("pv3", newVDouble(1.0, newTime(past1)));
        exp.writeValue("pv3", newVDouble(0.0, newTime(reference)));

        // Set values
        exp.writeValue("pv4", newVDouble(0.0, newTime(reference)));
        exp.writeValue("pv4", newVDouble(2.0, newTime(future1)));

        // Set values
        exp.writeValue("pv5", newVDouble(0.0, newTime(reference)));

        VMultiDouble array = (VMultiDouble) exp.getValue();
        assertEquals(0.0, array.getValues().get(0).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(1).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(2).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(3).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(4).getValue(), 0.0);

    }

}