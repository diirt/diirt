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
import static org.mockito.Mockito.*;

/**
 *
 * @author carcassi
 */
public class FormulaFunctionsTest {

    @Test
    public void matchArgumentTypes1() {
        FormulaFunction formula = mock(FormulaFunction.class);
        when(formula.isVarargs()).thenReturn(false);
        when(formula.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class, String.class));
        
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test"), formula), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList("test", "test"), formula), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0, ""), formula), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0), formula), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", 1.0), formula), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", "test"), formula), equalTo(false));
    }

    @Test
    public void matchArgumentTypes2() {
        FormulaFunction formula = mock(FormulaFunction.class);
        when(formula.isVarargs()).thenReturn(true);
        when(formula.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class, String.class));
        
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test"), formula), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList("test", "test"), formula), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0, ""), formula), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0), formula), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", 1.0), formula), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", "test"), formula), equalTo(true));
    }
    
}
