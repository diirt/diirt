/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.multiAxisLineGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.MultiAxisLineGraph2DExpression;
import org.diirt.graphene.InterpolationScheme;
import org.diirt.graphene.MultiAxisLineGraph2DRendererUpdate;

/**
 * Creates multi-axis line graphs
 * 
 * @author mjchao
 */
public class MultiAxisLineGraphApp extends BaseGraphApp {

    private InterpolationScheme interpolationScheme = InterpolationScheme.NEAREST_NEIGHBOR;
    private boolean separateAreas = false;
    
    final private BaseGraphView< MultiAxisLineGraph2DRendererUpdate > multiAxisLineGraphView = new BaseGraphView< MultiAxisLineGraph2DRendererUpdate >() {

	@Override
	public Graph2DExpression<MultiAxisLineGraph2DRendererUpdate> createExpression(String dataFormula) {
	    MultiAxisLineGraph2DExpression plot = multiAxisLineGraphOf(formula(dataFormula),
			null,
			null);
	    plot.update(plot.newUpdate().interpolation(interpolationScheme).separateAreas(separateAreas));
	    return plot;
	}
    };

    private Graph2DExpression< MultiAxisLineGraph2DRendererUpdate > graph;
    
    public InterpolationScheme getInterpolationScheme() {
        return interpolationScheme;
    }
    
    public void setInterpolationScheme(InterpolationScheme interpolationScheme) {
        this.interpolationScheme = interpolationScheme;
        if (graph != null) {
            graph.update(graph.newUpdate().interpolation(interpolationScheme));
        }
    }
    
    void setSeparateAreas(boolean separateAreas) {
        this.separateAreas = separateAreas;
        if (graph != null) {
            graph.update(graph.newUpdate().separateAreas(separateAreas));
        }
    }
    
    @Override
    public BaseGraphView getGraphView() {
	return this.multiAxisLineGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.graph = this.multiAxisLineGraphView.graph;
	this.addDataFormulae( DataFormulaFactory.fromFormula( this.multiAxisLineGraphView , "=tableOf(column(\"Sine\", 'sim://sineWaveform(1,50,100,0.01)'), column(\"Triangle\", 'sim://triangleWaveform(2,50,100,0.01)'), column(\"Triangle\", 'sim://squareWaveform(3,50,100,0.01)'))" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.multiAxisLineGraphView , "Gaussian Waveform" , "sim://gaussianWaveform" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.multiAxisLineGraphView , "Sine Waveform" , "sim://sineWaveform" ) ,
			      DataFormulaFactory.fromNameAndFormula( this.multiAxisLineGraphView , "Triangle Waveform" , "sim://triangleWaveform" ) ,
			      DataFormulaFactory.fromFormula( this.multiAxisLineGraphView , "=tableOf(column(\"X\", range(-5, 5)), column(\"Y\", 'sim://gaussianWaveform'))" )
	);
    }  
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
