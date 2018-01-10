/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile;

import org.diirt.graphene.profile.settings.SaveSettings;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import org.diirt.graphene.Graph2DRenderer;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.graphene.profile.io.CSVWriter;
import org.diirt.graphene.profile.io.DateUtils;
import org.diirt.graphene.profile.io.ImageWriter;
import org.diirt.graphene.profile.settings.RenderSettings;
import org.diirt.graphene.profile.utils.Resolution;

/**
 * Handles the profiling for testing rendering (specifically the draw) of a <code>Graph2DRenderer</code>.
 * The base class for all graph profilers.
 * Has parameter T that is a graph renderer.
 * Has parameter S that is the dataset associated with T.
 *
 * A profiler creates a loop in which a Graph2DRenderer perform multiple render operations.
 * Various options are provided to handle the profile statistics.
 *
 * @param <T> type of graph render being profiled that is subclass of <code>Graph2DRenderer</code>
 * @param <S> dataset type that is associated with T the graph renderer
 *
 * @author asbarber
 */
public abstract class ProfileGraph2D<T extends Graph2DRenderer, S> extends Profiler{

    /**
     * Creates a graph profiler.
     */
    public ProfileGraph2D(){
        saveSettings = new SaveSettings();
        renderSettings = new RenderSettings(this);
    }

    /**
     * Default file path for all CSV log files of statistics.
     */
    public static final String LOG_FILEPATH = "ProfileResults\\";


    //Parameters
    private Resolution      resolution = new Resolution(600, 400);
    private int             nPoints = 1000;

    //Settings
    private RenderSettings  renderSettings;
    private SaveSettings    saveSettings;

    //Temporary (used in iteration)
    private BufferedImage   image = null;
    private Graphics2D      graphics = null;

    protected S data = null;
    protected T renderer = null;

    @Override
    protected void preLoopAction(){
        //Clears
        saveSettings.setSaveImage(null);

        //Data and Render Objects (Implemented in subclasses)
        data = getDataset();
        renderer = getRenderer(resolution.getWidth(), resolution.getHeight());

        for (Graph2DRendererUpdate u: renderSettings.getUpdates()){
            renderer.update(u);
        }

        //Creates the image buffer if parameter says to set it ouside of render loop
        if (!renderSettings.getBufferInLoop()){
            image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
            graphics = image.createGraphics();
        }
    }

    @Override
    protected void iterationAction(){
        //Create Image if necessary
        if (renderSettings.getBufferInLoop()){
            image = new BufferedImage(renderer.getImageWidth(),
                                      renderer.getImageHeight(),
                                      BufferedImage.TYPE_3BYTE_BGR);
            graphics = image.createGraphics();
        }

        //Subclass render
        render(graphics, renderer, data);
    }

    @Override
    protected void postIterationAction(){
        //Stores first image
        if (getSaveSettings().getSaveImage() == null){
            getSaveSettings().setSaveImage(image);
        }

        //Buffer clears
        if (image != null && image.getRGB(0, 0) == 0){
            System.out.println("Black");
        }
    }


    //During-Profile Sections

    /**
     * The data that to be rendered in the render loop.
     * Precondition: getRenderer() is capable of rendering getDataset().
     *               Thus type T is capable of rendering type S.
     *
     * A useful helper method is getNumDataPoints.
     * The size of the returned data set should match the size specified by getNumDataPoints.
     * @return a set of data associated with T
     */
    protected abstract S getDataset();

    /**
     * The renderer used in the render loop.
     * Precondition: <code>getRenderer()</code> is capable of rendering <code>getDataset()</code>.
     *               Thus type <code>T</code> is capable of rendering type <code>S</code>.
     * @param imageWidth pixel width of rendered image
     * @param imageHeight pixel height of rendered image
     * @return a <code>Graph2DRenderer</code> associated with data S
     */
    protected abstract T getRenderer(int imageWidth, int imageHeight);

    /**
     * The primary method in the profiling render loop.
     * Override this method to test the draw method of 'renderer'.
     *
     * @param graphics where image draws to
     * @param renderer what draws the image
     * @param data what is drawn
     */
    protected abstract void render(Graphics2D graphics, T renderer, S data);

    /**
     * Gets the updates associated with the renderer in a map, linking a
     * description of the update to the update object.
     * @return map with description of update paired with an update
     */
    public abstract LinkedHashMap<String, Graph2DRendererUpdate> getVariations();


    //Post-Profile Options


    /**
     * Writes the profile statistics to a CSV file designated to the profile graph.
     * Appends the statistics to the end of the CSV file.
     *
     * The file name is designated by getLogFileName().
     * Each subclass should thus have its own CSV file.
     * If a CSV file does not exist, the file is created.
     *
     * If statistics do no exist, no operations are performed.
     * If errors occur in IO, a console message is printed.
     *
     * The delimiting is:
     * <ul>
     *      <li>Non-numeric components enclosed in quotes</li>
     *      <li>Numeric components not enclosed
     *      <li>Components separated by commas (no spaces)</li>
     * </ul>
     *
     * The components are:
     * <ol>
     *      <li>Graph title (type of graph)</li>
     *      <li>Date</li>
     *      <li>Average time rendering</li>
     *      <li>Total time rendering</li>
     *      <li>Number of render attempts</li>
     *      <li>Number of data points</li>
     *      <li>Width of image rendered</li>
     *      <li>Height of image rendered</li>
     *      <li>Timing Type</li>
     *      <li>Update Applied</li>
     *      <li>Comment about the data set rendered (useful for multi-dimensional or unusual data sets)</li>
     *      <li>Comment about the author performing the profile</li>
     *      <li>General comment about rendering</li>
     * </ol>
     */
    public void saveStatistics(){
        String fileName = LOG_FILEPATH + getLogFileName();

        List header = CSVWriter.arrayCombine(
                "Graph Type",
                "Date",
                stopWatch.getTitle(),
                "Number of Tries",
                "Number of Data Points",
                resolution.getTitle(),
                getProfileSettings().getTitle(),
                renderSettings.getTitle(),
                saveSettings.getTitle()
        );

        List row = CSVWriter.arrayCombine(
            getGraphTitle(),
            DateUtils.getDate(DateUtils.DateFormat.DELIMITED),
            stopWatch.getOutput(),
            nTries,
            getNumDataPoints(),
            resolution.getOutput(),
            getProfileSettings().getOutput(),
            renderSettings.getOutput(),
            saveSettings.getOutput()
        );

        super.saveStatistics(fileName, header, row);
    }

    public void saveImage(){
        BufferedImage i = getSaveSettings().getSaveImage();

        if (i != null){
            ImageWriter.saveImage(getLogFileName(), i);
        }
    }


    //Save Parameter Getters
    @Override
    public String getProfileTitle(){
        return getGraphTitle();
    }

    /**
     * Gets the type of graph renderer.
     * Used in printing and saving statistics.
     * Example: "LineGraph2D"
     * @return the title of the graph renderer being profiled
     */
    public abstract String getGraphTitle();

    /**
     * Gets the name of the CSV file to save statistics to.
     * Derived from <code>getGraphTitle()</code>.
     * @return the file name (not file path) of the CSV log file
     */
    public String getLogFileName(){
        return getGraphTitle() + "-Table1D";
    }

    /**
     * Gets the settings to be saved to the output file
     * for a profile.
     * Some settings include:
     * <ul>
     *      <li>Author</li>
     *      <li>Dataset message</li>
     *      <li>Save message</li>
     * </ul>
     * @return the messages about settings information to
     *         be saved to an output file
     */
    public SaveSettings getSaveSettings(){
        return this.saveSettings;
    }

    public RenderSettings getRenderSettings(){
        return this.renderSettings;
    }

    public Resolution getResolution(){
        return resolution;
    }


    //Test Parameter Getters

    /**
     * Gets the size of the data set.
     * Useful for creating the data set of the appropriate size.
     * Used in saving statistics to the CSV log file.
     *
     * @return size of the data set in rendering
     */
    public int getNumDataPoints(){
        return nPoints;
    }


    //Test Parameter Setters

    /**
     * Sets the size of the data set.
     * Useful for creating the data set of the appropriate size.
     * Used in saving statistics to the CSV log file.
     *
     * @param nPoints size of the data set in rendering
     */
    public void setNumDataPoints(int nPoints){
        this.nPoints = nPoints;
    }
}
