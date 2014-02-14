/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.lang.management.ManagementFactory;

/**
 *
 * @author asbarber
 */
public class SaveSettings {
    private static final double BYTES_TO_GB = Math.pow(2, 30);
    
    public static final String QUOTE = "\"";
    public static final String DELIM = ",";
    
    private String datasetMessage = "",
                  saveMessage = "",
                  authorMessage = "";
    
    /**
     * Set the comment associated with the data set.
     * This comment will be written to the CSV log file when saving the statistics.
     * 
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
     * 
     * This is appropriate for discussing the parameters of the renderer, etc.
     * 
     * @param message general comment about the profiling
     */    
    public void setSaveMessage(String message){
        this.saveMessage = message;
    }
    
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
    
    public String getAuthorMessage(){
        return this.authorMessage;
    }    
    
    
    public String getOutputTitle(){
        return        
                       QUOTE + "Dataset Comments" + QUOTE + DELIM +
                       QUOTE + "Author" + QUOTE + DELIM +               
                       QUOTE + "General Message" + QUOTE;        
    }
    
    public String getOutputMessage(){
        String quote = "\"";
        String delim = ",";
        
        return
                         quote + getDatasetMessage() + quote + delim +
                         quote + getAuthorMessage() + quote + delim +
                         quote + getSaveMessage() + quote;    
    }
    
    
    public String getHardwareOutputTitle(){
        String quote = "\"";
        String delim = ",";

        return
                         quote + "JVM Version" + quote + delim +
                         quote + "Available Memory (GB)"+ quote + delim +
                         quote + "RAM (GB)" + quote;      
    }
    
    public String getHardwareOutputMessage(){
        String quote = "\"";
        String delim = ",";
                
        //Get Environment Properties
        String javaVersion = System.getProperty("java.version");
        
        //Format
        javaVersion = javaVersion == null ? "": javaVersion;
        
        //Memory
        double runtime  = Runtime.getRuntime().maxMemory() / SaveSettings.BYTES_TO_GB;
        double memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / SaveSettings.BYTES_TO_GB;
        
        return
                         quote + javaVersion +quote + delim +
                         String.format("%.3f", runtime) + delim +
                         String.format("%.3f", memorySize);        
    }
}
