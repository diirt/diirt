/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphene;

import javafx.stage.Stage;
import org.diirt.graphene.InterpolationScheme;

/**
 *
 * @author mjchao
 */
public class LineGraphApp extends BaseGraphApp {

    final private LineGraphView lineGraphView = new LineGraphView();

    @Override
    public BaseGraphView getGraphView() {
	return this.lineGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	
	this.addDataFormulae( "sim://table",
                    "sim://gaussianWaveform",
                    "sim://sineWaveform",
                    "sim://triangleWaveform",
                    "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))" );
    }
    
    @Override
    public void openConfigurationPanel() {
	this.lineGraphView.getDefaultConfigurationDialog().open();
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
    
}
