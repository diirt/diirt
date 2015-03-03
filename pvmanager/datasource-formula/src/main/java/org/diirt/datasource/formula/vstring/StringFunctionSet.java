/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vstring;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;

/**
 * A set of functions to work with {@link org.diirt.vtype.VString}.
 * 
 * @author shroffk
 * 
 */
public class StringFunctionSet extends FormulaFunctionSet {

    /**
     * Creates a new set.
     */
    public StringFunctionSet() {
	super(new FormulaFunctionSetDescription("vstring",
		"Function to aggregate and manipulate strings")
		.addFormulaFunction(new ConcatStringArrayFunction())
		.addFormulaFunction(new ConcatStringsFunction())
		.addFormulaFunction(new ToStringFunction())
                );
    }

}
