/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VString;
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
    public void testCast1() {
        LastOfChannelExpression<Object> exp = new LastOfChannelExpression<Object>("test", Object.class);
        LastOfChannelExpression<Object> exp2 = exp.cast(Object.class);
        assertThat(exp2, sameInstance(exp));
    }

    @Test
    public void testCast2() {
        LastOfChannelExpression<Object> exp = new LastOfChannelExpression<Object>("test", Object.class);
        LastOfChannelExpression<VDouble> vDoubleExp = exp.cast(VDouble.class);
        assertThat(vDoubleExp.getType(), equalTo(VDouble.class));
        assertThat(vDoubleExp.getName(), equalTo("test"));
    }

    @Test
    public void testCast3() {
        LastOfChannelExpression<VDouble> exp = new LastOfChannelExpression<VDouble>("test", VDouble.class);
        LastOfChannelExpression<?> exp2 = exp.cast(Object.class);
        assertThat(exp2 == exp, equalTo(true));
        assertThat(exp2.getName(), equalTo("test"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCast4() {
        LastOfChannelExpression<VDouble> exp = new LastOfChannelExpression<VDouble>("test", VDouble.class);
        LastOfChannelExpression<?> exp2 = exp.cast(VString.class);
    }
}
