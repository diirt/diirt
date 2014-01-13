/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import static org.epics.graphene.ColorScheme.*;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 *
 * @author sjdallst
 */
public class NLineGraphs2DRenderer extends Graph2DRenderer{
    public NLineGraphs2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth,imageHeight);
    }
    private int imageWidth,
                imageHeight;
    private ArrayList<LineGraph2DRenderer> graphList;
    private ArrayList<Graphics2D> graphicsList;
    /**
     *Supported interpolation schemes. 
     * Possible values:
     * <ul>
     *  <li>NEAREST_NEIGHBOR: Draws a line in steps. Starts at an initial point, draws that point's value
     * until it is halfway to the next point, draws a straight line upwards to the next point's value, then draws
     * a straight line to the next point.</li>
     *  <li>LINEAR: Draws lines from one point to the next in a linear fashion.</li>
     *  <li>CUBIC: Fits a cubic curve to the points plotted.</li>
     * </ul>
     */
    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    /**
     *Supported reduction schemes. Possible values:
     * <ul>
     *  <li>FIRST_MAX_MIN_LAST: plots only the first max min and last points within every four pixels along the x-axis. 
     *  To be used when there are many more points than pixels.</li>
     *  <li>NONE: No reduction scheme, all points are plotted.</li>
     * </ul>
     */
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 

    private InterpolationScheme interpolation = InterpolationScheme.LINEAR;
    public void draw( BufferedImage image, List<Point2DDataset> data){
        this.g = (Graphics2D) image.getGraphics();
        graphList = new ArrayList<LineGraph2DRenderer>();
        graphicsList = new ArrayList<Graphics2D>();
        for(Point2DDataset dataset: data){
            LineGraph2DRenderer added = new LineGraph2DRenderer(this.getImageWidth(),this.getImageHeight()/data.size());
            graphList.add(added);
        }
        for(int i = 0; i < graphList.size(); i++){
            System.out.println(i);
            Graphics2D gtemp = (Graphics2D) image.getGraphics();
            gtemp.translate(0,this.getImageHeight()/data.size()*i);
            graphList.get(i).draw(gtemp, data.get(i));
        }
    }
    @Override
    public Graph2DRendererUpdate newUpdate() {
        return new NLineGraphs2DRendererUpdate();
    }
    public void update(NLineGraphs2DRendererUpdate update) {
        super.update(update);
    }
}
