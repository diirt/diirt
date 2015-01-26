/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.datasource.sample.graphenefx;

import java.awt.image.BufferedImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.graphene.ExpressionLanguage.scatterGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.Graph2DResult;
import org.diirt.datasource.graphene.ScatterGraph2DExpression;
import static org.diirt.datasource.util.Executors.fxPlatform;
import org.diirt.graphene.ScatterGraph2DRendererUpdate;
import org.diirt.graphene.InterpolationScheme;
import static org.diirt.util.time.TimeDuration.ofHertz;
import org.diirt.vtype.ValueUtil;

/**
 *
 * @author Mickey
 */
public class GraphFx extends FlowPane {
    
    private BufferedImage bufferedGraphImage;
    private WritableImage graphImage;
    private ImageView viewGraph;
    
    //for reading graph data
    private PVReader< Graph2DResult > dataReader;
    private Graph2DExpression< ScatterGraph2DRendererUpdate > graph;
    
    public GraphFx( int graphWidth , int graphHeight ) {
	this.graphImage = new WritableImage( graphWidth , graphHeight );
	this.viewGraph = new ImageView();
	this.getChildren().add( viewGraph );
	
	viewGraph.fitWidthProperty().bind( this.widthProperty() );
	viewGraph.fitHeightProperty().bind( this.heightProperty() );
	
	this.widthProperty().addListener( new ChangeListener< Number >() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		    viewGraph.fitWidthProperty().bind( GraphFx.this.widthProperty() );
		}
	    }
	);
	
	this.heightProperty().addListener( new ChangeListener< Number >() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		    viewGraph.fitHeightProperty().bind( GraphFx.this.heightProperty() );
		}
	    }
	);
	
	this.viewGraph.setImage( new Image( "file:C:\\Users\\Mickey\\Documents\\SparklineGraph.png" ) );
	reconnect();
    }
    
    public void loadGraph( BufferedImage graph , WritableImage canvas ) {
	SwingFXUtils.toFXImage(graph , canvas );
	this.graphImage = canvas;
	this.viewGraph.setImage( this.graphImage );
    }
    
    protected ScatterGraph2DExpression createExpression(String dataFormula) {
        ScatterGraph2DExpression plot = scatterGraphOf(formula(dataFormula),
                    null,
                    null,
                    null);
        plot.update(plot.newUpdate().interpolation(InterpolationScheme.NONE ));
        return plot;
    }
    
    public void reconnect() {
	graph = createExpression( "=tableOf(column(\"X\", step(0, 1)), column(\"Y\", 'sim://gaussianWaveform'))" );
	
	graph.update(graph.newUpdate().imageHeight( (int)this.graphImage.getHeight() )
                .imageWidth( (int)this.graphImage.getWidth() ) );
	
        dataReader = PVManager.read(graph)
                .notifyOn(fxPlatform())
                .readListener(new PVReaderListener<Graph2DResult>() {

                    @Override
                    public void pvChanged(PVReaderEvent<Graph2DResult> event) {
                        if (dataReader.getValue() != null) {
                            BufferedImage image = ValueUtil.toImage(dataReader.getValue().getImage());
			    bufferedGraphImage = image;
                            loadGraph( image , graphImage );
                        }
                    }
                })
                .maxRate(ofHertz(50));
    }
}
