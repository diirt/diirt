/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.epics.pvmanager.integration;

import java.util.Arrays;
import java.util.List;
import org.epics.vtype.VDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.vtype.ValueFactory.*;

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
