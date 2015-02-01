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
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.diirt.graphene.Graph2DRendererUpdate;

/**
 *
 * @author Mickey
 */
public class BaseGraphApp< T extends Graph2DRendererUpdate< T > > extends Application {
    
    final private DataSelectionPanel pnlData = new DataSelectionPanel();
    final private BaseGraphView pnlGraph = new BaseGraphView();
    final private ErrorMessagePanel pnlError = new ErrorMessagePanel();
    @Override
    public void start( Stage stage ) throws Exception {
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
	
	addDataFormulae( DataFormulaFactory.sineWave( this.pnlGraph ) , DataFormulaFactory.gaussianWaveform( this.pnlGraph ) );
    }
    
    public void reconnect( String dataForm ) {
	this.pnlGraph.reconnect( dataForm );
    }
    
    public void addDataFormulae( String... formNames ) {
	for ( String s : formNames ) {
	    DataFormula f = DataFormulaFactory.fromFormula( this.pnlGraph , s );
	    this.pnlData.addDataForms( f );
	}
    }
    
    public void addDataFormulae( DataFormula... dataForms ) {
	this.pnlData.addDataForms( dataForms );
    }
    
    private class DataSelectionPanel extends BorderPane {
	
	private ComboBox< DataFormula > cboSelectData = new ComboBox< DataFormula >();
	
	public DataSelectionPanel() {
	    this.setCenter( this.cboSelectData );
	    
	    //allow the combo box to stretch out and fill panel completely
	    this.cboSelectData.setMaxSize( Double.MAX_VALUE , Double.MAX_VALUE );
	    
	    //allow the user to select no data
	    DataFormula defaultData = new DataFormula( "[Select]" ) {
		
		@Override
		public void onSelected() {
		    reconnect( null );
		}
	    };
	    addDataForms( defaultData );
	    this.cboSelectData.setValue( defaultData );
	    
	    this.cboSelectData.valueProperty().addListener( new ChangeListener< DataFormula >() {

		@Override
		public void changed(ObservableValue<? extends DataFormula> observable, DataFormula oldValue, DataFormula newValue) {
		    newValue.onSelected();
		}
	    });
	}
	
	final public void addDataForms( DataFormula... f ) {
	    this.cboSelectData.getItems().addAll( f );
	}
    }
    
    private class ErrorMessagePanel extends BorderPane {
	
    }
    
    public static void main( String[] args ) {
	launch( args );
    }

}
