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
public class DataFormulaFactory {
    
    final public static DataFormula fromFormula( final BaseGraphView view , final String formula ) {
	
	DataFormula rtn = new DataFormula() {

	    @Override
	    public void onSelected() {
		view.reconnect( formula );
	    }
	};
	return rtn;
    }
    
    final public static DataFormula fromNameAndFormula( final BaseGraphView view , final String name , final String formula ) {
	DataFormula rtn = new DataFormula( name ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( formula );
	    }
	};
	return rtn;
    }
    
    final public static DataFormula sineWave( final BaseGraphView view ) {
	DataFormula rtn = new DataFormula( "Sine wave with some properties..." ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( "sim://sine2DWaveform(1,50,45,100,100,0.1)" );
	    }
	};
	return rtn;
    }
    
    final public static DataFormula gaussianWaveform( final BaseGraphView view ) {
	DataFormula rtn = new DataFormula( "Gaussian Waveform" ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( "sim://gaussianWaveform" );
	    }
	};
	return rtn;
    }
}
