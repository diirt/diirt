/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CSVWriter {
    /**
     * Quote delimiter for a .CSV formatted output file.
     */
    public static final String QUOTE = "\"";
    
    /**
     * Comma delimiter for a .CSV formatted output file.
     */
    public static final String DELIM = ",";
    
    private CSVWriter(){}
    
    /**
     * Creates a CSV file (without overriding) based on the file name
     * and writes the header and row data to the file.
     * <p>
     * The header and all row data must have the same number of entries as
     * specified by the columns parameter.
     * 
     * @param cols number of entries per row of the CSV
     * @param filename file path + name of file (Excluding .CSV extension)
     * @param header data for the header, must have <i>cols</i> entries
     * @param rows data of the csv, each row must have <i>cols</i> entries
     * @return generated CSV file with data written to it,
     *         null if invalid file write
     */
    public static File createCSV(int cols, String filename, List header, List<List> rows){
        File csv = createFile(filename);
        
        //Invalid header
        if (header == null){
            throw new IllegalArgumentException("Cannot have null header data.");
        }
        
        //Invalid rows
        if (rows == null){
            throw new IllegalArgumentException("Cannot have null rows data.");
        }
        
        //Invalid header
        if (header.size() != cols){
            throw new IllegalArgumentException("Header must match the number of columns");
        }
        
        //Invalid rows
        for (List<Object> row: rows){
            if (row == null){
                throw new IllegalArgumentException("A row cannot be null.");
            }
            
            if (row.size() != cols){
                throw new IllegalArgumentException("Each row must match the number of columns.");
            }
        }
        
        //Invalid file write
        if (csv == null){
            return csv;
        }
        
        //Writes data
        writeHeader(csv, header);
        writeData(csv, rows);
        
        return csv;
    }
    
    public static File createNewFile(String filename){
        try {
            File outputFile = new File(filename + ".csv");
            
            //Creates File
            outputFile.createNewFile();
            
            return outputFile;
            
        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
    }
    
    public static File createFile(String filename){
        try {
            File outputFile = new File(filename + ".csv");
            
            //Prevent File Overwrite
            int tmp = 1;
            while (outputFile.exists()){
                outputFile = new File(filename + ".csv" + "." + tmp);
                tmp++;
            }
            
            //Creates File
            outputFile.createNewFile();
            
            return outputFile;
            
        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static void writeHeader(File csvFile, List header){
        //Writes header
        writeRow(csvFile, header);
    }
    
    public static void writeData(File csvFile, List<List> rows){
        //Invalid File
        if (csvFile == null){
            throw new IllegalArgumentException("Cannot write to a null file.");
        }
        
        //Invalid data
        if (rows == null){
            throw new IllegalArgumentException("Must have non-null data to write.");
        }
        
        //Writes all rows
        for (List<Object> row: rows){
            writeRow(csvFile, row);
        }     
    }
    
    public static void writeRow(File csvFile, List row){
        //Invalid File
        if (csvFile == null){
            throw new IllegalArgumentException("Cannot write to a null file.");
        }
        
        //Invalid data
        if (row == null){
            throw new IllegalArgumentException("Must have non-null data to write.");
        }
        
        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile, true)))) {
                
                //Write Entries
                for (Object entry: row){
                    out.print(formatEntry(entry));
                }
                
                //Clean-up
                out.println();                
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Output errors exist.");
        }            
    }
    
    public static void writeRow(File csvFile, Object data){
        List rows = new ArrayList<>();
        rows.add(data);
        writeRow(csvFile, rows);
    }
    
    private static String formatEntry(Object entry){
        if (entry instanceof Integer || entry instanceof Double ||
            entry instanceof Float || entry instanceof Short ||
            entry instanceof Long){
            
            return entry.toString();
        }
        else{
            return QUOTE + entry.toString() + QUOTE + DELIM;
        }
    }
}
