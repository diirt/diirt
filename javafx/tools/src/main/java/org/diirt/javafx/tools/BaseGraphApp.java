/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.IntensityGraph2DRendererUpdate;

/**
 * Allows the user to select some form of data (e.g. a Gaussian wave or a sine
 * wave) and graphs it.
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
	this.pnlData.reselect();
    }
    
    /**
     * Reconnects to the data source using the given data formula.
     * 
     * @param dataFormula the data formula to use
     */
    public void reconnect( String dataFormula ) {
	this.pnlGraph.reconnect( dataFormula );
    }
    
    /**
     * Adds the given data formulae to the list of acceptable data formulae
     * for this graphing application.
     * 
     * @param formulae the formulae to add
     */
    public void addDataFormulae( String... formulae ) {
	for ( String s : formulae ) {
	    DataFormula f = DataFormulaFactory.fromFormula( this.pnlGraph , s );
	    this.pnlData.addDataFormulae( f );
	}
    }
    
    /**
     * Adds the given data formulae to the list of acceptable data formulae
     * for this graphing application.
     * 
     * @param formulae the formula objects to add
     */
    void addDataFormulae( DataFormula... formulae ) {
	this.pnlData.addDataFormulae( formulae );
    }
    
    /**
     * Panel allowing the user to select some data to graph.
     */
    private class DataSelectionPanel extends BorderPane {
	
	/**
	 * the allowed data formulae are stored here and the user can
	 * select from this list
	 */
	final private ComboBox< DataFormula > cboSelectData = new ComboBox< DataFormula >();
	
	public DataSelectionPanel() {
	    this.setCenter( this.cboSelectData );
	    
	    //allow the combo box to stretch out and fill panel completely
	    this.cboSelectData.setMaxSize( Double.MAX_VALUE , Double.MAX_VALUE );
	    
	    //allow the user to select no data
	    DataFormula defaultData = new DataFormula( "[Select]" ) {
		
		@Override
		public void onSelected() {
		    BaseGraphApp.this.reconnect( null );
		}
	    };
	    addDataFormulae( defaultData );
	    this.cboSelectData.setValue( defaultData );
	    
	    //watches for when the user selects a new data formula
	    this.cboSelectData.valueProperty().addListener( new ChangeListener< DataFormula >() {

		@Override
		public void changed(ObservableValue<? extends DataFormula> observable, DataFormula oldValue, DataFormula newValue) {
		    newValue.onSelected();
		}
	    });
	}
	
	/**
	 * Adds the given data formulae objects to the list of acceptable
	 * data formulae.
	 * 
	 * @param f the data formula objects to add
	 */
	final public void addDataFormulae( DataFormula... f ) {
	    this.cboSelectData.getItems().addAll( f );
	}
	
	/**
	 * Reselects the currently selected data formula and reconnects to the 
	 * data source using this data formula.
	 */
	public void reselect() {
	    this.cboSelectData.getValue().onSelected();
	}
    }
    
    /**
     * Panel for displaying any messages that may be received from the data
     * source.
     */
    private class MessagePanel extends BorderPane {
	
    }
}
