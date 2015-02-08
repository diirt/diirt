/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListNumber;

/**
 * Renderer for a line graph.
 *
 * @author carcassi
 */
public class LineTimeGraph2DRenderer extends TemporalGraph2DRenderer<LineTimeGraph2DRendererUpdate> {

    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(
            InterpolationScheme.NEAREST_NEIGHBOR, 
            InterpolationScheme.PREVIOUS_VALUE,
            InterpolationScheme.LINEAR,
            InterpolationScheme.CUBIC);
    
    @Override
    public LineTimeGraph2DRendererUpdate newUpdate() {
        return new LineTimeGraph2DRendererUpdate();
    }

    private InterpolationScheme interpolation = InterpolationScheme.NEAREST_NEIGHBOR;

    /**
     * Creates a new line graph renderer.
     * 
     * @param imageWidth the graph width
     * @param imageHeight the graph height
     */
    public LineTimeGraph2DRenderer(int imageWidth, int imageHeight) {
        super(imageWidth, imageHeight);
    }

    /**
     * The current interpolation used for the line.
     * 
     * @return the current interpolation
     */
    public InterpolationScheme getInterpolation() {
        return interpolation;
    }
    
    @Override
    public void update(LineTimeGraph2DRendererUpdate update) {
        super.update(update);
        if (update.getInterpolation() != null) {
            interpolation = update.getInterpolation();
        }
    }

    /**
     * Draws the graph on the given graphics context.
     * 
     * @param g the graphics on which to display the data
     * @param data the data to display
     */
    public void draw(Graphics2D g, TimeSeriesDataset data) {
        this.g = g;
        
        calculateRanges(data.getStatistics().getRange(), data.getTimeInterval());
        calculateGraphArea();
        drawBackground();
        drawGraphArea();
        
        ListNumber xValues = data.getNormalizedTime();
        ListNumber yValues = data.getValues();
	
	//if necessary, extend the last data point to the end of the graph, 
	//so that we don't have a random empty gap with no graph
	if ( xValues.getDouble( xValues.size()-1 ) == 1.0 ) {
	    if ( super.getAggregatedTimeInterval().getEnd().compareTo( super.getPlotTimeInterval().getEnd() ) < 0 ) {
		
		//Please see TemporalGraph2DRenderer.drawValueLine(...) as to why
		//this multiplier is here
		double multiplier = (double)(super.getAggregatedTimeInterval().getEnd().durationFrom( super.getAggregatedTimeInterval().getStart() ).toNanosLong()) / super.getPlotTimeInterval().getEnd().durationFrom( super.getPlotTimeInterval().getStart() ).toNanosLong();
		
		//to extend the last data point to the end of the graph, we need
		//to know what the normalized value is for the pixel on the right
		//border. Let this value be x. Conveniently, multiplier scales these
		//x values of the graph's x axis range to x values in the dataset's
		//x range. If x * multiplier = 1.0, where 1.0 is the normalized
		//value of the leftmost point in the dataset's x axis range, then 1.0/multiplier will give us
		//x, the normalized value of the leftmost point int he graph's 
		//x axis range.
		double lastX = 1.0 / multiplier;
		xValues = ListDouble.concatenate( xValues , new ArrayDouble( new double[] {lastX} ) );
		double lastY = yValues.getDouble( yValues.size()-1 );
		yValues = ListDouble.concatenate( yValues , new ArrayDouble( new double[] {lastY} ) );
	    }
	}

        setClip(g);
        g.setColor(Color.BLACK);
        drawValueLine(xValues, yValues, interpolation);
    }
}
