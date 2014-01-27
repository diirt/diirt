/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author asbarber
 */
public class ProfileAnalysis {
    public static String[] SUPPORTED_TABLES_1D = new String[]   {"Histogram1D.csv",
                                                                "IntensityGraph2D.csv",
                                                                "LineGraph2D.csv",
                                                                "ScatterGraph2D.csv",
                                                                "SparklineGraph2D.csv"
                                                                };
    /**
     * Brings a dialog box to ask the user to select two table based
     * CSV files and compares the differences of the tables.
     * <p>
     * Computes the difference of each cell between the first file selected
     * and the second file (first - second).
     * <p>
     * Precondition: both files selected are <code>MultiLevelProfiler</code>
     * table files of the same graph type.
     */
    public static void compareTables2D(){
        String message = "Select two Graphene table files (*.csv) to compute"
                       + " the timing differences of.";
        String hint    = "Note that the differences are calculated by:"
                       + " first file cell minus second file cell.";
        
        JOptionPane.showMessageDialog(null, message);
        JOptionPane.showMessageDialog(null, hint);
        
        File fileA, fileB;
        
        //File Choosers
        final JFileChooser fc = new JFileChooser(ProfileGraph2D.LOG_FILEPATH);
        fc.setFileFilter(new FileNameExtensionFilter("Graphene Tables (*.csv)", "csv"));
        
        //File A
        int returnValueA = fc.showOpenDialog(null);
        if (returnValueA == JFileChooser.APPROVE_OPTION){
            fileA = fc.getSelectedFile();
        }
        else{
            return;
        }
        
        //File B
        int returnValueB = fc.showOpenDialog(null);
        if (returnValueB == JFileChooser.APPROVE_OPTION){
            fileB = fc.getSelectedFile();
        }
        else{
            return;
        }
        
        compareTables2D(fileA, fileB);
    }
    
    /**
     * Computes the difference of each cell between the first file selected
     * and the second file (first - second).
     * <p>
     * Precondition: both files selected are <code>MultiLevelProfiler</code>
     * table files of the same graph type.
     */
    public static void compareTables2D(File fileA, File fileB){
        List<String[]> rowsOfA = readTable(fileA);
        List<String[]> rowsOfB = readTable(fileB);
        
        List<String[]> differenceRows = new ArrayList<>();
        
        //Ensures the same number of rows in both tables
        if (rowsOfA.size() != rowsOfB.size()){
            throw new IllegalArgumentException("The tables must have the same number of rows.");
        }
        
        //Every row (applies to both tables)
        for (int i = 0; i < rowsOfA.size(); i++){
            String[] rowA = rowsOfA.get(i);
            String[] rowB = rowsOfB.get(i);
            
            //Ensures same number of cells in the row of both tables
            if (rowA.length != rowB.length){
                throw new IllegalArgumentException("The tables must have the same number of columns.");
            }
            
            String[] diffRow = new String[rowA.length];
            
            //Column header (first cell in each row)
            if (rowA[0].equals(rowB[0])){
                diffRow[0] = rowA[0];
            }
            else{
                diffRow[0] = "*Error in reading this cell*";
            }
            
            //All cells in the row (applies to both tables) (starts at cell 1, ignores column header)
            for (int j = 1; j < rowA.length; j++){
                String cellA = rowA[j];
                String cellB = rowB[j];
                
                if (cellA == null || cellB == null){
                    diffRow[j] = "";
                }
                else{
                    //Tries to find a numerical values (for times)
                    try{
                        double numberA = Double.parseDouble(rowA[j]);
                        double numberB = Double.parseDouble(rowB[j]);
                        diffRow[j] = String.format("%.3f", (numberA - numberB)/numberA*100) + "%";
                    }catch(NumberFormatException e){
                        //When 
                        if (cellA.equals(cellB)){
                            diffRow[j] = cellA;
                        }
                        else{
                            diffRow[j] = "*Error In Reading This Cell*";
                        }
                    }                    
                }
            }
            
            differenceRows.add(diffRow);
        }
        
        
        
        //File Output
        String delim = ",";
        String filenameA = fileA.getName();
        String filenameB = fileB.getName();
        
        int delimA1 = filenameA.indexOf("-"),
            delimA2 = filenameA.lastIndexOf("-"),
            delimB1 = filenameB.indexOf("-");
                
            //How to name output file
            String graphType;        
            if (delimA2 == -1){
                graphType = "Unknown Graph Type";
            }
            else{
                graphType = filenameA.substring(delimA1 + 1, delimA2);
            }
            
        String dateA, dateB;
            if (delimA1 == -1){
                dateA = filenameA;
            }
            else{
                dateA = filenameA.substring(0, delimA1);
            }

            if (delimB1 == -1){
                dateB = filenameB;
            }
            else{
                dateB = filenameB.substring(0, delimB1);
            }
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String todaysDate = format.format(new Date());
       
        File outputFile = new File(ProfileGraph2D.LOG_FILEPATH + 
                                   todaysDate +
                                   "-" +
                                   graphType +
                                   "-Table Difference-" + 
                                   dateA +
                                   "vs" +
                                   dateB +
                                   ".csv");
        
        try {
            outputFile.createNewFile();
            
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
            
            //Prints rows
            for (String[] row: differenceRows){
                for (String element: row){
                    out.print(element + delim);
                }
                out.println();
            }
            
            out.close();
        } catch (IOException ex) {
            System.err.println("Output errors exist.");
        }        
    }    
    
    /**
     * Reads a <code>MultiLevelProfiler</code> table CSV file
     * and gets the input of every cell.
     * 
     * The output is a list of each row formatted as an array.
     * @param file
     * @return a list of each row, with the row formatted as an array
     */
    public static List<String[]> readTable(File file){
        List<String[]> rows = new ArrayList<>();
        String delim = ",";
        FileReader fr = null;
        
        try{
            fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            
            String header = reader.readLine();
            rows.add(header.split(delim));
            
            String row;
            while ( (row = reader.readLine()) != null ){
                rows.add(row.split(delim));
            }
            
            reader.close();
        }
        catch(FileNotFoundException ex){
            System.err.println("The first file could not be found.");
        }
        catch (IOException ex){
            System.err.println("Output errors exist.");
        } 
        finally {
           try {
               if (fr != null){
                   fr.close();
               }
           } 
           catch (IOException ex){
           }
        }
        
        return rows;
    }    
    
    
    public static List<String> analyzeTables1D(){
        List<File>              files = findTables1D();
        List<String>            results = new ArrayList<>();
        
        for (File tableFile: files){
            if (tableFile != null){
                List<String[]> tableRows = readTable(tableFile);

                //Includes a header plus two rows of data
                if (tableRows.size() > 2){
                    String[] previous = tableRows.get(tableRows.size() - 2);
                    String[] recent = tableRows.get(tableRows.size() - 1);

                    //Index 2 represents "Average Time"
                    //Index 0 represents "Graph Type"

                    double previousTime = Double.parseDouble(previous[2]);
                    double recentTime = Double.parseDouble(recent[2]);
                    String graphType = previous[0];

                    double percentChange = (recentTime - previousTime) / previousTime;
                    String performanceMessage;

                    //Signficant performance decrease
                    if (percentChange > 0.33){
                        performanceMessage = "Performance Decrease";
                    }
                    //Significant performance increase
                    else if (percentChange < -0.33){
                        performanceMessage = "Performance Increase";                    
                    }
                    //No Significant performance change
                    else{
                        performanceMessage = "Performance Stable";                    
                    }

                        results.add(graphType +
                                    ": " +
                                    performanceMessage +
                                    ": " + 
                                    String.format("%.3f", percentChange) +
                                    "% changed"
                                   );                
                } 
            }
        }
        
        results.add("\n" + results.size() + " files analyzed.");
        return results;
    }
    
    public static List<File> findTables1D(){
        List<File> tables1D = new ArrayList<>();
        
        File root = new File(ProfileGraph2D.LOG_FILEPATH);
        
        for (File subfile : root.listFiles()){
            for (int i = 0; i < SUPPORTED_TABLES_1D.length; i++){
                
                //If supported file name
                if (SUPPORTED_TABLES_1D[i].equals(subfile.getName())){
                    tables1D.add(subfile);
                    i = SUPPORTED_TABLES_1D.length;    //break
                }
                
            }
        }
        
        return tables1D;
    }
    
}
