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
 *
 * @author Mickey
 */
abstract public class BaseGraphApp extends Application {
    
    final private DataSelectionPanel pnlData = new DataSelectionPanel();
    private BaseGraphView pnlGraph;
    final private ErrorMessagePanel pnlError = new ErrorMessagePanel();
    
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
    
    public void reconnect() {
	this.pnlData.reselect();
    }
    
    public void reconnect( String dataForm ) {
	this.pnlGraph.reconnect( dataForm );
    }
    
    public void addDataFormulae( String... formNames ) {
	for ( String s : formNames ) {
	    DataFormula f = DataFormulaFactory.fromFormula( this.pnlGraph , s );
	    this.pnlData.addDataFormulae( f );
	}
    }
    
    void addDataFormulae( DataFormula... dataForms ) {
	this.pnlData.addDataFormulae( dataForms );
    }
    
    private class DataSelectionPanel extends BorderPane {
	
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
	    
	    this.cboSelectData.valueProperty().addListener( new ChangeListener< DataFormula >() {

		@Override
		public void changed(ObservableValue<? extends DataFormula> observable, DataFormula oldValue, DataFormula newValue) {
		    newValue.onSelected();
		}
	    });
	}
	
	final public void addDataFormulae( DataFormula... f ) {
	    this.cboSelectData.getItems().addAll( f );
	}
	
	public void reselect() {
	    this.cboSelectData.getValue().onSelected();
	}
    }
    
    private class ErrorMessagePanel extends BorderPane {
	
    }
}
