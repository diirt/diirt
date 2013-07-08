/**
 * 
 */
package org.epics.pvmanager.formula;

/**
 * @author shroffk
 * 
 */
public class StringFunctionSet extends FormulaFunctionSet {

    public StringFunctionSet() {
	super(new FormulaFunctionSetDescription("String",
		"Function to aggregate and manipulate strings")
		.addFormulaFunction(new ConcatFunction()));
    }

}
