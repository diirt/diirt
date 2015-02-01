/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.application.Application;
import javafx.scene.Scene;
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
	
    }
    
    private class DataSelectionPanel extends FlowPane {
	
    }
    
    private class ErrorMessagePanel extends FlowPane {
	
    }
    
    public static void main( String[] args ) {
	launch( args );
    }

}
