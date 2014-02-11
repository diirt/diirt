/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

/**
 *
 * @author asbarber
 */
public class Settings {
    public final String QUOTE = "\"";
    public final String DELIM = ",";
    
    public String datasetMessage = "",
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
}
