/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

/**
 * Represents a data formula that can be sent to a data source to get back
 * data.
 * 
 * @author mjchao
 */
abstract class DataFormula {
    
    	final private String textualRepresentation;
	
	/**
	 * Creates a default data formula with no textual representation.
	 */
	public DataFormula() {
	   this.textualRepresentation = ""; 
	}
	
	/**
	 * Creates a data formula using the given formula and sets its
	 * textual representation to that formula.
	 * 
	 * @param formula the formula to use
	 */
	public DataFormula( String formula ) {
	    this.textualRepresentation = formula;
	}
	
	@Override
	public String toString() {
	    return this.textualRepresentation;
	}
	
	/**
	 * Handles what happens when the user selects this data form.
	 * Override to provide custom functionality. Ideally, it would then
	 * take this formula, give it to the data source, and finally receive
	 * data to be graphed.
	 */
	abstract public void onSelected();
}
