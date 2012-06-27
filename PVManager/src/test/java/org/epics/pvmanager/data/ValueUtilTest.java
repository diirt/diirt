/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.data.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class ValueUtilTest {

    public ValueUtilTest() {
    }

    @Test
    public void testTypeOf() {
        assertThat(ValueUtil.typeOf(newVString(null, alarmNone(), timeNow())),
                equalTo((Class) VString.class));
        assertThat(ValueUtil.typeOf(newVDouble(Double.NaN, alarmNone(), timeNow(), displayNone())),
                equalTo((Class) VDouble.class));
    }

}