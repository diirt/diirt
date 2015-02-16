/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.scatterGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.ScatterGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.ScatterGraph2DRendererUpdate;

/**
 * Creates scatter graphs.
 * 
 * @author mjchao
 */
public class ScatterGraphApp extends BaseGraphApp {

    private InterpolationScheme interpolationScheme = InterpolationScheme.NONE;
    
    private BaseGraphView< ScatterGraph2DRendererUpdate > scatterGraphView = new BaseGraphView< ScatterGraph2DRendererUpdate >() {

	@Override
	public Graph2DExpression<ScatterGraph2DRendererUpdate> createExpression(String dataFormula) {
	    ScatterGraph2DExpression plot = scatterGraphOf(formula(dataFormula),
			null,
			null,
			null);
	    plot.update(plot.newUpdate().interpolation(interpolationScheme));
	    return plot;
	}
	
    };
    
    private Graph2DExpression< ScatterGraph2DRendererUpdate > graph;
    
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
    public BaseGraphView getGraphView() {
	return this.scatterGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.graph = this.scatterGraphView.graph;
	this.addDataFormulae( DataFormulaFactory.fromNameAndFormula( this.scatterGraphView , "Table" , "sim://table" ) ,
			      DataFormulaFactory.fromFormula( this.scatterGraphView , "=tableOf(column(\"X\", step(0, 1)), column(\"Y\", 'sim://gaussianWaveform'))" ) ,
			      DataFormulaFactory.fromFormula( this.scatterGraphView , "=tableOf(column(\"X\", 'sim://sineWaveform(1,100,100,0.01)'), column(\"Y\", 'sim://sineWaveform(10,100,100,0.01)'))" ) ,
			      DataFormulaFactory.fromFormula( this.scatterGraphView , "=tableOf(column(\"X\", 'sim://triangleWaveform(10,100,100,0.01)'), column(\"Y\", 'sim://triangleWaveform(20,100,100,0.01)'))" )
	);
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
