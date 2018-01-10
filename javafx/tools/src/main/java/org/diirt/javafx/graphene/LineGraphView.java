/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.lineGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.LineGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.LineGraph2DRendererUpdate;

/**
 *
 * @author Mickey
 */
public class LineGraphView extends BaseGraphView< LineGraph2DRendererUpdate > {

    private final Property< InterpolationScheme > interpolationScheme = new SimpleObjectProperty< InterpolationScheme >( this , "interpolationScheme" , InterpolationScheme.NEAREST_NEIGHBOR );
    private final ConfigurationDialog defaultConfigurationDialog = new ConfigurationDialog();

    @Override
    public Graph2DExpression<LineGraph2DRendererUpdate> createExpression(String dataFormula) {
        LineGraph2DExpression plot = lineGraphOf(formula(dataFormula),
                    null,
                    null,
                    null);
        plot.update(plot.newUpdate().interpolation(interpolationScheme.getValue()));
        return plot;
    }

    public LineGraphView() {
        this.interpolationScheme.addListener( new ChangeListener< InterpolationScheme >() {

            @Override
            public void changed(ObservableValue<? extends InterpolationScheme> observable, InterpolationScheme oldValue, InterpolationScheme newValue) {
                graph.update( graph.newUpdate().interpolation( newValue ) );
            }

        });

        this.defaultConfigurationDialog.addInterpolationSchemeListProperty( "Interpolation Scheme" , this.interpolationScheme , new InterpolationScheme[]{ InterpolationScheme.NEAREST_NEIGHBOR , InterpolationScheme.LINEAR , InterpolationScheme.CUBIC } );
    }

    public void setInterpolationScheme( InterpolationScheme scheme ) {
        this.interpolationScheme.setValue( scheme );
    }

    public InterpolationScheme getInterpolationScheme() {
        return this.interpolationScheme.getValue();
    }

    public Property< InterpolationScheme > interpolationSchemeProperty() {
        return this.interpolationScheme;
    }

    public ConfigurationDialog getDefaultConfigurationDialog() {
        return this.defaultConfigurationDialog;
    }
}
