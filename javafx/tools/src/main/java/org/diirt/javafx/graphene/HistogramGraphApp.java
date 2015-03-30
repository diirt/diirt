/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphene;

import javafx.stage.Stage;

/**
 *
 * @author mjchao
 */
public class HistogramGraphApp extends BaseGraphApp {

    private HistogramGraphView histogramGraphView = new HistogramGraphView();
    
    @Override
    public BaseGraphView getGraphView() {
	return this.histogramGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.addDataFormulae( "sim://gaussianWaveform",
                    "=histogramOf('sim://noiseWaveform')",
                    "=arrayWithBoundaries(arrayOf(1,3,2,4,3,5), range(-10,10))",
                    "=caHistogram(\"histo\")" );
    }
    
    @Override
    public void openConfigurationPanel() {
	this.histogramGraphView.getDefaultConfigurationDialog().open();
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
