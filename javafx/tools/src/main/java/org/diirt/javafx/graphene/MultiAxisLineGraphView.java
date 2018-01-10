/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.multiAxisLineGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.MultiAxisLineGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.MultiAxisLineGraph2DRendererUpdate;

/**
 *
 * @author mjchao
 */
public class MultiAxisLineGraphView extends BaseGraphView< MultiAxisLineGraph2DRendererUpdate > {

    private final Property< InterpolationScheme > interpolationScheme = new SimpleObjectProperty< InterpolationScheme >( this , "interpolationScheme" , InterpolationScheme.NEAREST_NEIGHBOR );
    private final BooleanProperty separateAreas = new SimpleBooleanProperty( this , "separateAreas" , false );
    private final ConfigurationDialog defaultConfigurationDialog = new ConfigurationDialog();

    @Override
    public Graph2DExpression<MultiAxisLineGraph2DRendererUpdate> createExpression(String dataFormula) {
            MultiAxisLineGraph2DExpression plot = multiAxisLineGraphOf(formula(dataFormula),
                        null,
                        null);
            plot.update(plot.newUpdate().interpolation(interpolationScheme.getValue()).separateAreas(separateAreas.getValue()));
            return plot;
    }

    public MultiAxisLineGraphView() {

        this.interpolationScheme.addListener( new ChangeListener< InterpolationScheme >() {

            @Override
            public void changed(ObservableValue<? extends InterpolationScheme> observable, InterpolationScheme oldValue, InterpolationScheme newValue) {
                graph.update( graph.newUpdate().interpolation( newValue ) );
            }

        });

        this.separateAreas.addListener( new ChangeListener< Boolean >() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                graph.update( graph.newUpdate().separateAreas( newValue ) );
            }

        });

        defaultConfigurationDialog.addInterpolationSchemeListProperty( "Interpolation Scheme" , this.interpolationScheme , new InterpolationScheme[] { InterpolationScheme.NEAREST_NEIGHBOR , InterpolationScheme.LINEAR , InterpolationScheme.CUBIC } );
        defaultConfigurationDialog.addBooleanProperty( "Separate Areas" , this.separateAreas );
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

    public void setSeparateAreas( boolean separateAreas ) {
        this.separateAreas.setValue( separateAreas );
    }

    public boolean getSeparateAreas() {
        return this.separateAreas.getValue();
    }

    public BooleanProperty separateAreasProperty() {
        return this.separateAreas;
    }

    public ConfigurationDialog getDefaultConfigurationDialog() {
        return this.defaultConfigurationDialog;
    }

}
