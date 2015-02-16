/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.lineGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.LineGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.LineGraph2DRendererUpdate;

/**
 * Creates line graphs
 * 
 * @author mjchao
 */
public class LineGraphApp extends BaseGraphApp {
    private InterpolationScheme interpolationScheme = InterpolationScheme.NEAREST_NEIGHBOR;
    
    final private BaseGraphView< LineGraph2DRendererUpdate > lineGraphView = new BaseGraphView< LineGraph2DRendererUpdate >() {

	@Override
	public Graph2DExpression<LineGraph2DRendererUpdate> createExpression(String dataFormula) {
	    LineGraph2DExpression plot = lineGraphOf(formula(dataFormula),
			null,
			null,
			null);
	    plot.update(plot.newUpdate().interpolation(interpolationScheme));
	    return plot;
	}
    };
    
    private Graph2DExpression< LineGraph2DRendererUpdate > graph;
    
    public InterpolationScheme getInterpolationScheme() {
        return interpolationScheme;
    }
    
    @Override
    public BaseGraphView getGraphView() {
	return this.lineGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.graph = this.lineGraphView.graph;
	this.addDataFormulae( DataFormulaFactory.fromNameAndFormula( this.lineGraphView , "Table" , "sim://table" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.lineGraphView , "Gaussian Waveform" , "sim://gaussianWaveform" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.lineGraphView , "Sine Waveform" , "sim://sineWaveform" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.lineGraphView , "Triangle Waveform" , "sim://triangleWaveform" ) ,
			      DataFormulaFactory.fromFormula( this.lineGraphView , "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))" )
	);
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
