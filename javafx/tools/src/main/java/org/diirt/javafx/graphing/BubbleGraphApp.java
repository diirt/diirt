/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import org.diirt.javafx.tools.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.formula.ExpressionLanguage.formulaArg;
import org.diirt.datasource.graphene.BubbleGraph2DExpression;
import static org.diirt.datasource.graphene.ExpressionLanguage.bubbleGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.graphene.BubbleGraph2DRendererUpdate;

/**
 * Creates bubble graphs.
 * 
 * @author mjchao
 */
public class BubbleGraphApp extends BaseGraphApp {
    
    final private BubbleGraphView bubbleGraphView = new BubbleGraphView();

    @Override
    public BaseGraphView getGraphView() {
	return this.bubbleGraphView;
    }
    
    @Override
    public void start( Stage stage ) throws Exception {
	super.start( stage );
	this.addDataFormulae( "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'), column(\"SIZE\", 'sim://gaussianWaveform'), column(\"COLOR\", 'sim://sineWaveform'))",
                "=tableOf(column(\"X\", range(-10,10)), column(\"Y\", 'sim://noiseWaveform'))",
                "=tableOf(column(\"X\", arrayOf(2,3,4,3,2,1,0,1)), column(\"Y\", arrayOf(0,1,2,3,4,3,2,1)), column(\"SIZE\", arrayOf(5,4,3,2,1,2,3,4)), column(\"SIZE\", arrayOf(\"A\",\"A\",\"B\",\"B\",\"B\",\"B\",\"A\",\"A\")))",
                "=tableOf(column(\"X\", arrayOf(1,2,3,4,5)), column(\"Y\", arrayOf(3,1,4,2,5)), column(\"NAMES\", arrayOf(\"A\", \"A\", \"A\", \"B\", \"B\")))"
	
	);
    }
    
    final public static void main( String[] args ) {
	launch( args );
    }
}
