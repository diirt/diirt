/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;

import org.epics.graphene.IntensityGraph2DRenderer;
import org.epics.graphene.IntensityGraph2DRendererUpdate;
import org.epics.graphene.NumberColorMap;
import org.epics.graphene.NumberColorMaps;
import static org.epics.pvmanager.formula.ExpressionLanguage.formula;
import static org.epics.pvmanager.graphene.ExpressionLanguage.*;
import org.epics.pvmanager.graphene.Graph2DExpression;
import org.epics.pvmanager.graphene.IntensityGraph2DExpression;

/**
 *
 * @author carcassi
 */
public class IntensityGraphApp extends BaseGraphApp<IntensityGraph2DRendererUpdate> {

    public IntensityGraphApp() {
        dataFormulaField.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "sim://gaussianWaveform",
                    "sim://sine2DWaveform(1,50,45,100,100,0.1)",
                    "=arrayWithBoundaries(arrayOf(1,3,2,4,3,5), range(-10,10))",
                    "=caHistogram(\"histo\")"}));
    }
    
    protected void updateGraph() {
        if (graph != null) {
            update(graph);
        }
    }
    
    protected void update(Graph2DExpression<IntensityGraph2DRendererUpdate> graph) {
        graph.update(graph.newUpdate().colorMap(colorMap).drawLegend(drawLegend));
    }

    @Override
    protected IntensityGraph2DExpression createExpression(String dataFormula) {
        IntensityGraph2DExpression plot = intensityGraphOf(formula(dataFormula));
        plot.update(plot.newUpdate().colorMap(colorMap));
        return plot;
    }
    
    private NumberColorMap colorMap = IntensityGraph2DRenderer.DEFAULT_COLOR_MAP;
    private boolean drawLegend = IntensityGraph2DRenderer.DEFAULT_DRAW_LEGEND;
    
    public NumberColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(NumberColorMap colorMap) {
        this.colorMap = colorMap;
        updateGraph();
    }

    public boolean isDrawLegend() {
        return drawLegend;
    }

    public void setDrawLegend(boolean drawLegend) {
        this.drawLegend = drawLegend;
        updateGraph();
    }
    
    @Override
    protected void openConfigurationDialog() {
        IntensityGraphDialog dialog = new IntensityGraphDialog(new javax.swing.JFrame(), true, this);
        dialog.setTitle("Configure...");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        main(IntensityGraphApp.class);
    }
    
}
