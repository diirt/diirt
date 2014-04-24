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
    
    private GraphBuffer(BufferedImage image){
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        pixels = ((DataBufferByte)this.image.getRaster().getDataBuffer()).getData();
        hasAlphaChannel = image.getAlphaRaster() != null;
        graphics = image.createGraphics();
    }
    
    public GraphBuffer(int width, int height) {
        this(new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR));
    }
    
    public GraphBuffer(Graph2DRenderer<?> renderer) {
        this(renderer.getImageWidth(), renderer.getImageHeight());
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
    
    public BufferedImage getImage(){
        return image;
    }
    
    public Graphics2D getGraphicsContext(){
        return graphics;
    }
    
    public void drawDataImage(int xStartPoint, int yStartPoint,
            int[] xPointToDataMap, int[] yPointToDataMap,
            Cell2DDataset data, NumberColorMapInstance colorMap) {
        int previousYData = -1;
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
    
    private int xAreaStart;
    private int xAreaEnd;
    private int yAreaStart;
    private int yAreaEnd;

    /**
     * Change the portion of the buffer allocated to displaying the graph.
     * It gives the range of pixels (inclusive of both sides) where the
     * graph will be displayed.
     * <p>
     * The coordinate system is that of a standard image, where (0,0) is the
     * top left corner.
     * 
     * @param xAreaStart the first pixel on the left (inclusive)
     * @param yAreaStart the first pixel on the top (inclusive)
     * @param xAreaEnd the last pixel on the right (inclusive)
     * @param yAreaEnd the last pixel on the bottom (inclusive)
     */
    public void setGraphArea(int xAreaStart, int yAreaStart, int xAreaEnd, int yAreaEnd) {
        this.xAreaStart = xAreaStart;
        this.yAreaStart = yAreaStart;
        this.xAreaEnd = xAreaEnd;
        this.yAreaEnd = yAreaEnd;
    }
    
    private double xLeftValue;
    private double xRightValue;
    private double xLeftPixel;
    private double xRightPixel;
    private ValueScale xValueScale;

    /**
     * Sets the scaling data for the x axis assuming values are going
     * to represent cells. The minimum value is going to be positioned at the
     * left of the xMinPixel while the maximum value is going to be position
     * at the right of the xMaxPixel.
     * 
     * @param range the range to be displayed
     * @param xMinPixel the pixel corresponding to the minimum
     * @param xMaxPixel the pixel corresponding to the maximum
     * @param xValueScale the scale used to transform values to pixel
     */
    public void setXScaleAsCell(Range range, int xMinPixel, int xMaxPixel, ValueScale xValueScale) {
        xLeftValue = range.getMinimum().doubleValue();
        xRightValue = range.getMaximum().doubleValue();
        xLeftPixel = xMinPixel;
        xRightPixel = xMaxPixel + 1;
        this.xValueScale = xValueScale;
    }

    /**
     * Sets the scaling data for the x axis assuming values are going
     * to represent points. The minimum value is going to be positioned in the
     * center of the xMinPixel while the maximum value is going to be position
     * in the middle of the xMaxPixel.
     * 
     * @param range the range to be displayed
     * @param xMinPixel the pixel corresponding to the minimum
     * @param xMaxPixel the pixel corresponding to the maximum
     * @param xValueScale the scale used to transform values to pixel
     */
    public void setXScaleAsPoint(Range range, int xMinPixel, int xMaxPixel, ValueScale xValueScale) {
        xLeftValue = range.getMinimum().doubleValue();
        xRightValue = range.getMaximum().doubleValue();
        xLeftPixel = xMinPixel + 0.5;
        xRightPixel = xMaxPixel + 0.5;
        this.xValueScale = xValueScale;
    }

    /**
     * Converts the given value to the pixel position.
     * 
     * @param value the value
     * @return the pixel where the value should be mapped
     */
    public int xValueToPixel(double value) {
        return (int) xValueScale.scaleValue(value, xLeftValue, xRightValue, xLeftPixel, xRightPixel);
    }
}
