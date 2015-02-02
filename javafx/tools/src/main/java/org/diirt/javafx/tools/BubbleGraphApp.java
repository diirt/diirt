/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.tools;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.formula.ExpressionLanguage.formulaArg;
import org.diirt.datasource.graphene.BubbleGraph2DExpression;
import static org.diirt.datasource.graphene.ExpressionLanguage.bubbleGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.graphene.BubbleGraph2DRendererUpdate;

/**
 *
 * @author Mickey
 */
public class BubbleGraphApp extends BaseGraphApp {
    
    private String xColumn = null;
    private String yColumn = null;
    private String sizeColumn = null;
    private String colorColumn = null;
    private boolean highlightFocusValue;
    
    private Graph2DExpression<BubbleGraph2DRendererUpdate> graph;
    
    final private BaseGraphView< BubbleGraph2DRendererUpdate > bubbleGraphView = new BaseGraphView< BubbleGraph2DRendererUpdate >() {

	@Override
	public Graph2DExpression<BubbleGraph2DRendererUpdate> createExpression(String dataFormula) {
	    BubbleGraph2DExpression plot = bubbleGraphOf(formula(dataFormula),
		formulaArg(xColumn),
		formulaArg(yColumn),
		formulaArg(sizeColumn),
		formulaArg(colorColumn));
	    return plot;
	}
	
	@Override
	protected void onMouseMove( MouseEvent e ) {
	    if ( graph != null ) {
		graph.update(graph.newUpdate().focusPixel( (int)e.getX() , (int)e.getY()));
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

    public String getXColumn() {
        return xColumn;
    }

    public void setXColumn(String xColumn) {
        this.xColumn = xColumn;
        reconnect();
    }

    public String getYColumn() {
        return yColumn;
    }

    public void setYColumn(String yColumn) {
        this.yColumn = yColumn;
        reconnect();
    }

    public String getSizeColumn() {
        return sizeColumn;
    }

    public void setSizeColumn(String sizeColumn) {
        this.sizeColumn = sizeColumn;
        reconnect();
    }

    public String getColorColumn() {
        return colorColumn;
    }

    public void setColorColumn(String colorColumn) {
        this.colorColumn = colorColumn;
        reconnect();
    }

    @Override
    public BaseGraphView getGraphView() {
	return this.bubbleGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.graph = this.bubbleGraphView.graph;
	this.addDataFormulae( DataFormulaFactory.fromFormula( this.bubbleGraphView , "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'), column(\"SIZE\", 'sim://gaussianWaveform'), column(\"COLOR\", 'sim://sineWaveform'))" ) ,
			      DataFormulaFactory.fromFormula( this.bubbleGraphView , "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'))" )
	
	);
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
