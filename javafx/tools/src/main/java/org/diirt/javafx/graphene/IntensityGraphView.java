/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.graphene;

import java.util.Collection;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.intensityGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.graphene.IntensityGraph2DRenderer;
import org.diirt.graphene.IntensityGraph2DRendererUpdate;
import org.diirt.graphene.NumberColorMap;
import org.diirt.graphene.NumberColorMaps;

/**
 *
 * @author mjchao
 */
public class IntensityGraphView extends BaseGraphView< IntensityGraph2DRendererUpdate >{

    private final Property< NumberColorMap > colorMap = new SimpleObjectProperty< NumberColorMap >( this , "colorMap" , IntensityGraph2DRenderer.DEFAULT_COLOR_MAP );
    private final BooleanProperty drawLegend = new SimpleBooleanProperty( this , "drawLegend" , IntensityGraph2DRenderer.DEFAULT_DRAW_LEGEND );
    private final ConfigurationDialog defaultConfigurationDialog = new ConfigurationDialog();

    @Override
    public Graph2DExpression<IntensityGraph2DRendererUpdate> createExpression(String dataFormula) {
        return intensityGraphOf( formula(dataFormula) );
    }

    @Override
    protected void reconnect( String formula ) {
        super.reconnect( formula );
        updateGraph();
    }

    public IntensityGraphView() {
        this.colorMap.addListener( new ChangeListener< NumberColorMap >() {

            @Override
            public void changed(ObservableValue<? extends NumberColorMap> observable, NumberColorMap oldValue, NumberColorMap newValue) {
                if ( graph != null ) {
                    graph.update( graph.newUpdate().colorMap( newValue ) );
                }
            }

        });
        this.drawLegend.addListener( new ChangeListener< Boolean >() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                graph.update( graph.newUpdate().drawLegend( newValue ) );
            }

        });
        Collection< NumberColorMap > maps = NumberColorMaps.getRegisteredColorSchemes().values();
        NumberColorMap[] allowedMappings = new NumberColorMap[ maps.size() ];
        allowedMappings = maps.toArray( allowedMappings );
        this.defaultConfigurationDialog.addNumberColorMapListProperty( "Color Map" , colorMap , allowedMappings );
        this.defaultConfigurationDialog.addBooleanProperty( "Draw Legend" , this.drawLegend );
    }

    public NumberColorMap getColorMap() {
        return this.colorMap.getValue();
    }

    public void setColorMap( NumberColorMap map ) {
        this.colorMap.setValue( map );
        updateGraph();
    }

    public boolean isDrawLegend() {
        return drawLegend.getValue();
    }

    public void setDrawLegend(boolean drawLegend) {
        this.drawLegend.setValue( drawLegend );
        updateGraph();
    }

    protected void updateGraph() {
        if (graph != null) {
            update(graph);
        }
    }

    protected void update(Graph2DExpression<IntensityGraph2DRendererUpdate> graph) {
        graph.update(graph.newUpdate().colorMap(colorMap.getValue()).drawLegend(drawLegend.getValue()));
    }

    public ConfigurationDialog getDefaultConfigurationDialog() {
        return this.defaultConfigurationDialog;
    }
}
