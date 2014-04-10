/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;

import java.awt.event.MouseEvent;
import org.epics.graphene.AreaGraph2DRendererUpdate;
import org.epics.graphene.InterpolationScheme;
import org.epics.graphene.LineGraph2DRendererUpdate;
import org.epics.pvmanager.graphene.ScatterGraph2DExpression;
import static org.epics.pvmanager.formula.ExpressionLanguage.formula;
import org.epics.pvmanager.graphene.AreaGraph2DExpression;
import static org.epics.pvmanager.graphene.ExpressionLanguage.*;
import org.epics.pvmanager.graphene.HistogramGraph2DExpression;
import org.epics.pvmanager.graphene.LineGraph2DExpression;

/**
 *
 * @author carcassi
 */
public class HistogramGraphApp extends BaseGraphApp<AreaGraph2DRendererUpdate> {
    
    private boolean highlightFocusValue;

    public HistogramGraphApp() {
        dataFormulaField.setModel(new javax.swing.DefaultComboBoxModel<String>(
                new String[] { "sim://gaussianWaveform",
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
