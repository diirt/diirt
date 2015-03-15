/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

/**
 * Creates bubble graphs.
 * 
 * @author mjchao
 */
public class BubbleGraphApp extends BaseGraphApp {
    
    final private BubbleGraphView bubbleGraphView = new BubbleGraphView();
    final private ConfigurationDialog configuration = new ConfigurationDialog();

    @Override
    public BaseGraphView getGraphView() {
	return this.bubbleGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	
	SimpleStringProperty xCol = new SimpleStringProperty( this , "X Column" , "" );
	configuration.addStringProperty( xCol , new Runnable() {

	    @Override
	    public void run() {
		bubbleGraphView.setXColumn( xCol.getValue().equals( "" )? null : xCol.getValue() );
		bubbleGraphView.reconnect();
	    }
	    
	});
	
	SimpleStringProperty yCol = new SimpleStringProperty( this , "Y Column" , "" );
	configuration.addStringProperty( yCol , new Runnable(){

	    @Override
	    public void run() {
		bubbleGraphView.setYColumn( yCol.getValue().equals( "" )? null : yCol.getValue() );
		bubbleGraphView.reconnect();
	    }
	});
	
	SimpleStringProperty sizeCol = new SimpleStringProperty( this , "Size Column" , "" );
	configuration.addStringProperty( sizeCol , new Runnable() {
	    
	    @Override
	    public void run() {
		bubbleGraphView.setSizeColumn( sizeCol.getValue().equals( "" )? null : sizeCol.getValue() );
		bubbleGraphView.reconnect();
	    }
	});
	
	SimpleStringProperty colorCol = new SimpleStringProperty( this , "Color Column" , "" );
	configuration.addStringProperty( colorCol , new Runnable() {
	    
	    @Override
	    public void run() {
		bubbleGraphView.setColorColumn( colorCol.getValue().equals( "" )? null : colorCol.getValue() );
		bubbleGraphView.reconnect();
	    }
	});
	
	SimpleBooleanProperty highlightFocus = new SimpleBooleanProperty( this , "Highlight Focus" , false );
	configuration.addBooleanProperty( highlightFocus , new Runnable() {
	    
	    @Override
	    public void run() {
		bubbleGraphView.setHighlightFocusValue( highlightFocus.getValue() );
	    }
	});

	this.addDataFormulae( "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'), column(\"SIZE\", 'sim://gaussianWaveform'), column(\"COLOR\", 'sim://sineWaveform'))",
                "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'))",
                "=tableOf(column(\"X\", arrayOf(2,3,4,3,2,1,0,1)), column(\"Y\", arrayOf(0,1,2,3,4,3,2,1)), column(\"SIZE\", arrayOf(5,4,3,2,1,2,3,4)), column(\"SIZE\", arrayOf(\"A\",\"A\",\"B\",\"B\",\"B\",\"B\",\"A\",\"A\")))",
                "=tableOf(column(\"X\", arrayOf(1,2,3,4,5)), column(\"Y\", arrayOf(3,1,4,2,5)), column(\"NAMES\", arrayOf(\"A\", \"A\", \"A\", \"B\", \"B\")))"
	
	);
    }
    
    @Override
    public void openConfigurationPanel() {
	this.configuration.open();
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
