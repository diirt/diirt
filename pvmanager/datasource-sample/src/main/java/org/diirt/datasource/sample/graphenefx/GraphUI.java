/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.datasource.sample.graphenefx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Mickey
 */
public class GraphUI extends Application {
    
    @Override
    public void start( Stage stage ) {
	Group root = new Group();
	Scene scene = new Scene( root );
	GraphFx graph = new GraphFx();
	root.getChildren().add( graph );
	stage.setScene( scene );
	stage.setWidth( 500 );
	stage.setHeight( 500 );
	stage.sizeToScene();
	stage.show();
	scene.widthProperty().addListener( new ChangeListener< Number >() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		    graph.setPrefWidth( newValue.doubleValue() );
		    graph.setMaxWidth( newValue.doubleValue() );
		    graph.setMinWidth( newValue.doubleValue() );
		}
	    
	    }
	);
	
	scene.heightProperty().addListener( new ChangeListener< Number >() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		    graph.setPrefHeight( newValue.doubleValue() );
		    graph.setMaxHeight( newValue.doubleValue() );
		    graph.setMinHeight( newValue.doubleValue() );
		}
	    
	    }
	);
    }
    
    public GraphUI() {
	
    }
    
    final public static void main( String[] args ) {
	Application.launch( args );
    }
}
