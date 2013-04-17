/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import org.epics.pvmanager.formula.LastOfChannelExpression;
import org.epics.vtype.VNumber;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class MathFunctionSetTest {

    private static MathFunctionSet set = new MathFunctionSet();

    @Test
    public void abs1() {
        Collection<FormulaFunction> functions = set.findFunctions("abs");
        assertThat(functions.size(), equalTo(1));
        FormulaFunction function = functions.iterator().next();
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(1.0)));
        assertThat(value.getValue().doubleValue(), equalTo(1.0));
        value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(-1.0)));
        assertThat(value.getValue().doubleValue(), equalTo(1.0));
    }

    @Test
    public void acos1() {
        Collection<FormulaFunction> functions = set.findFunctions("acos");
        assertThat(functions.size(), equalTo(1));
        FormulaFunction function = functions.iterator().next();
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(0.0)));
        assertThat(value.getValue().doubleValue(), closeTo(3.1415/2.0, 0.0001));
        value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(1.0)));
        assertThat(value.getValue().doubleValue(), closeTo(0.0, 0.0001));
    }

    @Test
    public void log1() {
        Collection<FormulaFunction> functions = set.findFunctions("log");
        assertThat(functions.size(), equalTo(1));
        FormulaFunction function = functions.iterator().next();
        VNumber value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(Math.E)));
        assertThat(value.getValue().doubleValue(), equalTo(1.0));
        value = (VNumber) function.calculate(Arrays.<Object>asList(newVDouble(1.0)));
        assertThat(value.getValue().doubleValue(), equalTo(0.0));
    }
}
