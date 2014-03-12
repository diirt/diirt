/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;

import org.epics.graphene.BubbleGraph2DRendererUpdate;
import org.epics.graphene.InterpolationScheme;
import org.epics.graphene.ScatterGraph2DRendererUpdate;
import org.epics.pvmanager.graphene.ScatterGraph2DExpression;
import static org.epics.pvmanager.formula.ExpressionLanguage.formula;
import org.epics.pvmanager.graphene.BubbleGraph2DExpression;
import static org.epics.pvmanager.graphene.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class BubbleGraphApp extends BaseGraphApp<BubbleGraph2DRendererUpdate> {

    public BubbleGraphApp() {
        dataFormulaField.setModel(new javax.swing.DefaultComboBoxModel<String>(
                new String[] { "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'), column(\"SIZE\", 'sim://gaussianWaveform'))"}));
    }

    @Override
    protected BubbleGraph2DExpression createExpression(String dataFormula) {
        BubbleGraph2DExpression plot = bubbleGraphOf(formula(dataFormula),
                    null,
                    null,
                    null,
                    null);
        return plot;
    }

    @Override
    protected void openConfigurationDialog() {
        BubbleGraphDialog dialog = new BubbleGraphDialog(new javax.swing.JFrame(), true, this);
        dialog.setTitle("Configure...");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        main(BubbleGraphApp.class);
    }
    
}
