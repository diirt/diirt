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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.ParserConfigurationException;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListDouble;
import org.w3c.dom.Attr;
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
    
    
    private NumberColorMaps() {
        // Utility class. Do not instanciate.
    }
    
    private static ListDouble percentageRange(int size) {
        double [] percentages = new double[size]; 
        

        percentages[0]=0.0; 

        for (int i = 1; i <= size-1; i++) {
            percentages[i]=((double) i / (size-1));
        }

        return new ArrayDouble(percentages);
    }
   /*  file format 
    <colormap positionType="relative"/"absolute"> 
        <color>
          <position> relative or absoulute </position>
          <R> </R>
          <G> </G> 
          <B> </B> 
        </color>
    </colormap>
   */
    
    public static NumberColorMapGradient load(File file) throws FileNotFoundException,ParserConfigurationException, SAXException, IOException{
        //determine file type 
        String fileName= file.getName(); 
        String fileExtenstion=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()); 
 
        List<Double> positions = new ArrayList<>(); 
        List<Color> colors= new ArrayList<>(); 
        boolean relative = true; //default positions to be relative 
        
        //Reading from xml 
       if(fileExtenstion.equalsIgnoreCase("xml")){
                //if we are reading from a xml file
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file); 
            
            //check if the postions is relative  
            NodeList nodeList = doc.getElementsByTagName("colormap"); 
            Node node = nodeList.item(0); 
            Attr attr = (Attr) node.getAttributes().getNamedItem("positionType"); 
            relative = attr.getValue().equals("relative"); 
            //read positions and RGB value
            Element el = doc.getDocumentElement(); 
            NodeList nl = el.getChildNodes(); 
            
            if(nl!=null){
                for(int i=0; i<nl.getLength();i++){
                     if(nl.item(i).getNodeType()==Node.ELEMENT_NODE){
                         Element e = (Element)nl.item(i); 
                         if("color"==e.getNodeName()){

                             positions.add(parseDouble(e.getElementsByTagName("position").item(0).getTextContent())); 
                             int R =parseInt(e.getElementsByTagName("R").item(0).getTextContent()); 
                             int G =parseInt(e.getElementsByTagName("G").item(0).getTextContent()); 
                             int B =parseInt(e.getElementsByTagName("B").item(0).getTextContent()); 
                             colors.add(new Color(R,G,B)); 
                         }
                     }
                }
            }

          }
        //color map file can be found at colormap.org 
       else if (fileExtenstion.equalsIgnoreCase("cmap")){

        Scanner scanner=new Scanner(file);          
        String line; 
         
        while(scanner.hasNextLine()){
            line=scanner.nextLine(); 
            String []tokens=line.split(","); 
            if(tokens.length!=3){
                throw new IOException("Error Parsing RGB value"); 
            }
            colors.add(new Color(parseInt(tokens[0]),parseInt(tokens[1]),parseInt(tokens[2]))); 
        }
            return (NumberColorMapGradient) relative(colors, Color.BLACK, file.getName());//cmap file is automatically relative
       }
       else
       {
           throw new FileNotFoundException("File Format not Recognized"); 
           
       }
       
        double [] positionsArray = new double [positions.size()]; 
        for(int i =0; i<positions.size();++i){
            positionsArray[i]=positions.get(i); 
        }
         return new NumberColorMapGradient(colors,new ArrayDouble(positionsArray), relative, Color.BLACK, file.getName()); 
     
       
       
             
    }
    
    /**
     * JET ranges from blue to red, going through cyan and yellow.
     */
    
     public static final NumberColorMap JET = relative(Arrays.asList(new Color[]{new Color(0,0,138), 
                                                                                Color.BLUE,
                                                                                Color.CYAN,
                                                                                Color.YELLOW,
                                                                                Color.RED,
                                                                                new Color(138,0,0), 
                                                                                Color.BLACK}), Color.BLACK, "JET"); 
    /**
     * GRAY ranges from black to white.
     */
    public static final NumberColorMap GRAY= relative(Arrays.asList(new Color[]{Color.BLACK, 
                                                                                       Color.WHITE,
                                                                                       Color.RED}),Color.BLACK,"GRAY"); 
    /**
     * BONE ranges from black to white passing from blue.
     */
    public static final NumberColorMap BONE = relative(Arrays.asList(new Color[]{Color.BLACK,
                                                                                       new Color(57, 57, 86),
                                                                                       new Color(107, 115, 140),
                                                                                       new Color(165, 198, 198),
                                                                                       Color.WHITE,
                                                                                       Color.RED} ),Color.BLACK,"BONE");
    /**
     * HOT ranges from black to white passing from red and yellow.
     */
    public static final NumberColorMap HOT = relative(Arrays.asList(Color.BLACK,
                                                Color.RED,
                                                Color.YELLOW,
                                                Color.WHITE,
                                                Color.BLUE), Color.BLACK, "HOT");
            
    /**
     * HSV goes through the color wheel: red, yellow, green, cyan, blue, magenta
     * and back to red. Useful for periodic functions.
     */
    public static final NumberColorMap HSV = relative(Arrays.asList(new Color[]{Color.RED,
                                                                                       Color.YELLOW,
                                                                                       Color.GREEN,
                                                                                       Color.CYAN,
                                                                                       Color.BLUE,
                                                                                       Color.MAGENTA,
                                                                                       Color.RED,
                                                                                       Color.BLACK} ),Color.BLACK,"HSV"); 

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
    
    public static NumberColorMap relative(List<Color> colors, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, percentageRange(colors.size()), true, nanColor, name);
    }

}
