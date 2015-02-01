/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

/**
 *
 * @author Mickey
 */
abstract class DataFormula {
    
    	final private String textualRepresentation;
	
	public DataFormula() {
	   this.textualRepresentation = ""; 
	}
	
	public DataFormula( String formName ) {
	    this.textualRepresentation = formName;
	}
	
	@Override
	public String toString() {
	    return this.textualRepresentation;
	}
	
	/**
	 * Handles what happens when the user selects this data form.
	 * Override to provide custom functionality.
	 */
	abstract public void onSelected();
}
