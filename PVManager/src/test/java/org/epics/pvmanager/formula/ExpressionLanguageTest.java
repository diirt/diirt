/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.epics.pvmanager.data.*;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
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
    public void statisticsOf1() {
        DesiredRateExpression<VDouble> exp = add(vConst(3.0), vConst(4.0));
        VDouble result = exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(7.0));
    }
}
