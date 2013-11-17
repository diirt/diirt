/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Color;

/**
 *
 * @author carcassi
 */
public class ValueColorSchemes {

    public static ValueColorScheme grayScale(final Range range) {
        return singleRangeGradient(range, Color.BLACK, Color.WHITE, Color.RED);
    }
    
    public static ValueColorScheme jetScale(final Range range) {
        return quintipleRangeGradient(range, new Color(0,0,138), new Color(0,255,255),new Color(255,255,0),new Color(255,0,0),new Color(138,0,0), Color.BLACK);
    }
    
    public static ValueColorScheme singleRangeGradient(final Range range, final Color minValueColor, final Color maxValueColor, final Color nanColor) {
        return new ValueColorScheme() {

            @Override
            public int colorFor(double value) {
                if (Double.isNaN(value)) {
                    return nanColor.getRGB();
                }
                
                double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue(), range.getMaximum().doubleValue());
                normalValue = Math.min(normalValue, 1.0);
                normalValue = Math.max(normalValue, 0.0);
                int alpha = 255;
                int red = (int) (minValueColor.getRed() + (maxValueColor.getRed() - minValueColor.getRed()) * normalValue);
                int green = (int) (minValueColor.getGreen() + (maxValueColor.getGreen() - minValueColor.getGreen()) * normalValue);
                int blue = (int) (minValueColor.getBlue() + (maxValueColor.getBlue() - minValueColor.getBlue()) * normalValue);
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }
    
    public static ValueColorScheme quintipleRangeGradient(final Range range, final Color firstValueColor, final Color secondValueColor,final Color thirdValueColor,final Color fourthValueColor,final Color fifthValueColor, final Color nanColor) {
        return new ValueColorScheme() {

            @Override
            public int colorFor(double value) {
                if (Double.isNaN(value)) {
                    return nanColor.getRGB();
                }
                double firstNum = range.getMinimum().doubleValue();
                double secondNum = (range.getMaximum().doubleValue()-range.getMinimum().doubleValue())/4;
                double thirdNum = secondNum*2;
                double fourthNum = secondNum*3;
                double fifthNum = range.getMaximum().doubleValue();;
                
                int alpha = 0, red = 0, green = 0, blue = 0;
                if(value<secondNum){
                    double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue(), range.getMaximum().doubleValue());
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (firstValueColor.getRed() + (secondValueColor.getRed() - firstValueColor.getRed()) * normalValue);
                    green = (int) (firstValueColor.getGreen() + (secondValueColor.getGreen() - firstValueColor.getGreen()) * normalValue);
                    blue = (int) (firstValueColor.getBlue() + (secondValueColor.getBlue() - firstValueColor.getBlue()) * normalValue);  
                }
                
                if(value>=secondNum && value < thirdNum){
                    double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue(), range.getMaximum().doubleValue());
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (secondValueColor.getRed() + (thirdValueColor.getRed() - secondValueColor.getRed()) * normalValue);
                    green = (int) (secondValueColor.getGreen() + (thirdValueColor.getGreen() - secondValueColor.getGreen()) * normalValue);
                    blue = (int) (secondValueColor.getBlue() + (thirdValueColor.getBlue() - secondValueColor.getBlue()) * normalValue);  
                }
                
                if(value>=thirdNum && value < fourthNum){
                    double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue(), range.getMaximum().doubleValue());
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (thirdValueColor.getRed() + (fourthValueColor.getRed() - thirdValueColor.getRed()) * normalValue);
                    green = (int) (thirdValueColor.getGreen() + (fourthValueColor.getGreen() - thirdValueColor.getGreen()) * normalValue);
                    blue = (int) (thirdValueColor.getBlue() + (fourthValueColor.getBlue() - thirdValueColor.getBlue()) * normalValue);  
                }
                
                if(value>=fourthNum && value <= fifthNum){
                    double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue(), range.getMaximum().doubleValue());
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (fourthValueColor.getRed() + (fifthValueColor.getRed() - fourthValueColor.getRed()) * normalValue);
                    green = (int) (fourthValueColor.getGreen() + (fifthValueColor.getGreen() - fourthValueColor.getGreen()) * normalValue);
                    blue = (int) (fourthValueColor.getBlue() + (fifthValueColor.getBlue() - fourthValueColor.getBlue()) * normalValue);  
                }
                
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }
}
