/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.datasource.sample.graphenefx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Mickey
 */
public class GraphFx extends FlowPane {
    
    public GraphFx() {
	Image graph = new Image( "file:C:\\Users\\Mickey\\Documents\\SparklineGraph.png" );
	ImageView v = new ImageView();
	v.setImage( graph );
	this.getChildren().add( v );
	
	v.fitWidthProperty().bind( this.widthProperty() );
	v.fitHeightProperty().bind( this.heightProperty() );
	
	this.widthProperty().addListener( new ChangeListener< Number >() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		    v.fitWidthProperty().bind( GraphFx.this.widthProperty() );
		}
	    }
	);
	
	this.heightProperty().addListener( new ChangeListener< Number >() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		    v.fitHeightProperty().bind( GraphFx.this.heightProperty() );
		}
	    }
	);
    }
}
