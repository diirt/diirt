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
import static org.diirt.datasource.graphene.ExpressionLanguage.scatterGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.ScatterGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.ScatterGraph2DRendererUpdate;

/**
 *
 * @author mjchao
 */
public class ScatterGraphView extends BaseGraphView< ScatterGraph2DRendererUpdate > {

    private final Property< InterpolationScheme > interpolationScheme = new SimpleObjectProperty< InterpolationScheme >( this , "interpolationScheme" , InterpolationScheme.NONE );
    private final ConfigurationDialog defaultConfigurationDialog = new ConfigurationDialog();

    @Override
    public Graph2DExpression<ScatterGraph2DRendererUpdate> createExpression(String dataFormula) {
        ScatterGraph2DExpression plot = scatterGraphOf(formula(dataFormula),
                    null,
                    null,
                    null);
        plot.update(plot.newUpdate().interpolation(interpolationScheme.getValue()));
        return plot;
    }

    public ScatterGraphView() {
        this.interpolationScheme.addListener( new ChangeListener< InterpolationScheme >() {

            @Override
            public void changed(ObservableValue<? extends InterpolationScheme> observable, InterpolationScheme oldValue, InterpolationScheme newValue) {
                graph.update( graph.newUpdate().interpolation( newValue ) );
            }

        });

        defaultConfigurationDialog.addInterpolationSchemeListProperty( "Interpolation Scheme" , this.interpolationScheme , new InterpolationScheme[] { InterpolationScheme.NONE , InterpolationScheme.LINEAR , InterpolationScheme.CUBIC } );
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
