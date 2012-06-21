/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import org.epics.pvmanager.data.VDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class LastOfChannelExpressionTest {
    
    public LastOfChannelExpressionTest() {
    }

    @Test
    public void testCast() {
        LastOfChannelExpression<Object> exp = new LastOfChannelExpression<Object>("test", Object.class);
        LastOfChannelExpression<VDouble> vDoubleExp = exp.cast(VDouble.class);
        assertThat(vDoubleExp.getType(), equalTo(VDouble.class));
    }
}
