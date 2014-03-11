/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.utils;

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
    
    private String datasetMessage = "",
                  saveMessage = "",
                  authorMessage = "";
    
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
    
    
    
    //FORMATS FOR OUTPUT FILE
    
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
    private String getSaveOutputHeader(){
        return        
                       QUOTE + "Dataset Comments" + QUOTE + DELIM +
                       QUOTE + "Author" + QUOTE + DELIM +               
                       QUOTE + "General Message" + QUOTE;        
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
    private String getSaveOutputMessage(){
        String quote = "\"";
        String delim = ",";
        
        return
                         quote + getDatasetMessage() + quote + delim +
                         quote + getAuthorMessage() + quote + delim +
                         quote + getSaveMessage() + quote;    
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
    private String getHardwareTitle(){
        String quote = "\"";
        String delim = ",";

        return
                         quote + "JVM Version" + quote + delim +
                         quote + "Available Memory (GB)"+ quote + delim +
                         quote + "RAM (GB)" + quote + delim +
                         quote + "OS" + quote + delim +
                         quote + " OS Version" + quote;
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
    private String getHardwareMessage(){
        String quote = "\"";
        String delim = ",";
                
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
        
        return
                         quote + javaVersion + quote + delim +
                         String.format("%.3f", runtime) + delim +
                         String.format("%.3f", memorySize) + delim +
                         quote + os + quote + delim +
                         quote + osVersion + quote;
    }

    
    //COMBINED FORMATS FOR OUTPUT FILE
    
    @Override
    public String getTitle() {
        return getSaveOutputHeader() + DELIM + getHardwareTitle();
    }

    @Override
    public String getOutput() {
        return getSaveOutputMessage() + DELIM + getHardwareMessage();
    }
    
    
}
