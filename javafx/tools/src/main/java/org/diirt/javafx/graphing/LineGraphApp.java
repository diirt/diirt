/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import org.diirt.graphene.InterpolationScheme;

/**
 *
 * @author mjchao
 */
public class LineGraphApp extends BaseGraphApp {

    final private LineGraphView lineGraphView = new LineGraphView();
    
    final private ConfigurationDialog config = new ConfigurationDialog();
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
	
	
	String[] interpolations = new String[] { "NEAREST_NEIGHBOR" , "LINEAR" , "CUBIC" };
	StringProperty interpolationScheme = new SimpleStringProperty( this , "Interpolation Scheme" , interpolations[ 0 ] );
	config.addListProperty( interpolationScheme , interpolations , new Runnable() {
	    
	    @Override
	    public void run() {
		switch( interpolationScheme.getValue() ) {
		    case "NEAREST_NEIGHBOR":
			lineGraphView.setInterpolationScheme( InterpolationScheme.NEAREST_NEIGHBOR );
			break;
		    case "LINEAR":
			lineGraphView.setInterpolationScheme( InterpolationScheme.LINEAR );
			break;
		    case "CUBIC":
			lineGraphView.setInterpolationScheme( InterpolationScheme.CUBIC );
			break;
		    default:
			lineGraphView.setInterpolationScheme( InterpolationScheme.NEAREST_NEIGHBOR );
		}
	    }
	});
	
    }
    
    @Override
    public void openConfigurationPanel() {
	this.config.open();
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
    
}
