/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import static org.epics.pvmanager.formula.BaseTestForFormula.testFunction;
import static org.epics.pvmanager.formula.BaseTestForFormula.testTwoArgNumericFunction;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class NumberOperatorFunctionSetTest extends BaseTestForFormula {

    private static FormulaFunctionSet set = new NumberOperatorFunctionSet();
    
    @Test
    public void add1() {
        testTwoArgNumericFunction(set, "+", 1.0, 2.0, 3.0);
        testTwoArgNumericFunction(set, "+", 1.0, -2.0, -1.0);
    }
    
    @Test
    public void subtract1() {
        testTwoArgNumericFunction(set, "-", 1.0, 2.0, -1.0);
        testTwoArgNumericFunction(set, "-", 1.0, -2.0, 3.0);
    }
    
    @Test
    public void negate1() {
        testFunction(set, "-", 1.0, -1.0);
        testFunction(set, "-", -2.0, 2.0);
    }
    
    @Test
    public void multiply1() {
        testTwoArgNumericFunction(set, "*", 2.0, 5.0, 10.0);
        testTwoArgNumericFunction(set, "*", 3.0, -2.0, -6.0);
    }
    
    @Test
    public void divide1() {
        testTwoArgNumericFunction(set, "/", 8.0, 2.0, 4.0);
        testTwoArgNumericFunction(set, "/", 1.0, -2.0, -0.5);
    }
    
    @Test
    public void remainder1() {
        testTwoArgNumericFunction(set, "%", 8.0, 2.0, 0.0);
        testTwoArgNumericFunction(set, "%", 3.0, 2.0, 1.0);
    }
    
    @Test
    public void pow1() {
        testTwoArgNumericFunction(set, "^", 8.0, 2.0, 64.0);
        testTwoArgNumericFunction(set, "^", 4.0, 0.5, 2.0);
    }
}
