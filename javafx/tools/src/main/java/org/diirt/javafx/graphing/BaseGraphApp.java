/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Allows the user to select some form of data (e.g. a Gaussian wave or a sine
 * wave), and graphs it with some available configurations (e.g. modifying the 
 * interpolation scheme).
 * <p>
 * To produce a custom graph app, e.g. a <code>BubbleGraphApp</code>, you first extend the
 * <code>BaseGraphApp</code>. You must also create a custom graph view, e.g. a <code>BubbleGraphView</code>,
 * by extending the <code>BaseGraphView</code>. Details on how to create the <code>BaseGraphView</code>
 * are available in the documentation for that class. When you have the <code>BaseGraphView</code>, you
 * can override the getGraphView() method to return that.
 * 
 * <p>
 * Then, you add some data formulae to the app, using
 * <p>
 * <code>
 *     addDataFormulae( "[formula 1]", "[formula 2]" , ... )
 * </code>
 * <p>
 * This can be done in the constructor or by overriding the start() method.
 * 
 * <p>
 * If you wish to allow the user to configure properties of the graph, for example
 * change its interpolation scheme, you must create a <code>ConfigurationDialog</code>. 
 * More details on how to use <code>ConfigurationDialog</code> are available in
 * the documentation for that class.
 * 
 * @see BaseGraphView
 * @see ConfigurationDialog
 * @author mjchao
 */
abstract public class BaseGraphApp extends Application {
    
    final private DataSelectionPanel pnlData = new DataSelectionPanel();
    private BaseGraphView pnlGraph;
    private MessagePanel pnlError;
    
    abstract public BaseGraphView getGraphView();
    
    @Override
    public void start( Stage stage ) throws Exception {
	this.pnlGraph = getGraphView();
	this.pnlError = new MessagePanel( pnlGraph.lastExceptionProperty() );
	
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
	final private Label lblData = new Label( "Data:" );
	final private ComboBox<String> cboSelectData = new ComboBox<String>();
	final private Button cmdConfigure = new Button( "Configure" );
	
	public DataSelectionPanel() {
	    FlowPane pnlCenter = new FlowPane();
	    this.cboSelectData.setPrefWidth( 375 );
	    pnlCenter.getChildren().addAll( this.lblData , this.cboSelectData , this.cmdConfigure );
	    
	    this.setCenter( pnlCenter );
	    
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
	    
	    this.cmdConfigure.setOnAction( new EventHandler< ActionEvent >() {

		@Override
		public void handle(ActionEvent event) {
		    BaseGraphApp.this.openConfigurationPanel();
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
	
	/**
	 * @return the formula the user has currently selected to be displayed
	 * in the graph
	 */
	final public String getSelectedFormula() {
	    return this.cboSelectData.getValue();
	}
    }
    
    /**
     * Shows the configuration panel that allows the user to configure properties
     * of the graph, such as its interpolation scheme or its x column. Override
     * this to show custom configuration panels for specific graph types.
     */
    public void openConfigurationPanel() {
	//do nothing, by default
    }
    
    /**
     * Panel for displaying any messages that may be received from the data
     * source.
     */
    private class MessagePanel extends BorderPane {
	
	final private Label lblMessage = new Label();
	final private ReadOnlyProperty< Exception > lastException;
	
	public MessagePanel( ReadOnlyProperty< Exception > lastException ) {
	    this.setCenter( lblMessage );
	    this.lastException = lastException;
	    this.lastException.addListener( new ChangeListener< Exception >() {

		@Override
		public void changed(ObservableValue<? extends Exception> observable, Exception oldValue, Exception newValue) {
		    setMessage( newValue == null? "" : newValue.getMessage() );
		}
		
	    });
	}
	
	public void setMessage( String message ) {
	    this.lblMessage.setText( message );
	}
    }
}
