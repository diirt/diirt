/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import javafx.scene.paint.Color;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.util.config.Configuration;
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
  
    public static NumberColorMap load(File file) {

        //Reading from xml 
        if (file.getName().endsWith(".xml")) {
            return loadXML(file);
        } //reading from CMAP
        else if (file.getName().endsWith(".cmap")) {
            return loadCMAP(file);
        }
        //File format not recognized
        throw new RuntimeException("File Format not Recognized"+file);
    }
    
    
    private static NumberColorMap loadCMAP(File file){
        List<Color> colors = new ArrayList<>();
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Colormap file "+file+" not found", ex);
        }
        String line;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] tokens = line.split(",");
            if (tokens.length != 3) {
                throw new RuntimeException("Error Parsing RGB value from file: "+file);
            }
            colors.add(Color.rgb(parseInt(tokens[0]), parseInt(tokens[1]), parseInt(tokens[2]),1.0));
        }
        String colormapName = file.getName(); 
        colormapName = colormapName.substring(0,colormapName.lastIndexOf('.')); 
        return relative(colors, Color.BLACK, colormapName);//cmap file is automatically relative
    }
    private static List<NumberColorMap> loadDefaultMaps(){
          List<NumberColorMap> maps = new ArrayList<>();
          Logger log = Logger.getLogger(NumberColorMaps.class.getName()); 
          File path = new File(Configuration.getDirectory(),"graphene/colormaps"); 
        if (path.exists()) {
                log.log(Level.CONFIG, "Loading ColorMaps from directory: "+path);
                for (File file : path.listFiles()) {
                    //makes sure we are only loading the xml files
                   if(file.getName().endsWith(".xml")){
                      log.log(Level.CONFIG, "Loading ColorMap from file: "+file);
                       maps.add(loadXML(file)); 
                       log.log(Level.CONFIG, "Load Success!");
                   }
                }
             
            }
         else { // The path does not exist
            path.mkdirs();
            log.log(Level.CONFIG, "Creating Path graphene/colormaps under DIIRT_HOME ");
            throw new RuntimeException("graphene/colormaps directory not existed. Could not load default ColorMaps"); 
        }
   
      
         return maps; 
    }

    private static NumberColorMap loadXML(File file){
        //if we are reading from a xml file

        List<Double> positions = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        boolean relative = true; //default positions to be relative 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;

        try {
            builder = factory.newDocumentBuilder();

        } catch (ParserConfigurationException ex) {
            throw new RuntimeException("Couldn't load color map from file: " + file, ex);
        }

        try {
            doc = builder.parse(file);
        } catch (SAXException ex) {
            throw new RuntimeException("Couldn't parse color map file: "+file, ex);
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't load color map from file: "+file, ex);
        }

        Element root = doc.getDocumentElement(); 
        relative = root.getAttribute("position").equals("relative"); 
        Color nanColor = Color.web(root.getAttribute("colorNaN")); 
    

        NodeList children = root.getChildNodes();
      
        for(int i = 0 ; i<children.getLength();++i){
             Node child = children.item(i); 
             if(child instanceof Element){
                    Element e = (Element)child; 
                    if(e.getTagName()=="color"){
                       positions.add(parseDouble( e.getAttribute("position"))); 
                       colors.add(Color.web(e.getAttribute("value"))); 
                       
                    }
             }
        }
    
        double[] positionsArray = new double[positions.size()];
        for (int i = 0; i < positions.size(); ++i) {
            positionsArray[i] = positions.get(i);
        }
        String colormapName = file.getName(); 
        colormapName = colormapName.substring(0,colormapName.lastIndexOf('.')); 
        return new NumberColorMapGradient(colors, new ArrayDouble(positionsArray), relative, nanColor, colormapName);
    }
    
    /**
     * JET ranges from blue to red, going through cyan and yellow.
     */
    
    public static final NumberColorMap JET = relative(Arrays.asList(new Color[]{Color.rgb(0,0,138), 
                                                                                Color.BLUE,
                                                                                Color.CYAN,
                                                                                Color.YELLOW,
                                                                                Color.RED,
                                                                                Color.rgb(138,0,0)}), Color.BLACK, "JET"); 
    /**
     * GRAY ranges from black to white.
     */
    public static final NumberColorMap GRAY= relative(Arrays.asList(new Color[]{Color.BLACK, 
                                                                                       Color.WHITE
                                                                                       }),Color.RED,"GRAY"); 
    /**
     * BONE ranges from black to white passing from blue.
     */     
    public static final NumberColorMap BONE = relative(Arrays.asList(new Color[]{Color.BLACK,
                                                                                       Color.rgb(57, 57, 86),
                                                                                        Color.rgb(107, 115, 140),
                                                                                        Color.rgb(165, 198, 198),
                                                                                       Color.WHITE
                                                                                       } ),Color.RED,"BONE");
    /**
     * HOT ranges from black to white passing from red and yellow.
     */
    public static final NumberColorMap HOT = relative(Arrays.asList(Color.BLACK,
                                                Color.RED,
                                                Color.YELLOW,
                                                Color.WHITE
                                                ), Color.BLUE, "HOT");
            
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
                                                                                       Color.RED
                                                                                       } ),Color.BLACK,"HSV"); 

    private static final Map<String, NumberColorMap> registeredColorSchemes
            = new ConcurrentHashMap<>();
   
    static {
        
        registeredColorSchemes.put(JET.toString(), JET);
        registeredColorSchemes.put(GRAY.toString(), GRAY);
        registeredColorSchemes.put(BONE.toString(), BONE);
        registeredColorSchemes.put(HOT.toString(), HOT);
        registeredColorSchemes.put(HSV.toString(), HSV);
        
        // TODO: Load new ones from "DIIRT_HOME/graphene/colormaps/
        // using Configuration. (see jdbc service)
        
         /*
        List<NumberColorMap> maps = loadDefaultMaps(); 
        
        for(NumberColorMap map: maps) {
            registeredColorSchemes.put(map.toString(),map); 
        }
         */
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
    // TODO: add javadocs
    public static NumberColorMap relative(List<Color> colors, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, percentageRange(colors.size()), true, nanColor, name);
    }
    
    public static NumberColorMap relative(List<Color> colors, ListDouble percentages, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, percentages, true, nanColor, name);
    }
    
    public static NumberColorMap absolute(List<Color> colors, ListDouble values, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, values, false, nanColor, name);
    }

}
