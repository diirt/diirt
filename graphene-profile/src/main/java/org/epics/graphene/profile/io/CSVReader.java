/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    /**
     * Quote delimiter for a .CSV formatted output file.
     */
    public static final String QUOTE = "\"";
    
    /**
     * Comma delimiter for a .CSV formatted output file.
     */
    public static final String DELIM = ",";
    
    private CSVReader() {}
    
    //Parsing CSV File
    
    public static List<String>          parseRow(String row){
        if (row == null){
            throw new IllegalArgumentException("Requires non-null row data.");
        }        
        
        return Arrays.asList(row.split(DELIM));
    }
    
    public static List<List<String>>    parseRows(List<String> rows){
        if (rows == null){
            throw new IllegalArgumentException("Requires non-null list of rows.");
        }
        
        List<List<String>> parsed = new ArrayList<>();
        

        for (String row: rows){
            if (row == null){
                throw new IllegalArgumentException("Requires non-null row data.");
            }
            
            parsed.add( CSVReader.parseRow(row) );
        }
        
        return parsed;
    }

    public static List<String>          readRows(File csvFile){
        CSVFinder.validateCSV(csvFile);
        
        List<String> rows = new ArrayList<>();
        String row;
        FileReader fr = null;
        BufferedReader reader;
        
        try{
            fr = new FileReader(csvFile);
            reader = new BufferedReader(fr);
            
            while ( (row = reader.readLine()) != null ){
                rows.add(row);
            }
            
            reader.close();
        }
        catch(FileNotFoundException ex){
            throw new IllegalArgumentException("The file " + csvFile.getPath() + " does not exist");
        }
        catch (IOException ex){
            System.err.println("Could not close BufferedReader stream.");
        }
        finally{
            try {             
                if (fr != null){
                    fr.close();
                }
            }
            catch (IOException ex){
                System.err.println("Could not close FileReader stream.");
            }
        }
        
        return rows;
    }
    
    public static List<List<String>>    parseCSV(File csvFile){
        CSVFinder.validateCSV(csvFile);
        
        return parseRows(readRows(csvFile));
    }
    
    
    //Validating CSV File
    public static void validate2DTablesNames(File csvA, File csvB){
        CSVFinder.validateCSV(csvA);
        CSVFinder.validateCSV(csvB);
        
        //{Date, Graph Type, Table.csv}
        String[] compA = csvA.getName().split("-");
        String[] compB = csvB.getName().split("-");
        
        if (compA.length != 3){
            throw new IllegalArgumentException("File A is name incorrectly");
        }
        if (compB.length != 3){
            throw new IllegalArgumentException("File B is name incorrectly");
        }      
        
        if (!compA[1].equals(compB[1])){
            throw new IllegalArgumentException("The files are not the same graph type.");
        }
    }
    
    public static void validate2DTables(List<List<String>> dataA, List<List<String>> dataB){
        if (dataA == null){
            throw new IllegalArgumentException("Data must not be null.");            
        }
        if (dataB == null){
            throw new IllegalArgumentException("Data must not be null.");
        }
        
        if (dataA.size() != dataB.size()){
            throw new IllegalArgumentException("Must have same number of rows.");            
        }
        
        int rows = dataA.size();
        
        for (int i = 0; i < rows; ++i){
            if (dataA.get(i) == null){
                throw new IllegalArgumentException("Row " + i  + " of dataset 1 is null.");
            }
            
            if (dataB.get(i) == null){
                throw new IllegalArgumentException("Row " + i  + " of dataset 2 is null.");                
            }
            
            if (dataA.get(i).size() != dataB.get(i).size()){
                throw new IllegalArgumentException("Row " + i  + " of dataset 1 and dataset2 must have the same number of columns.");                
            }
        }
    }
    
}
