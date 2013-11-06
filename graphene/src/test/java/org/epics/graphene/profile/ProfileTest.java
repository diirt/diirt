package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.epics.graphene.Point2DDataset;
import org.epics.graphene.SparklineGraph2DRenderer;
import org.epics.graphene.SparklineGraph2DRendererUpdate;

/**
 *
 * @author Aaron
 */
public class ProfileTest extends ProfileGraph2D<SparklineGraph2DRenderer, Point2DDataset> {
    
    @Override
    protected Point2DDataset getDataset() {
        return ProfileGraph2D.makePoint2DData(1000);
    }
    @Override
    protected SparklineGraph2DRenderer getRenderer(int imageWidth, int imageHeight) {
        SparklineGraph2DRenderer renderer = new SparklineGraph2DRenderer(imageWidth, imageHeight);
        renderer.update(new SparklineGraph2DRendererUpdate());
        
        return renderer;
    }
    @Override
    protected void render(SparklineGraph2DRenderer renderer, Point2DDataset dataset) {
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        renderer.draw(graphics, dataset);    
    }


    public static void main(String[] args){
        ProfileTest profiler = new ProfileTest();
        profiler.profile();
        profiler.printStatistics();
    }    
}
