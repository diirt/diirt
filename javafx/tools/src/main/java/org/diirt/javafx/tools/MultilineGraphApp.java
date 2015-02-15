/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.multilineGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.MultilineGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.LineGraph2DRendererUpdate;

/**
 * Creates multi-line graphs
 * 
 * @author mjchao
 */
public class MultilineGraphApp extends BaseGraphApp {

    private InterpolationScheme interpolationScheme = InterpolationScheme.NEAREST_NEIGHBOR;
     
    final private BaseGraphView< LineGraph2DRendererUpdate > multilineGraphView = new BaseGraphView< LineGraph2DRendererUpdate >() {

	@Override
	public Graph2DExpression<LineGraph2DRendererUpdate> createExpression(String dataFormula) {
	    MultilineGraph2DExpression plot = multilineGraphOf(formula(dataFormula),
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

    public void setInterpolationScheme(InterpolationScheme interpolationScheme) {
        this.interpolationScheme = interpolationScheme;
        if (graph != null) {
            graph.update(graph.newUpdate().interpolation(interpolationScheme));
        }
    }
    
    @Override
    public BaseGraphView getGraphView() {
	return this.multilineGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.graph = this.multilineGraphView.graph;
	this.addDataFormulae( DataFormulaFactory.fromFormula( this.multilineGraphView , "=tableOf(column(\"Sine\", 'sim://sineWaveform(1,50,100,0.01)'), column(\"Triangle\", 'sim://triangleWaveform(2,50,100,0.01)'), column(\"Triangle\", 'sim://squareWaveform(3,50,100,0.01)'))" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.multilineGraphView , "Gaussian Waveform" , "sim://gaussianWaveform" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.multilineGraphView , "Sine Waveform" , "sim://sineWaveform" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.multilineGraphView , "Triangle Waveform" , "sim://triangleWaveform" ) ,
			      DataFormulaFactory.fromFormula( this.multilineGraphView , "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))" )
	);
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
