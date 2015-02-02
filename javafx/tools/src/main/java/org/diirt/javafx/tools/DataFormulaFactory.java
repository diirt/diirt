/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

/**
 * Creates Data Formula objects.
 * 
 * @author Mickey
 */
public class DataFormulaFactory {
    
    final static DataFormula fromFormula( final BaseGraphView view , final String formula ) {
	
	DataFormula rtn = new DataFormula( formula ) {

	    @Override
	    public void onSelected() {
		view.reconnect( formula );
	    }
	};
	return rtn;
    }
    
    final static DataFormula fromNameAndFormula( final BaseGraphView view , final String name , final String formula ) {
	DataFormula rtn = new DataFormula( name ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( formula );
	    }
	};
	return rtn;
    }
    
    final static DataFormula sineWave( final BaseGraphView view ) {
	DataFormula rtn = new DataFormula( "Sine wave with some properties..." ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( "sim://sine2DWaveform(1,50,45,100,100,0.1)" );
	    }
	};
	return rtn;
    }
    
    final static DataFormula gaussianWaveform( final BaseGraphView view ) {
	DataFormula rtn = new DataFormula( "Gaussian Waveform" ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( "sim://gaussianWaveform" );
	    }
	};
	return rtn;
    }
    
    final static DataFormula histogram( final BaseGraphView view ) {
	DataFormula rtn = new DataFormula( "Histogram with some properties..." ) {
	    
	    @Override
	    public void onSelected() {
		view.reconnect( "=histogram2DOf(tableOf(column(\"X\", 'sim://sineWaveform(1,100,100,0.01)'), column(\"Y\", 'sim://sineWaveform(10,100,100,0.01)')), \"Y\", \"X\")" );
	    }
	};
	return rtn;
    }
}
