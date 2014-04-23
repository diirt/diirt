/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
/**
 *
 * @author carcassi, sjdallst, asbarber, jkfeng
 */
public class GraphBuffer {
    
    private final BufferedImage image;
    private final Graphics2D graphics;
    private final byte[] pixels;
    private final boolean hasAlphaChannel;
    private final int width, height;
    
    public GraphBuffer(BufferedImage image){
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        pixels = ((DataBufferByte)this.image.getRaster().getDataBuffer()).getData();
        hasAlphaChannel = image.getAlphaRaster() != null;
        graphics = image.createGraphics();
    }
    
    public void setPixel(int x, int y, int color){
        if(hasAlphaChannel){
            pixels[y*image.getWidth()*4 + x*4 + 3] = (byte)(color >> 24 & 0xFF);
            pixels[y*image.getWidth()*4 + x*4 + 0] = (byte)(color >> 0 & 0xFF);
            pixels[y*image.getWidth()*4 + x*4 + 1] = (byte)(color >> 8 & 0xFF);
            pixels[y*image.getWidth()*4 + x*4 + 2] = (byte)(color >> 16 & 0xFF);
        }
        else{
            pixels[y*image.getWidth()*4 + x*4 + 0] = (byte)(color >> 0 & 0xFF);
            pixels[y*image.getWidth()*4 + x*4 + 1] = (byte)(color >> 8 & 0xFF);
            pixels[y*image.getWidth()*4 + x*4 + 2] = (byte)(color >> 16 & 0xFF);
        }
    }
    
    public BufferedImage getBufferedImage(){
        return image;
    }
    
    public Graphics2D getGraphicsContext(){
        return graphics;
    }
    
    public void drawDataImage(int xStartPoint, int yStartPoint,
            int[] xPointToDataMap, int[] yPointToDataMap,
            Cell2DDataset data, NumberColorMapInstance colorMap) {
        int previousYData = -1;
        int previousXData = -1;
        for (int yOffset = 0; yOffset < yPointToDataMap.length; yOffset++) {
            int yData = yPointToDataMap[yOffset];
            if (yData != previousYData) {
                for (int xOffset = 0; xOffset < xPointToDataMap.length; xOffset++) {
                    int xData = xPointToDataMap[xOffset];
                    int rgb = colorMap.colorFor(data.getValue(xData, yData));
                    if(hasAlphaChannel){
                        pixels[(yStartPoint + yOffset)*width*4 + 4*(xStartPoint + xOffset) + 0] = (byte)(rgb >> 24 & 0xFF);
                        pixels[(yStartPoint + yOffset)*width*4 + 4*(xStartPoint + xOffset) + 1] = (byte)(rgb & 0xFF);
                        pixels[(yStartPoint + yOffset)*width*4 + 4*(xStartPoint + xOffset) + 2] = (byte)(rgb >> 8 & 0xFF);
                        pixels[(yStartPoint + yOffset)*width*4 + 4*(xStartPoint + xOffset) + 3] = (byte)(rgb >> 16 & 0xFF);
                    } else {
                        pixels[(yStartPoint + yOffset)*width*3 + 3*(xStartPoint + xOffset) + 0] = (byte)(rgb & 0xFF);
                        pixels[(yStartPoint + yOffset)*width*3 + 3*(xStartPoint + xOffset) + 1] = (byte)((rgb >> 8 & 0xFF) );
                        pixels[(yStartPoint + yOffset)*width*3 + 3*(xStartPoint + xOffset) + 2] = (byte)((rgb >> 16 & 0xFF));
                    }
                }
            } else {
                if (hasAlphaChannel) {
                    System.arraycopy(pixels, (yStartPoint + yOffset - 1)*width*4 + 4*xStartPoint,
                            pixels, (yStartPoint + yOffset)*width*4 + 4*xStartPoint, xPointToDataMap.length*4);
                } else {
                    System.arraycopy(pixels, (yStartPoint + yOffset - 1)*width*3 + 3*xStartPoint,
                            pixels, (yStartPoint + yOffset)*width*3 + 3*xStartPoint, xPointToDataMap.length*3);
                }
            }
            previousYData = yData;
        }
    }
}
