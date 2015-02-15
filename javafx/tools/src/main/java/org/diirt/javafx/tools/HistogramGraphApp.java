/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.histogramGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.HistogramGraph2DExpression;
import org.diirt.graphene.AreaGraph2DRendererUpdate;

/**
 * Creates histogram graphs
 * @author mjchao
 */
public class HistogramGraphApp extends BaseGraphApp {

    private boolean highlightFocusValue;
    
    final private BaseGraphView< AreaGraph2DRendererUpdate > histogramGraphView = new BaseGraphView< AreaGraph2DRendererUpdate >() {
	
	@Override
	public Graph2DExpression<AreaGraph2DRendererUpdate> createExpression(String dataFormula) {
	    HistogramGraph2DExpression plot = histogramGraphOf(formula(dataFormula));
	    plot.update(plot.newUpdate().highlightFocusValue(highlightFocusValue));
	    return plot;
	}
	
	@Override
	protected void onMouseMove(MouseEvent e) {
	    if ( graph != null ) {
		graph.update(graph.newUpdate().focusPixel( (int)e.getX() ));
	    }
	}
    };
    
    	
    public boolean isHighlightFocusValue() {
	return highlightFocusValue;
    }

    public void setHighlightFocusValue(boolean highlightFocusValue) {
	this.highlightFocusValue = highlightFocusValue;
	graph.update(graph.newUpdate().highlightFocusValue(highlightFocusValue));
    }
    
    private Graph2DExpression< AreaGraph2DRendererUpdate > graph;

    @Override
    public BaseGraphView getGraphView() {
	return this.histogramGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	graph = this.histogramGraphView.graph;
		this.addDataFormulae( DataFormulaFactory.fromFormula( this.histogramGraphView , "sim://gaussianWaveform" ) ,
				      DataFormulaFactory.fromNameAndFormula( this.histogramGraphView , "Noise" , "=histogramOf('sim://noiseWaveform')") , 
				      DataFormulaFactory.fromFormula( this.histogramGraphView , "=arrayWithBoundaries(arrayOf(1,3,2,4,3,5), range(-10,10))")
	
	);
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
