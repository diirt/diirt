/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.graphene.IntensityGraph2DRendererUpdate;
import static org.diirt.datasource.graphene.ExpressionLanguage.*;
import org.diirt.graphene.IntensityGraph2DRenderer;
import org.diirt.graphene.NumberColorMap;

/**
 * Creates intensity graphs.
 * 
 * @author mjchao
 */
public class IntensityGraphApp extends BaseGraphApp {

    private BaseGraphView< IntensityGraph2DRendererUpdate > intensityGraphView = new BaseGraphView< IntensityGraph2DRendererUpdate >() {
	    @Override
	    public Graph2DExpression<IntensityGraph2DRendererUpdate> createExpression(String dataFormula) {
		return intensityGraphOf( formula(dataFormula) );
	    }    
    };
    
    //we can't get this object until the <code>BubbleGraphApp</code> is created
    //since we get it from <code>bubbleGraphView</code>. Therefore, it is
    //critical that this is initialized in the start() method.
    private Graph2DExpression<IntensityGraph2DRendererUpdate> graph;

    @Override
    public BaseGraphView getGraphView() {
	return this.intensityGraphView;
    }
    
    protected void updateGraph() {
        if (graph != null) {
            update(graph);
        }
    }
    
    private NumberColorMap colorMap = IntensityGraph2DRenderer.DEFAULT_COLOR_MAP;
    private boolean drawLegend = IntensityGraph2DRenderer.DEFAULT_DRAW_LEGEND;
    
    public NumberColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(NumberColorMap colorMap) {
        this.colorMap = colorMap;
        updateGraph();
    }

    public boolean isDrawLegend() {
        return drawLegend;
    }

    public void setDrawLegend(boolean drawLegend) {
        this.drawLegend = drawLegend;
        updateGraph();
    }
    
    protected void update(Graph2DExpression<IntensityGraph2DRendererUpdate> graph) {
        graph.update(graph.newUpdate().colorMap(colorMap).drawLegend(drawLegend));
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	super.addDataFormulae( DataFormulaFactory.sineWave( this.intensityGraphView ) , 
		DataFormulaFactory.gaussianWaveform( this.intensityGraphView ) ,
		DataFormulaFactory.histogram( this.intensityGraphView ) );
	this.graph = this.intensityGraphView.graph;
    }

    public static void main( String[] args ) {
	launch( args );
    }
}
