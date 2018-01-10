/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class ValidatorsTest {

    public ValidatorsTest() {
    }

    @Test
    public void matchCycle() {
        List<Object> matchValues = Arrays.<Object>asList(newVDouble(1.0), newVDouble(2.0), newVDouble(3.0), newVDouble(4.0));
        List<Object> values = Arrays.<Object>asList(newVDouble(1.0), newVDouble(2.0), newVDouble(3.0), newVDouble(4.0));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 0, matchValues, values), equalTo(true));
        values = Arrays.<Object>asList(newVDouble(1.0), newVDouble(2.0), newVDouble(3.0), newVDouble(4.0),
                newVDouble(1.0), newVDouble(2.0), newVDouble(3.0), newVDouble(4.0),
                newVDouble(1.0), newVDouble(2.0));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 0, matchValues, values), equalTo(true));
        values = Arrays.<Object>asList(newVDouble(3.0), newVDouble(4.0), newVDouble(1.0), newVDouble(2.0));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 0, matchValues, values), equalTo(false));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 1, matchValues, values), equalTo(false));
        assertThat(Validators.matchCycle(VTypeMatchMask.VALUE, 2, matchValues, values), equalTo(true));
    }

}
