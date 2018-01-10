/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Range;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.diirt.util.array.ListNumbers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Factory and registry class for {@link NumberColorMap}s.
 * It allows to create and register new maps, and allows a central place to
 * find registered color maps by name.
 *
 * @author carcassi
 */
public class NumberColorMaps {
    private static final Logger log = Logger.getLogger(NumberColorMaps.class.getName());

    private NumberColorMaps() {
        // Utility class. Do not instanciate.
    }

    /**
     * Loads a {@code NumberColorMap} from a file.
     * <p>
     * It must follow one of the supported format, or an
     * exception is returned. The extension is used to determine
     * which file format is being used.
     *
     * @param file the color map file
     * @return the new map
     */
    public static NumberColorMap load(File file) {

        if (file.getName().endsWith(".xml")) {
            // Reading from xml
            return loadXML(file);
        } else if (file.getName().endsWith(".cmap")) {
            // Reading from CMAP
            return loadCMAP(file);
        }

        // File format not recognized
        throw new RuntimeException("File Format not Recognized" + file);
    }


    private static NumberColorMap loadCMAP(File file){
        List<Color> colors = new ArrayList<>();
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Colormap file " + file + " not found", ex);
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
        // cmap file is automatically relative
        return relative(colors, Color.BLACK, colormapName);
    }

    private static NumberColorMap loadXML(File file){
        //if we are reading from a xml file

        List<Double> positions = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        boolean relative;
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
            throw new RuntimeException("Couldn't parse color map file: "+ file, ex);
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't load color map from file: "+ file, ex);
        }

        Element root = doc.getDocumentElement();
        // Throw exception if they don't match format supported
        if (root.getAttribute("position").equals("relative") ||
                root.getAttribute("position").equals("absolute")){
              relative = root.getAttribute("position").equals("relative");
        }else{
            throw new RuntimeException("Colormap file only supports absoulute and relative scale " + file);
        }


        Color nanColor = Color.web(root.getAttribute("colorNaN"));


        NodeList children = root.getChildNodes();

        for(int i = 0 ; i<children.getLength();++i){
            Node child = children.item(i);
            if (child instanceof Element) {
                    Element e = (Element)child;
                    if (e.getTagName().equals("color")) {
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


    private static void initializeColorMapDirectory(File path) {
        // List of default color maps
        String [] mapNames = {
            "BONE.xml",
            "GRAY.xml",
            "HOT.xml",
            "HSV.xml",
            "HSVRadian.xml",
            "JET.xml"
        };

        for (String map: mapNames) {
            File mapFile = new File(path,map);
            try {
                mapFile.createNewFile();
            } catch (IOException ex) {
                log.log(Level.WARNING, "Failed Creating new file " + mapFile, ex);
                continue;
            }

            try (InputStream input = NumberColorMaps.class.getResourceAsStream(map);
                    OutputStream output = new FileOutputStream(mapFile)) {
                byte[] buffer = new byte[8*1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                log.log(Level.WARNING, "Failed Loading " + map, ex);
            }

        }
    }

    private static List<NumberColorMap> loadMapsFromLocal() {
        List<NumberColorMap> maps = new ArrayList<>();
        File path = new File(Configuration.getDirectory(), "graphene/colormaps");
        // If maps are not there, create them first
        if (!path.exists()) {
            path.mkdirs();

            log.log(Level.CONFIG, "Creating path graphene/colormaps under DIIRT_HOME ");
            initializeColorMapDirectory(path);
        }
        // Load maps from local directory
        log.log(Level.CONFIG, "Loading ColorMaps from directory: " + path);
        for (File file : path.listFiles()) {

            log.log(Level.CONFIG, "Loading ColorMap from file: " + file);
            try {
                maps.add(load(file));
            } catch (RuntimeException ex) {
                log.log(Level.WARNING, ex.getMessage());
            }

        }

        return maps;
    }


    // TODO: remove hard-coded color maps. They should be loaded by name
    // from the registered map

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
        List<NumberColorMap> maps = loadMapsFromLocal();

        for (NumberColorMap map: maps) {
            registeredColorSchemes.put(map.toString(),map);
        }

    }

    public static final String DEFAULT_NUMBER_COLOR_MAP_NAME = "JET";

    /**
     * Returns the default {@code NumberColorMap}. It searches for {@link #DEFAULT_NUMBER_COLOR_MAP_NAME}
     * within the registered maps, and if not found it uses a hard coded version.
     *
     * @return a color map; never null
     */
    public static NumberColorMap defaultNumberColorMap() {
        NumberColorMap colorMap = null;
        try {
            colorMap = getRegisteredColorSchemes().get(DEFAULT_NUMBER_COLOR_MAP_NAME);
        } catch(Exception ex) {
            // Loading failed
        }
        if (colorMap != null) {
            return colorMap;
        } else {
            return JET;
        }
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
     * Creates a new {@code ColorMap} where the color list is equally
     * spaced.
     *
     * @param colors the list of colors used for the values
     * @param nanColor the color used for NaN values
     * @param name the name of the color map
     * @return the new color map
     */
    public static NumberColorMap relative(List<Color> colors, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, ListNumbers.linearListFromRange(0.0, 1.0, colors.size()), true, nanColor, name);
    }


    /**
     * Creates a new{@code ColorMap} where the color list is spaced out
     * according to the percentage points specified
     * @param colors the list of colors used
     * @param percentages the list of percentages position that divide up the colors
     * @param nanColor the color used for Nan values
     * @param name the name of the color map
     * @return the new color map
     */
    public static NumberColorMap relative(List<Color> colors, ListDouble percentages, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, percentages, true, nanColor, name);
    }

       /**
     * Creates a new{@code ColorMap} where the color list is spaced out
     * according to the absolute points specified
     * @param colors the list of colors used
     * @param values the list of absolute position that divide up the colors
     * @param nanColor the color used for Nan values
     * @param name the name of the color map
     * @return the new color map
     */
    public static NumberColorMap absolute(List<Color> colors, ListDouble values, Color nanColor, String name) {
        return new NumberColorMapGradient(colors, values, false, nanColor, name);
    }

}
