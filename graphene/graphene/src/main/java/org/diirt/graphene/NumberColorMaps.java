/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A utility class that provides implementations of {@link NumberColorMap},
 * a set standard utilities and a directory for color maps.
 *
 * @author carcassi
 */
public class NumberColorMaps {
    
    // TODO: add more color schemes like the ones that can be found:
    // http://www.mathworks.com/help/matlab/ref/colormap.html
    
      private NumberColorMap colorMap; 
    
    public NumberColorMaps(){
        colorMap=JET; //default color map
    }
    public NumberColorMap getColorMap(){
        return colorMap; 
    }
   /*  file format 
    <colormap> 
        <color>
          <position> relative or absoulute </position>
          <R> </R>
          <G> </G> 
          <B> </B> 
        </color>
    </colormap>
   */
    
    public void loadColorMap(File file,boolean format_relative) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
      // new implementation loading from XML file 
        List<Double> positions = new ArrayList<>(); 
        List<Color> colors= new ArrayList<>(); 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file); 
        Element el = doc.getDocumentElement(); 
        NodeList nl = el.getChildNodes(); 
        
        if(nl!=null){
            for(int i=0; i<nl.getLength();i++){
                 if(nl.item(i).getNodeType()==Node.ELEMENT_NODE){
                     Element e = (Element)nl.item(i); 
                     if(e.getNodeName()=="color"){
                        
                         positions.add( parseDouble(e.getElementsByTagName("position").item(0).getTextContent())); 
                         int R =parseInt(e.getElementsByTagName("R").item(0).getTextContent()); 
                         int G =parseInt(e.getElementsByTagName("G").item(0).getTextContent()); 
                         int B =parseInt(e.getElementsByTagName("B").item(0).getTextContent()); 
                     }
                 }
            }
        }
        
        
      /* 
        //color map file can be found at colormap.org
        Scanner scanner=new Scanner(file);          
        String line; 
        List<Color> colorArray=new ArrayList<Color>(); 
        while(scanner.hasNextLine()){
            line=scanner.nextLine(); 
            String []tokens=line.split("\\s+"); 
            if(tokens.length!=3){
                throw new IOException(); 
            }
            colorArray.add(new Color(parseInt(tokens[0]),parseInt(tokens[1]),parseInt(tokens[2]))); 
        }
       */ 
        colorMap=new NumberColorMapGradient(colors, positions, format_relative, Color.yellow, Color.orange, Color.orange, file.getName()); 
       
             
    }
    
    /**
     * JET ranges from blue to red, going through cyan and yellow.
     */
    public static final NumberColorMap JET = new NumberColorMapGradient(new Color[]{new Color(0,0,138), 
                                                                                Color.BLUE,
                                                                                Color.CYAN,
                                                                                Color.YELLOW,
                                                                                Color.RED,
                                                                                new Color(138,0,0), 
                                                                                Color.BLACK}, "JET");
    /**
     * GRAY ranges from black to white.
     */
    public static final NumberColorMap GRAY = new NumberColorMapGradient(new Color[]{Color.BLACK, 
                                                                                       Color.WHITE,
                                                                                       Color.RED}, "GRAY");
    /**
     * BONE ranges from black to white passing from blue.
     */
    public static final NumberColorMap BONE = new NumberColorMapGradient(new Color[]{Color.BLACK,
                                                                                       new Color(57, 57, 86),
                                                                                       new Color(107, 115, 140),
                                                                                       new Color(165, 198, 198),
                                                                                       Color.WHITE,
                                                                                       Color.RED}, "BONE");
    /**
     * HOT ranges from black to white passing from red and yellow.
     */
    public static final NumberColorMap HOT = new NumberColorMapGradient(new Color[]{Color.BLACK,
                                                                                       Color.RED,
                                                                                       Color.YELLOW,
                                                                                       Color.WHITE,
                                                                                       Color.BLUE}, "HOT");
    /**
     * HSV goes through the color wheel: red, yellow, green, cyan, blue, magenta
     * and back to red. Useful for periodic functions.
     */
    public static final NumberColorMap HSV = new NumberColorMapGradient(new Color[]{Color.RED,
                                                                                       Color.YELLOW,
                                                                                       Color.GREEN,
                                                                                       Color.CYAN,
                                                                                       Color.BLUE,
                                                                                       Color.MAGENTA,
                                                                                       Color.RED,
                                                                                       Color.BLACK}, "HSV");
    private static final Map<String, NumberColorMap> registeredColorSchemes
            = new ConcurrentHashMap<>();
    
    static {
        registeredColorSchemes.put(JET.toString(), JET);
        registeredColorSchemes.put(GRAY.toString(), GRAY);
        registeredColorSchemes.put(BONE.toString(), BONE);
        registeredColorSchemes.put(HOT.toString(), HOT);
        registeredColorSchemes.put(HSV.toString(), HSV);
    }
    
    /**
     * A set of registered color maps available to all applications.
     * 
     * @return a set of color maps and their names
     */
    public static Map<String, NumberColorMap> getRegisteredColorSchemes() {
        return Collections.unmodifiableMap(registeredColorSchemes);
    }
    
    /**
     * Returns a new optimized instance created by pre-calculating the colors
     * in the given range and storing them in an array.
     * <p>
     * An optimized map will trade off precision for speed. The color will not
     * change smoothly but will be quantized to the size of the array.
     * 
     * @param instance the color map instance to optimize
     * @param range the range of values to optimize
     * @return the optimized map
     */
    public static NumberColorMapInstance optimize(NumberColorMapInstance instance, Range range){
        return new NumberColorMapInstanceOptimized(instance, range);
    }
    
    /**
     * TODO: what is this about?
     * 
     * @param instance the color map instance to optimize
     * @param oldRange TODO
     * @param newRange TODO
     * @return TODO
     */
    public static NumberColorMapInstance optimize(NumberColorMapInstance instance, Range oldRange, Range newRange){
        return new NumberColorMapInstanceOptimized(instance, oldRange, newRange);
    }
    
}
