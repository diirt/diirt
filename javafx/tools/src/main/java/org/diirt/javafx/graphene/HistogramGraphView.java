/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.histogramGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.HistogramGraph2DExpression;
import org.diirt.graphene.AreaGraph2DRendererUpdate;

/**
 *
 * @author Mickey
 */
public class HistogramGraphView extends BaseGraphView< AreaGraph2DRendererUpdate > {

    private final BooleanProperty highlightFocusValue = new SimpleBooleanProperty( this , "highlightFocusValue" , false );

    private final ConfigurationDialog defaultConfigurationDialog = new ConfigurationDialog();

    @Override
    public Graph2DExpression createExpression(String dataFormula) {
        HistogramGraph2DExpression plot = histogramGraphOf(formula(dataFormula));
        plot.update(plot.newUpdate().highlightFocusValue(highlightFocusValue.getValue()));
        return plot;
    }

    public HistogramGraphView() {
        this.highlightFocusValue.addListener( new ChangeListener< Boolean >() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                graph.update( graph.newUpdate().highlightFocusValue( newValue ) );
            }
        });

        this.defaultConfigurationDialog.addBooleanProperty( "Highlight Focus" , this.highlightFocusValue );
    }

    @Override
    protected void onMouseMove(MouseEvent e) {
        if ( graph != null ) {
            graph.update(graph.newUpdate().focusPixel( (int)e.getX() ));
        }
    }

    public void setHighlightFocusValue( boolean b ) {
        this.highlightFocusValue.setValue( b );
    }

    public boolean isHighlightFocusValue() {
        return this.highlightFocusValue.getValue();
    }

    public BooleanProperty highlightFocusValueProperty() {
        return this.highlightFocusValue;
    }

    public ConfigurationDialog getDefaultConfigurationDialog() {
        return this.defaultConfigurationDialog;
    }
}
