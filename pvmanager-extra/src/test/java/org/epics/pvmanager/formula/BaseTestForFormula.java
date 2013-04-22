/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author carcassi
 */
public class BaseTestForFormula {
        
    @BeforeClass
    public static void initMath() {
        if (FormulaRegistry.getDefault().findFunctionSet("math") == null) {
            FormulaRegistry.getDefault().registerFormulaFunctionSet(new MathFunctionSet());
            FormulaRegistry.getDefault().registerFormulaFunctionSet(new NumberOperatorFunctionSet());
            FormulaRegistry.getDefault().registerFormulaFunctionSet(new ArrayFunctionSet());
        }
    }

}
