/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.graphene;

import org.diirt.graphene.IntensityGraph2DRenderer;
import org.diirt.graphene.IntensityGraph2DRendererUpdate;
import org.diirt.graphene.NumberColorMap;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.IntensityGraph2DExpression;

/**
 *
 * @author carcassi
 */
public class IntensityGraphApp extends BaseGraphApp<IntensityGraph2DRendererUpdate> {

    public IntensityGraphApp() {
        dataFormulaField.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "sim://gaussianWaveform",
                    "sim://sine2DWaveform(1,50,45,100,100,0.1)",
                    "=ndArray(arrayOf(1,3,2,4,3,5), dimDisplay(3,FALSE), dimDisplay(2,FALSE))",
                    "=ndArray('13SIM1:image1:ArrayData', dimDisplay('13SIM1:cam1:ArraySizeY_RBV', TRUE), dimDisplay('13SIM1:cam1:ArraySizeX_RBV', FALSE))",
                    "=histogram2DOf(tableOf(column(\"X\", 'sim://sineWaveform(1,100,100,0.01)'), column(\"Y\", 'sim://sineWaveform(10,100,100,0.01)')), \"Y\", \"X\")",
                    "sim://square2DWaveform(1,50,45,10000,10000,0.1)"}));
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
