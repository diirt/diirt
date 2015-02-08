/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.Graph2DResult;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.javafx.util.Executors;
import static org.diirt.util.time.TimeDuration.ofHertz;

/**
 * Displays a graph, handles its resizing, and handles its connection
 * with a data source.
 * 
 * @author mjchao, carcassi
 * @param <T> the type of renderer object used to create the graphs displayed
 * in by this graph view
 */
abstract public class BaseGraphView< T extends Graph2DRendererUpdate< T > > extends BorderPane {

    VImageView imagePanel = new VImageView();
    
    public BaseGraphView() {
	
	//allow this panel to shrink -- for some reason, JavaFX doesn't default
	//to this
	setMinSize( 0 , 0 );
        setCenter(imagePanel);
	
	//watch for mouse movements, if necessary
	this.imagePanel.setOnMouseMoved( new EventHandler< MouseEvent >() {

	    @Override
	    public void handle(MouseEvent event) {
		onMouseMove( event );
	    }
	});
	
	this.imagePanel.widthProperty().addListener(new ChangeListener< Number >() {

	    @Override
	    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		if ( graph != null ) {
		    graph.update(graph.newUpdate().imageWidth(Math.max( 1 , newValue.intValue() ) ));
		}
	    }
	});
	
	this.imagePanel.heightProperty().addListener( new ChangeListener< Number >() {

	    @Override
	    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		if ( graph != null ) {
		    graph.update(graph.newUpdate().imageHeight(Math.max(1 , newValue.intValue())));
		}
	    }
	    
	});
	
	//"sim://sine2DWaveform(1,50,45,100,100,0.1)"
        reconnect( null );
    }
    
    /**
     * Called whenever the mouse moves over the graph image of this view. By
     * default, this does nothing. Override this method to provide custom
     * functionality.
     * 
     * @param e 
     */
    protected void onMouseMove( MouseEvent e ) {
	//do nothing by default
    }
    
    /**
     * Reads from the data source.
     */
    private PVReader<Graph2DResult> pv;
    
    /**
     * An expression to be sent to the data source to receive data back.
     */
    protected Graph2DExpression< T > graph;

    /**
     * Sends the given data formula to the data source and asks it for data. 
     * The received data is then graphed on this <code>BaseGraphView</code>.
     * 
     * @param dataFormula the data formula to use
     */
    final protected void reconnect( String dataFormula ) {
	
        if (pv != null) {
            pv.close();
            imagePanel.setVImage(null);
            graph = null;
        }
	
	if ( dataFormula == null ) {
	    return;
	}
        
        graph = createExpression( dataFormula );
        
        graph.update(graph.newUpdate().imageHeight(Math.max(1 , imagePanel.heightProperty().intValue()))
                .imageWidth(Math.max( 1 , imagePanel.widthProperty().intValue() ) ));
        pv = PVManager.read(graph)
                .notifyOn(Executors.javaFXAT())
                .readListener(new PVReaderListener<Graph2DResult>() {

                    @Override
                    public void pvChanged(PVReaderEvent<Graph2DResult> event) {
                        if (pv.getValue() != null) {
                            imagePanel.setVImage(pv.getValue().getImage());
                        }
                    }
                })
                .maxRate(ofHertz(100));
    }
    
    /**
     * Creates an expression that the data source can understand from the
     * provided data formula.
     * 
     * @param dataFormula the data formula to use
     * @return an expression that the data source can understand
     */
    abstract public Graph2DExpression< T > createExpression( String dataFormula );
    
    
}
