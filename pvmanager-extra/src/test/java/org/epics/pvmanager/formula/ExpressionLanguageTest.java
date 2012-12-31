/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.epics.vtype.VDouble;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import static org.epics.pvmanager.formula.ExpressionLanguage.*;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.util.array.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {

    @Test
    public void add1() {
        DesiredRateExpression<VDouble> exp = add(vConst(3.0), vConst(4.0));
        VDouble result = exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(7.0));
    }

    @Test
    public void substract1() {
        DesiredRateExpression<VDouble> exp = subtract(vConst(3.0), vConst(4.0));
        VDouble result = exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-1.0));
    }

    @Test
    public void multiply1() {
        DesiredRateExpression<VDouble> exp = multiply(vConst(3.0), vConst(4.0));
        VDouble result = exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(12.0));
    }

    @Test
    public void divide1() {
        DesiredRateExpression<VDouble> exp = divide(vConst(12.0), vConst(4.0));
        VDouble result = exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void reminder1() {
        DesiredRateExpression<VDouble> exp = reminder(vConst(9.0), vConst(4.0));
        VDouble result = exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1.0));
    }
    
    @Test
    public void formula1() {
        DesiredRateExpression<?> test = formula("1+2-3");
        VDouble result = (VDouble) test.getFunction().readValue();
        assertThat(result.getValue(), equalTo(0.0));
    }
}
