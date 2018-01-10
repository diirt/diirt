/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.settings;

import java.awt.image.BufferedImage;
import java.lang.management.ManagementFactory;

/**
 * A general purpose collection of messages to be saved to a
 * .CSV output file for profiling.
 * <p>
 * Includes various properties,
 * such as author and dataset comments,
 * as well as hardware comments such as RAM.
 *
 * @author asbarber
 */
public class SaveSettings implements Settings{

    /**
     * Conversion from bytes to gigabytes:
     * 2^30 BYTES per GB.
     */
    private static final double BYTES_TO_GB = 1073741824; //2^30


    //Member Fields
    //--------------------------------------------------------------------------

    private String datasetMessage = "",
                  saveMessage = "",
                  authorMessage = "";

    private BufferedImage saveImage = null;

    //--------------------------------------------------------------------------


    //Setters
    //--------------------------------------------------------------------------

    /**
     * Set the comment associated with the data set.
     * This comment will be written to the CSV log file when saving the statistics.
     * <p>
     * This is appropriate for discussing the distribution of the data, dimensions of the data, etc.
     *
     * @param message comment about the data
     */
    public void setDatasetMessage(String message){
        this.datasetMessage = message;
    }

    /**
     * Set the general comment associated with the profile.
     * This comment will be written to the CSV log file when saving the statistics.
     * <p>
     * This is appropriate for discussing the parameters of the renderer, etc.
     *
     * @param message general comment about the profiling
     */
    public void setSaveMessage(String message){
        this.saveMessage = message;
    }

    /**
     * Set the author associated with the profile.
     * This comment will be written to the CSV log file when saving the statistics.
     * <p>
     * This is appropriate for stating the user who profiled.
     * This can help provide understanding in different results due
     * to computer hardware differences.
     *
     * @param author author for the profiling
     */
    public void setAuthorMessage(String author){
        this.authorMessage = author;
    }

    /**
     * Sets the image to be saved.
     * The image is one of the images rendered during profiling.
     * This field is not written to the comments of the CSV log file.
     * @param saveImage image of the graph to be saved
     */
    public void setSaveImage(BufferedImage saveImage){
        this.saveImage = saveImage;
    }


    //Getters
    //--------------------------------------------------------------------------

    /**
     * Gets the comment associated with the data set.
     * This comment will be written to the CSV log file when saving the statistics.
     *
     * This is appropriate for discussing the distribution of the data, dimensions of the data, etc.
     * @return message about the data set
     */
    public String getDatasetMessage(){
        return datasetMessage;
    }

    /**
     * Gets the general comment associated with the profile.
     * This comment will be written to the CSV log file when saving the statistics.
     *
     * This is appropriate for discussing the parameters of the renderer, etc.
     * @return general message about the profiling results
     */
    public String getSaveMessage(){
        return saveMessage;
    }

    /**
     * Gets the author associated with the profile.
     * This comment will be written to the CSV log file when saving the statistics.
     * <p>
     * This is appropriate for stating the user who profiled.
     * This can help provide understanding in different results due
     * to computer hardware differences.
     *
     * @return author for the profiling
     */
    public String getAuthorMessage(){
        return this.authorMessage;
    }

    /**
     * Gets the image to be saved.
     * The image is one of the images rendered during profiling.
     * This field is not written to the comments of the CSV log file.
     * @return image of the graph to be saved
     */
    public BufferedImage getSaveImage(){
        return saveImage;
    }

    //--------------------------------------------------------------------------


    //FORMATS FOR OUTPUT FILE
    //--------------------------------------------------------------------------

    /**
     * Gets the .CSV formatted header for the:
     * (title row)
     * <ol>
     *      <li>Dataset comments</li>
     *      <li>Author</li>
     *      <li>General Message</li>
     * </ol>
     *
     * @return the header for the general comments output
     */
    private String[] getSaveOutputHeader(){
        return new String[]{
            "Dataset Comments",
            "Author",
            "General Message"
        };
    }

    /**
     * Gets the .CSV formatted record for the:
     * (information row)
     * <ol>
     *      <li>Dataset comments</li>
     *      <li>Author</li>
     *      <li>General Message</li>
     * </ol>
     *
     * @return the record for the general comments output
     */
    private String[] getSaveOutputMessage(){
        String quote = "\"";
        String delim = ",";

        return new String[]{
            getDatasetMessage(),
            getAuthorMessage(),
            getSaveMessage()
        };
    }

    /**
     * Gets the .CSV formatted header for the:
     * (title row)
     * <ol>
     *      <li>Java Virtual Machine Version</li>
     *      <li>Available Memory</li>
     *      <li>Total RAM</li>
     *      <li>Operating System Name</li>
     *      <li>Operating System Version</li>
     * </ol>
     *
     * @return the header for the hardware comments output
     */
    private String[] getHardwareTitle(){
        return new String[]{
            "JVM Version",
            "Available Memory (GB)",
            "RAM (GB)",
            "OS",
            "OS Version"
        };
    }

    /**
     * Gets the .CSV formatted record for the:
     * (information row)
     * <ol>
     *      <li>Java Virtual Machine Version</li>
     *      <li>Available Memory</li>
     *      <li>Total RAM</li>
     *      <li>Operating System Name</li>
     *      <li>Operating System Version</li>
     * </ol>
     *
     * @return the record for the hardware comments output
     */
    private String[] getHardwareMessage(){
        //Get Environment Properties
        String javaVersion = System.getProperty("java.version");
        String os = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");

        //Format
        javaVersion = javaVersion == null ? "": javaVersion;
        os = os == null ? "": os;
        osVersion = osVersion == null ? "": osVersion;

        //Memory
        double runtime  = Runtime.getRuntime().maxMemory() / SaveSettings.BYTES_TO_GB;
        double memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / SaveSettings.BYTES_TO_GB;

        return new String[]{
            javaVersion,
            String.format("%.3f", runtime),
            String.format("%.3f", memorySize),
            os,
            osVersion
        };
    }

    //--------------------------------------------------------------------------


    //COMBINED FORMATS FOR OUTPUT FILE
    //--------------------------------------------------------------------------

    /**
     * List of headers for the data members.
     * @return header data fields
     */
    @Override
    public Object[] getTitle() {
        Object[] s = getSaveOutputHeader();
        Object[] h = getHardwareTitle();

        Object[] entries = new Object[s.length + h.length];

        System.arraycopy(s, 0, entries, 0, s.length);
        System.arraycopy(h, 0, entries, s.length, h.length);

        return entries;
    }

    /**
     * List of headers for the data members.
     * @return header data fields
     */
    @Override
    public Object[] getOutput() {
        Object[] s = getSaveOutputMessage();
        Object[] h = getHardwareMessage();

        Object[] entries = new Object[s.length + h.length];

        System.arraycopy(s, 0, entries, 0, s.length);
        System.arraycopy(h, 0, entries, s.length, h.length);

        return entries;
    }

    //--------------------------------------------------------------------------

}
