/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;
import java.util.ArrayList;
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
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(0,0,138)); //Dark Blue
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(new Color(138,0,0)); //Dark Red
        colors.add(Color.BLACK);    //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));
    }
    
    public static ValueColorScheme hotScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(30,0,0)); //Very Dark Red
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.WHITE);
        colors.add(Color.BLUE); //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));
    }

    public static ValueColorScheme coolScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        
        colors.add(Color.CYAN);
        colors.add(new Color(66, 189, 255));    //Light Blue        
        colors.add(new Color(189, 66, 255));    //Purple
        colors.add(Color.MAGENTA);
        
        colors.add(Color.RED);  //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));        
    }
    
    public static ValueColorScheme springScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        
        colors.add(Color.MAGENTA);
        colors.add(Color.YELLOW);

        colors.add(Color.RED);  //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));        
    }    
    
    public static ValueColorScheme boneScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        
        colors.add(Color.BLACK);
        colors.add(new Color(44, 37, 101));     //Dark Blue
        colors.add(new Color(107, 115, 140));   //Blue
        colors.add(new Color(158, 203, 205));   //Pale Blue
        colors.add(Color.WHITE);

        
        colors.add(Color.RED);  //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));        
    }    
    
    public static ValueColorScheme copperScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        
        colors.add(Color.BLACK);
        colors.add(new Color(66, 41, 24));      //Dark Brown
        colors.add(new Color(173, 107, 68));    //Brown
        colors.add(new Color(239, 148, 90));    //Light Brown
        colors.add(new Color(255, 198, 123));   //Tan

        
        colors.add(Color.RED);  //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));        
    }    
    
    public static ValueColorScheme pinkScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        
        colors.add(new Color(57, 0, 0));        //Dark Red
        colors.add(new Color(189, 123, 123));   //Dark Pink
        colors.add(new Color(214, 189, 156));   //Pale Pink
        colors.add(Color.WHITE);

        
        colors.add(Color.RED);  //NaN
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));        
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

    public static ValueColorScheme RangeGradient(final Range range, final ArrayList<Color> colors, final ArrayList<Double> percentages){
        return new ValueColorScheme() {
            
            Color nanColor = colors.get(colors.size()-1); 
            @Override
            public int colorFor(double value) {
                if (Double.isNaN(value)) {
                    return nanColor.getRGB();
                }
                double fullRange = range.getMaximum().doubleValue() - range.getMinimum().doubleValue();
                int alpha = 0, red = 0, green = 0, blue = 0;
                if(fullRange>0){
                    for(int i = 0; i < percentages.size()-1;i++){
                        if(range.getMinimum().doubleValue()+percentages.get(i)*fullRange <= value && value <= range.getMinimum().doubleValue()+percentages.get(i+1)*fullRange){
                            double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue()+percentages.get(i)*fullRange, range.getMinimum().doubleValue()+percentages.get(i+1)*fullRange);
                            normalValue = Math.min(normalValue, 1.0);
                            normalValue = Math.max(normalValue, 0.0);
                            alpha = 255;
                            red = (int) (colors.get(i).getRed() + (colors.get(i+1).getRed() - colors.get(i).getRed()) * normalValue);
                            green = (int) (colors.get(i).getGreen() + (colors.get(i+1).getGreen() - colors.get(i).getGreen()) * normalValue);
                            blue = (int) (colors.get(i).getBlue() + (colors.get(i+1).getBlue() - colors.get(i).getBlue()) * normalValue);
                        }
                    }
                }
                else{
                    for(int i = 0; i < percentages.size()-1;i++){
                        if(percentages.get(i) <= .5 && .5 <= percentages.get(i+1)){
                            double normalValue =0;
                            normalValue = Math.min(normalValue, 1.0);
                            normalValue = Math.max(normalValue, 0.0);
                            alpha = 255;
                            red = (int) (colors.get(i).getRed() + (colors.get(i+1).getRed() - colors.get(i).getRed()) * normalValue);
                            green = (int) (colors.get(i).getGreen() + (colors.get(i+1).getGreen() - colors.get(i).getGreen()) * normalValue);
                            blue = (int) (colors.get(i).getBlue() + (colors.get(i+1).getBlue() - colors.get(i).getBlue()) * normalValue);
                        }
                    }
                }
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }
    
    private static ArrayList<Double> percentageRange(int size){
        ArrayList<Double> percentages = new ArrayList<>();
        
        percentages.add(0.0);
        
        for (int i = 1; i <= size; i++){
            percentages.add((double) i / size);
        }
        
        return percentages;
    }
}
