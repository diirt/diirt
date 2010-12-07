/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @Test
    public void testTypeOf() {
        assertThat(Utils.typeOf(ValueFactory.newVString(null, AlarmSeverity.NONE, AlarmStatus.DEVICE, null, Integer.MIN_VALUE)),
                equalTo((Class) VString.class));
        assertThat(Utils.typeOf(ValueFactory.newVDouble(Double.NaN, AlarmSeverity.NONE, AlarmStatus.DEVICE, null, Integer.MIN_VALUE, Double.NaN, Double.MAX_VALUE, Double.NaN, null, null, Double.NaN, Double.MAX_VALUE, Double.NaN, Double.MAX_VALUE, Double.MAX_VALUE)),
                equalTo((Class) VDouble.class));
    }

}