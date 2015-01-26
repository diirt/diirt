/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import javafx.scene.layout.BorderPane;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;
import static org.diirt.datasource.formula.ExpressionLanguage.*;
import org.diirt.datasource.graphene.Graph2DResult;
import org.diirt.datasource.graphene.IntensityGraph2DExpression;
import org.diirt.javafx.util.Executors;
import static org.diirt.util.time.TimeDuration.ofHertz;

public final class BaseGraphView extends BorderPane {

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
        setCenter(imagePanel);
        reconnect();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    private PVReader<Graph2DResult> pv;
    protected IntensityGraph2DExpression graph;

    protected void reconnect() {
        if (pv != null) {
            pv.close();
            imagePanel.setVImage(null);
            graph = null;
        }
        
        graph = intensityGraphOf(formula("sim://sine2DWaveform(1,50,45,100,100,0.1)"));
        
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

}
