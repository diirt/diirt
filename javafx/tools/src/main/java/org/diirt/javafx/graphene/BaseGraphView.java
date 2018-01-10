/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.diirt.javafx.tools.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * <p>
 * To produce a custom graph view, e.g. a <code>BubbleGraphView</code>, you must
 * first extends <code>BaseGraphView</code>. You must define the createExpression()
 * method which will be sent to <code>PVManager</code> to generate graph data. If
 * there are any properties relevant to the graph, you must create those
 * property objects, and implement change listeners that handle what happens when
 * these graph properties change. For example, if the interpolation scheme changes,
 * you will need your listener to update the graph to use the new interpolation scheme.
 * Finally, you may also override onMouseMove() if you wish to handle mouse events
 * for the graph.
 * <p>
 * To summarize, in creating a custom graph view, there are three relevant things to be defined:
 * <ul>
 *  <li> <code>createExpression()</code> (mandatory)
 *  <li> Any graph properties you desire and their getters and setters
 *  <li> <code>onMouseMoved()</code> if you desire
 * </ul>
 * <p>
 * For <code>BaseGraphApp</code> demonstrations, we have also moved
 * configuration dialogs to be handled by the <code>BaseGraphView</code>. If
 * there are any properties that the user should be allowed to configure,
 * also be sure to add those to a <code>ConfigurationDialog</code> when you
 * extend <code>BaseGraphView</code>.
 *
 * @author mjchao, carcassi
 * @param <T> the type of renderer object used to create the graphs displayed
 * in by this graph view
 */
abstract public class BaseGraphView< T extends Graph2DRendererUpdate< T > > extends BorderPane {

    private final VImageView imagePanel = new VImageView();
    private final StringProperty formula = new SimpleStringProperty("");

    /**
     * Stores the last exception that was reported by PVManager when it tried
     * to graph
     */
    private final Property<Exception> lastException = new SimpleObjectProperty<>();

    public BaseGraphView() {

        //allow this panel to shrink -- for some reason, JavaFX doesn't default
        //to this
        setMinSize( 0 , 0 );
        setCenter(imagePanel);

        //watch for mouse movements, if necessary
        this.imagePanel.setOnMouseMoved(this::onMouseMove);

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

        this.formula.addListener( new ChangeListener< String >() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                reconnect( newValue );
            }
        });
    }

    /**
     * Sets the formula for the data this graph will display
     *
     * @param formula a data formula for PVManager
     */
    public void setFormula( String formula ) {
        this.formula.setValue( formula );
    }

    /**
     * @return the data formula currently being displayed on the graph
     */
    public String getFormula() {
        return this.formula.getValue();
    }

    public StringProperty formulaProperty() {
        return this.formula;
    }

    public ReadOnlyProperty< Exception > lastExceptionProperty() {
        return this.lastException;
    }

    /**
     * @return the last exception that was reported by PVManager when it tried
     * to graph. It is null, if PVManager did not report an exception
     */
    public Exception getlastException() {
        return this.lastException.getValue();
    }

    /**
     * Called whenever the mouse moves over the graph image of this view. By
     * default, this does nothing. Override this method to provide custom
     * functionality.
     *
     * @param e the event associated with the mouse movement
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
    Graph2DExpression< T > graph;

    /**
     * Redraws the graph
     */
    protected void reconnect() {
        reconnect( this.getFormula() );
    }
    /**
     * Sends the given data formula to the data source and asks it for data.
     * The received data is then graphed on this <code>BaseGraphView</code>.
     *
     * @param dataFormula the data formula to use
     */
    protected void reconnect( String dataFormula ) {

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
                        lastException.setValue( pv.lastException() );
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
