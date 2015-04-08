/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphene;

import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author mjchao
 */
public class ScatterGraphApp extends BaseGraphApp {

    final private ScatterGraphView scatterGraphView = new ScatterGraphView();
    
    @Override
    public BaseGraphView getGraphView() {
	return this.scatterGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	
	this.addDataFormulae( "sim://table", 
                    "=tableOf(column(\"X\", step(0, 1)), column(\"Y\", 'sim://gaussianWaveform'))", 
                    "=tableOf(column(\"X\", 'sim://sineWaveform(1,100,100,0.01)'), column(\"Y\", 'sim://sineWaveform(10,100,100,0.01)'))",
                    "=tableOf(column(\"X\", 'sim://triangleWaveform(10,100,100,0.01)'), column(\"Y\", 'sim://triangleWaveform(20,100,100,0.01)'))" );
    }
    
    @Override
    public void openConfigurationPanel() {
	this.scatterGraphView.getDefaultConfigurationDialog().open();
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
    
}
