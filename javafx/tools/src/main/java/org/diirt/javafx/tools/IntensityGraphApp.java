/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.graphene.IntensityGraph2DRendererUpdate;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;

/**
 *
 * @author Mickey
 */
public class IntensityGraphApp extends BaseGraphApp {

    private BaseGraphView< IntensityGraph2DRendererUpdate > intensityGraphView = new BaseGraphView< IntensityGraph2DRendererUpdate >() {
	    @Override
	    public Graph2DExpression<IntensityGraph2DRendererUpdate> createExpression(String dataFormula) {
		return intensityGraphOf( formula(dataFormula) );
	    }    
    };

    @Override
    public BaseGraphView getGraphView() {
	return this.intensityGraphView;
    }

    public static void main( String[] args ) {
	launch( args );
    }
}
