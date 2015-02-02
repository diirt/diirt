/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

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

abstract public class BaseGraphView< T extends Graph2DRendererUpdate< T > > extends BorderPane {

    VImageView imagePanel = new VImageView();
    
    public BaseGraphView() {
//        heightProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (graph != null) {
//                    graph.update(graph.newUpdate()
//                            .imageHeight(Math.max(1, newValue.intValue())));
//                }
//            }
//        });
//        widthProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                if (graph != null) {
//                    graph.update(graph.newUpdate()
//                            .imageWidth(Math.max(1, newValue.intValue())));
//                }
//            }
//        });
	
	//allow this panel to shrink -- for some reason, JavaFX doesn't default
	//to this
	setMinSize( 0 , 0 );
        setCenter(imagePanel);
	
	this.imagePanel.setOnMouseMoved( new EventHandler< MouseEvent >() {

	    @Override
	    public void handle(MouseEvent event) {
		onMouseMove( event );
	    }
	});
	
	//"sim://sine2DWaveform(1,50,45,100,100,0.1)"
        reconnect( null );
    }
    
    /**
     * Called whenever the mouse moves over the graph image of this view. By
     * default, this does nothing. Override this method to provide custom
     * functionality
     * 
     * @param e 
     */
    protected void onMouseMove( MouseEvent e ) {
	//do nothing by default
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private PVReader<Graph2DResult> pv;
    protected Graph2DExpression< T > graph;

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
        
        graph.update(graph.newUpdate().imageHeight(600)
                .imageWidth(600));
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
    
    abstract public Graph2DExpression< T > createExpression( String dataFormula );
    
    
}
