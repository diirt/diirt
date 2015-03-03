/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import org.diirt.datasource.formula.vtable.TableFunctionSet;
import org.junit.BeforeClass;

/**
 * 
 * @author carcassi
 */
public class BaseTestForFormula {

    @BeforeClass
    public static void initMath() {
	if (FormulaRegistry.getDefault().findFunctionSet("math") == null) {
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new MathFunctionSet());
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new NumberOperatorFunctionSet());
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new ArrayFunctionSet());
	    FormulaRegistry.getDefault().registerFormulaFunctionSet(
		    new TableFunctionSet());
	}
    }
}
