/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.graphene;

import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.MultiAxisLineGraph2DRendererUpdate;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;
import org.diirt.datasource.graphene.MultiAxisLineGraph2DExpression;

/**
 *
 * @author carcassi
 */
public class MultiAxisLineGraphApp extends BaseGraphApp<MultiAxisLineGraph2DRendererUpdate> {
    private InterpolationScheme interpolationScheme = InterpolationScheme.NEAREST_NEIGHBOR;
    private boolean separateAreas = false;

    public MultiAxisLineGraphApp() {
        dataFormulaField.setModel(new javax.swing.DefaultComboBoxModel<String>(
                new String[] { "=tableOf(column(\"Sine\", 'sim://sineWaveform(1,50,100,0.01)'), column(\"Triangle\", 'sim://triangleWaveform(2,50,100,0.01)'), column(\"Triangle\", 'sim://squareWaveform(3,50,100,0.01)'))",
                    "sim://gaussianWaveform",
                    "sim://sineWaveform",
                    "sim://triangleWaveform",
                    "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))"}));
    }

    public InterpolationScheme getInterpolationScheme() {
        return interpolationScheme;
    }

    public void setInterpolationScheme(InterpolationScheme interpolationScheme) {
        this.interpolationScheme = interpolationScheme;
        if (graph != null) {
            graph.update(graph.newUpdate().interpolation(interpolationScheme));
        }
    }

    @Override
    protected MultiAxisLineGraph2DExpression createExpression(String dataFormula) {
        MultiAxisLineGraph2DExpression plot = multiAxisLineGraphOf(formula(dataFormula),
                    null,
                    null);
        plot.update(plot.newUpdate().interpolation(interpolationScheme).separateAreas(separateAreas));
        return plot;
    }

    @Override
    protected void openConfigurationDialog() {
        MultiAxisLineGraphDialog dialog = new MultiAxisLineGraphDialog(new javax.swing.JFrame(), true, this);
        dialog.setTitle("Configure...");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        main(MultiAxisLineGraphApp.class);
    }

    void setSeparateAreas(boolean separateAreas) {
        this.separateAreas = separateAreas;
        if (graph != null) {
            graph.update(graph.newUpdate().separateAreas(separateAreas));
        }
    }

}
