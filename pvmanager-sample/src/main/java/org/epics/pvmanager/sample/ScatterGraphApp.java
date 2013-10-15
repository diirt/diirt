/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sample;

import org.epics.graphene.InterpolationScheme;
import org.epics.pvmanager.graphene.ScatterGraph2DExpression;
import static org.epics.pvmanager.formula.ExpressionLanguage.formula;
import static org.epics.pvmanager.graphene.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class ScatterGraphApp extends BaseGraphApp {
    private InterpolationScheme interpolationScheme = InterpolationScheme.NONE;

    public InterpolationScheme getInterpolationScheme() {
        return interpolationScheme;
    }

    public void setInterpolationScheme(InterpolationScheme interpolationScheme) {
        this.interpolationScheme = interpolationScheme;
        if (plot != null) {
            plot.update(plot.newUpdate().interpolation(interpolationScheme));
        }
    }

    @Override
    protected ScatterGraph2DExpression createExpression(String dataFormula) {
        ScatterGraph2DExpression plot = scatterGraphOf(formula(dataFormula),
                    null,
                    null,
                    null);
        plot.update(plot.newUpdate().interpolation(interpolationScheme));
        return plot;
    }

    @Override
    protected void openConfigurationDialog() {
        ScatterGraphDialog dialog = new ScatterGraphDialog(new javax.swing.JFrame(), true, this);
        dialog.setTitle("Configure...");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        main(ScatterGraphApp.class);
    }
    
}
