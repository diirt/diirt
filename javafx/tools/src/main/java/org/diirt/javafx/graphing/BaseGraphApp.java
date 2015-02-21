/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import org.diirt.javafx.tools.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Allows the user to select some form of data (e.g. a Gaussian wave or a sine
 * wave) and graphs it.
 * <p>
 * To produce a custom graph app, e.g. a BubbleGraphApp, you first extend the
 * BaseGraphApp. Inside this new class, you must create the BaseGraphView object
 * templated with the correct Graph2DRendererUpdate for your graph type.
 * Furthermore, you must define how to create expressions to be sent to
 * PVManager for data. Inside the BaseGraphView object, you will also find a
 * Graph2DExpression object which represents the data to be plotted on the graph.
 * You can send updates to this if you choose to.
 * <p>
 * The BaseGraphView object is created, so you can define the getGraphView()
 * method. Now, everything should be all set up. Just override start(), 
 * add your data formulae, and your graph app should be complete.
 * 
 * @author mjchao
 */
abstract public class BaseGraphApp extends Application {
    
    final private DataSelectionPanel pnlData = new DataSelectionPanel();
    private BaseGraphView pnlGraph;
    final private MessagePanel pnlError = new MessagePanel();
    
    abstract public BaseGraphView getGraphView();
    
    @Override
    public void start( Stage stage ) throws Exception {
	this.pnlGraph = getGraphView();
	
	BorderPane mainPanel = new BorderPane();
	    mainPanel.setTop( this.pnlData );
	    mainPanel.setCenter( this.pnlGraph );
	    mainPanel.setBottom( this.pnlError );
	    
	//allow components to shrink
	this.pnlData.setMinSize( 0 , 0 );
	this.pnlGraph.setMinSize( 0 , 0 );
	this.pnlError.setMinSize( 0 , 0 );
	
	
	
	Scene scene = new Scene( mainPanel );
	stage.setTitle( "diirt - BaseGraphApp" );
	stage.setScene( scene );
	stage.setWidth( 500 );
	stage.setHeight( 500 );
	stage.show();
    }
    
    /**
     * Reconnects to the data source with whatever data formula
     * is currently in use.
     */
    public void reconnect() {
	this.pnlGraph.setFormula( this.pnlData.getSelectedFormula() );
    }
    
    /**
     * Reconnects to the data source using the given data formula.
     * 
     * @param dataFormula the data formula to use
     */
    public void reconnect( String dataFormula ) {
	this.pnlGraph.setFormula( dataFormula );
    }
    
    /**
     * Adds the given data formulae to the list of acceptable data formulae
     * for this graphing application.
     * 
     * @param formulae the formulae to add
     */
    public void addDataFormulae( String... formulae ) {
	for ( String s : formulae ) {
	    this.pnlData.addDataFormulae( s );
	}
    }
    
    /**
     * Panel allowing the user to select some data to graph.
     */
    private class DataSelectionPanel extends BorderPane {
	
	/**
	 * the allowed data formulae are stored here and the user can
	 * select from this list
	 */
	final private ComboBox<String> cboSelectData = new ComboBox<String>();
	
	public DataSelectionPanel() {
	    this.setCenter( this.cboSelectData );
	    
	    //allow the combo box to stretch out and fill panel completely
	    this.cboSelectData.setMaxSize( Double.MAX_VALUE , Double.MAX_VALUE );
	    this.cboSelectData.setEditable( true );
	    
	    //watches for when the user selects a new data formula
	    this.cboSelectData.valueProperty().addListener( new ChangeListener< String >() {

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    reconnect( newValue );
		}
		
	    });
	}
	
	/**
	 * Adds the given data formulae objects to the list of acceptable
	 * data formulae.
	 * 
	 * @param f the data formula objects to add
	 */
	final public void addDataFormulae( String... s ) {
	    this.cboSelectData.getItems().addAll( s );
	    if ( cboSelectData.getItems().size() > 0 ) {
		this.cboSelectData.setValue( this.cboSelectData.getItems().get( 0 ) );
	    }
	}
	
	final public String getSelectedFormula() {
	    return this.cboSelectData.getValue();
	}
    }
    
    /**
     * Panel for displaying any messages that may be received from the data
     * source.
     */
    private class MessagePanel extends BorderPane {
	
    }
}
