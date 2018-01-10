/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.graphene;

import java.awt.event.MouseEvent;
import org.diirt.graphene.AreaGraph2DRendererUpdate;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;
import org.diirt.datasource.graphene.HistogramGraph2DExpression;

/**
 *
 * @author carcassi
 */
public class HistogramGraphApp extends BaseGraphApp<AreaGraph2DRendererUpdate> {

    private boolean highlightFocusValue;

    public HistogramGraphApp() {
        dataFormulaField.setModel(new javax.swing.DefaultComboBoxModel<String>(
                new String[] { "sim://gaussianWaveform",
                    "=histogramOf('sim://noiseWaveform')",
                    "=arrayWithBoundaries(arrayOf(1,3,2,4,3,5), range(-10,10))",
                    "=caHistogram(\"histo\")"}));
    }

    @Override
    protected HistogramGraph2DExpression createExpression(String dataFormula) {
        HistogramGraph2DExpression plot = histogramGraphOf(formula(dataFormula));
        plot.update(plot.newUpdate().highlightFocusValue(highlightFocusValue));
        return plot;
    }

    @Override
    protected void onMouseMove(MouseEvent e) {
        graph.update(graph.newUpdate().focusPixel(e.getX()));
    }

    public boolean isHighlightFocusValue() {
        return highlightFocusValue;
    }

    public void setHighlightFocusValue(boolean highlightFocusValue) {
        this.highlightFocusValue = highlightFocusValue;
        graph.update(graph.newUpdate().highlightFocusValue(highlightFocusValue));
    }

    @Override
    protected void openConfigurationDialog() {
        HistogramGraphDialog dialog = new HistogramGraphDialog(new javax.swing.JFrame(), true, this);
        dialog.setTitle("Configure...");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        main(HistogramGraphApp.class);
    }

}
