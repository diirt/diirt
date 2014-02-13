/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


/**
 *
 * @author asbarber
 */
public class VisualProfiler extends JFrame{
    private static final String PROFILE_PATH = "org.epics.graphene.profile";
    public static final String[] SUPPORTED_PROFILERS = {"Histogram1D",
                                                        "IntensityGraph2D",
                                                        "LineGraph2D",
                                                        "ScatterGraph2D",
                                                        "SparklineGraph2D"
                                                       };
    
    private JPanel mainPanel;
    private JTabbedPane tabs;
    
    //Pane: General SaveSettings
    private JComboBox           listRendererTypes;
    private JLabel              lblRendererTypes;
    
    private JTextField          txtTestTime;
    private JLabel              lblTestTime;
    
    private JTextField          txtMaxAttempts;
    private JLabel              lblMaxAttempts;
    
    private JLabel              lblSaveMessage;
    private JTextField          txtSaveMessage;
    
    private JLabel              lblAuthorMessage;
    private JTextField          txtAuthorMessage;
    
    
    //Tab: Single Profile
    private JLabel              lblDatasetSize;
    private JTextField          txtDatasetSize;
    
    private JLabel              lblImageWidth;
    private JTextField          txtImageWidth;
    
    private JLabel              lblImageHeight;
    private JTextField          txtImageHeight;
    
    private JLabel              lblShowGraph;
    private JCheckBox           chkShowGraph;
    
    private JButton             btnSingleProfile;
    private JButton             btnSingleProfileAll;
    
    
    //Tab: Control Panel
    private JButton             btnCompareTables;
    private JButton             btnCompareTables1D;
    
    
    //Tab: Multi Layer
    private JLabel              lblResolutions,
                                lblNPoints;
    
    private JButton             btnStart;
    
    private JList<Resolution>               listResolutions;
    private JList<Integer>                  listNPoints;
    private DefaultListModel<Resolution>    modelResolutions;
    private DefaultListModel<Integer>       modelNPoints;
    
    
    //Tab: File Viewer
    private JTree tree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode treeRoot;
    private JButton btnTreeOpenFile;
    private JButton btnTreeDeleteFile;
    private JButton btnTreeRefresh;
    
    
    //Pane: Console
    private JTextArea          console;
    private JLabel             lblConsole;
    private JButton            btnClearLog;
    private JButton            btnSaveLog;
    
    private JLabel             lblTime;
    private JTextField         txtTime;
    
    
    public VisualProfiler(){
        super("Visual Profiler");
                
        initFrame();
        initComponents();
        loadLists();
        addListeners();        
        addComponents();
                
        login();
        
        finalizeFrame();
        
        startTimer();        
    }
    
    
    //Swing Setup
    
    private void initFrame(){
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void initComponents(){
        mainPanel = new JPanel();        
        mainPanel.setLayout(new BorderLayout());
        tabs = new JTabbedPane();
        
        //General SaveSettings
        listRendererTypes = new JComboBox(VisualProfiler.SUPPORTED_PROFILERS);
        lblRendererTypes = new JLabel("Renderer Type: ");
        
        txtTestTime = new JTextField("20");
        lblTestTime = new JLabel("Test Time: ");
                
        txtMaxAttempts = new JTextField("1000000");
        lblMaxAttempts = new JLabel("Max Attempts: ");
        
        lblSaveMessage = new JLabel("Save Message: ");
        txtSaveMessage = new JTextField("");
        
        lblAuthorMessage = new JLabel("Author: ");
        txtAuthorMessage = new JTextField("");
        
        
        //Tab: Single Profile
        //------------
        lblDatasetSize = new JLabel("Number of Data Points: ");
        txtDatasetSize = new JTextField("10000");
        
        lblImageWidth = new JLabel("Image Width: ");
        txtImageWidth = new JTextField("640");
        
        lblImageHeight = new JLabel("Image Height: ");
        txtImageHeight = new JTextField("480");
        
        lblShowGraph = new JLabel("Graph Results: ");
        chkShowGraph = new JCheckBox("Show Graph");
        
        btnSingleProfile = new JButton("Profile");
        btnSingleProfileAll = new JButton("Profile For All Renderers");
        
        
        //Tab: Control Panel
        //------------
        btnCompareTables = new JButton("Compare Profile Tables");
        btnCompareTables1D = new JButton("Analyze Single Profile Tables");
        
        
        //Tab: Multi Layer
        //------------
        lblResolutions = new JLabel("Resolutions");
        lblNPoints = new JLabel("N Points");
        btnStart = new JButton("Start");
        
        listResolutions = new JList<>();
        listNPoints = new JList<>();
        
        
        //Tab: File Browser
        //--------
        treeRoot = new DefaultMutableTreeNode(new File(ProfileGraph2D.LOG_FILEPATH));
        treeModel = new DefaultTreeModel(treeRoot);
        tree = new JTree(treeRoot){
            @Override
            public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){  
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                try{
                    File f = (File) node.getUserObject();
                    return f.getName();
                }
                catch(Exception e){
                    return node.toString();
                }
            }
        };        
        tree.setModel(treeModel);
        this.addNodes(treeRoot);
        tree.expandRow(0);
        btnTreeOpenFile = new JButton("Open File(s)");
        btnTreeDeleteFile = new JButton("Delete File(s)");
        btnTreeRefresh = new JButton("Refresh");
        
        //Console
        console = new JTextArea(20, 50);
        console.setEditable(false);
        lblConsole = new JLabel("Console");
        btnSaveLog = new JButton("Save Log");
        btnClearLog = new JButton("Clear Log");
        lblTime = new JLabel("Timer:");
        txtTime = new JTextField("00:00:00");
        txtTime.setEditable(false);
    }
    private void loadLists(){
        modelResolutions = new DefaultListModel<>();
        modelNPoints = new DefaultListModel<>();
        
        for (Resolution resolution : Resolution.defaultResolutions()){
            modelResolutions.addElement(resolution);
        }
        
        for (Integer datasetSize : MultiLevelProfiler.defaultDatasetSizes()){
            modelNPoints.addElement(datasetSize);
        }
                
        listResolutions.setModel(modelResolutions);
        listNPoints.setModel(modelNPoints);
    }
    private void addListeners(){
       this.btnSingleProfile.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.singleProfileAction();
           }
           
       });
       this.btnSingleProfileAll.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.singleProfileActionAll();
           }
           
       });
       
       this.btnStart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                VisualProfiler.this.startAction();
            }
            
        });
       
       this.btnCompareTables.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.compareTablesAction();
           }
           
       });
       this.btnCompareTables1D.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.analyzeTables1DAction();
           }
           
       });
       
       this.btnTreeOpenFile.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.openFiles();
           }
           
       });
       this.btnTreeDeleteFile.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.deleteFiles();
           }
           
       });
       this.btnTreeRefresh.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.refresh();
           }
           
       });
       
       this.btnSaveLog.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.saveLog();
           }
           
       });
       this.btnClearLog.addActionListener(new ActionListener(){

           @Override
           public void actionPerformed(ActionEvent e) {
               VisualProfiler.this.clearLog();
           }
           
       });
       
       
       this.tabs.addChangeListener(new ChangeListener(){

           @Override
           public void stateChanged(ChangeEvent e) {
               VisualProfiler.this.refresh(true);
           }
           
       });
    }    
    private void addComponents(){
        
        //General SaveSettings
        JPanel settingsPane = new JPanel();
        settingsPane.setLayout(new GridLayout(0, 2));
        
            settingsPane.add(this.lblRendererTypes);
            settingsPane.add(this.listRendererTypes);

            settingsPane.add(this.lblTestTime);
            settingsPane.add(this.txtTestTime);

            settingsPane.add(this.lblMaxAttempts);
            settingsPane.add(this.txtMaxAttempts);
        
            settingsPane.add(lblSaveMessage);
            settingsPane.add(txtSaveMessage);
            
            settingsPane.add(lblAuthorMessage);
            settingsPane.add(txtAuthorMessage);            
            
        //Tab: Single Profile
        JPanel singleProfileTab = new JPanel();
        singleProfileTab.setLayout(new GridLayout(0, 2));
        
            singleProfileTab.add(lblDatasetSize);
            singleProfileTab.add(txtDatasetSize);

            singleProfileTab.add(lblImageWidth);
            singleProfileTab.add(txtImageWidth);

            singleProfileTab.add(lblImageHeight);
            singleProfileTab.add(txtImageHeight);

            singleProfileTab.add(lblShowGraph);
            singleProfileTab.add(chkShowGraph);
            
            singleProfileTab.add(blankPanel(btnSingleProfile));
            singleProfileTab.add(blankPanel(btnSingleProfileAll));
        
            
        //Tab: Control Panel
        JPanel controlPane = new JPanel();        
            controlPane.add(this.btnCompareTables);
            controlPane.add(this.btnCompareTables1D);
        
        
        //Tab: Multi Layer
                JPanel multiLayerLeft = new JPanel();
                multiLayerLeft.setLayout(new BorderLayout());
                multiLayerLeft.add(lblResolutions, BorderLayout.NORTH);
                multiLayerLeft.add(new JScrollPane(listResolutions), BorderLayout.CENTER);

                JPanel multiLayerMiddle = new JPanel();
                multiLayerMiddle.setLayout(new BorderLayout());
                multiLayerMiddle.add(lblNPoints, BorderLayout.NORTH);
                multiLayerMiddle.add(new JScrollPane(listNPoints), BorderLayout.CENTER);

                JPanel multiLayerRight = new JPanel();
                multiLayerRight.setLayout(new BorderLayout());        
                multiLayerRight.add(blankPanel(btnStart), BorderLayout.NORTH);
        
            final JSplitPane multiLayerInner = new JSplitPane();
            final JSplitPane multiLayerOuter = new JSplitPane();

            multiLayerInner.setLeftComponent(multiLayerLeft);
            multiLayerInner.setRightComponent(multiLayerMiddle);

            multiLayerOuter.setLeftComponent(multiLayerInner);
            multiLayerOuter.setRightComponent(multiLayerRight);
            
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    multiLayerOuter.setDividerLocation(0.8);
                    multiLayerInner.setDividerLocation(0.8);                                        
                }
                
            });            
        
            
        //Tab: File Browser
        JSplitPane fileTab = new JSplitPane();
        
            JPanel fileTabRight = new JPanel();
            fileTabRight.setLayout(new BoxLayout(fileTabRight, BoxLayout.Y_AXIS));
            fileTabRight.add(this.btnTreeOpenFile);
            fileTabRight.add(this.btnTreeDeleteFile);
            fileTabRight.add(this.btnTreeRefresh);
            
        fileTab.setLeftComponent(new JScrollPane(tree));
        fileTab.setRightComponent(fileTabRight);
           
        
        //Console
        JPanel consolePanel = new JPanel();
        consolePanel.setLayout(new BorderLayout());
        consolePanel.setBorder(BorderFactory.createLineBorder(Color.black));   
        
            JPanel consoleBottom = new JPanel();
            consoleBottom.setLayout(new GridLayout(2, 2));
                consoleBottom.add(blankPanel(this.btnSaveLog));
                consoleBottom.add(blankPanel(this.btnClearLog));
            
                consoleBottom.add(blankPanel(this.lblTime));
                consoleBottom.add(blankPanel(this.txtTime));
            
            
            consolePanel.add(lblConsole, BorderLayout.NORTH);
            consolePanel.add(new JScrollPane(console), BorderLayout.CENTER);
            consolePanel.add(consoleBottom, BorderLayout.SOUTH);
            
        
        //Tabs
        tabs.addTab("Single Profile", singleProfileTab);
        tabs.addTab("Multi Layer", multiLayerOuter);
        tabs.addTab("Control Panel", controlPane);
        tabs.addTab("File Browser", fileTab);
        
        
        //Add to panel hiearchy
        mainPanel.add(settingsPane, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(consolePanel, BorderLayout.SOUTH);
                
        super.add(mainPanel);
        
        
    }
    private void finalizeFrame(){
        super.setVisible(true);
        super.pack();
        super.setLocationRelativeTo(null);  //Centers
    }
    
    private void login(){
        String input = null;
        boolean exit = false;
        final String cancelMessage = "Do you want to exit the application?";
        
        //Validate
        while (!exit){
            input = JOptionPane.showInputDialog(null, "Username: ", "Login",
                                                JOptionPane.PLAIN_MESSAGE);
            
            //Warning
            if (input == null || input.equals("")){
                int cancel = JOptionPane.showConfirmDialog(null, cancelMessage,
                                                           "Cancel",
                                                           JOptionPane.YES_NO_OPTION);
                
                //User wants to close program
                if (cancel == JOptionPane.YES_OPTION){
                    exit = true;
                    this.processWindowEvent(new WindowEvent(
                                            this, WindowEvent.WINDOW_CLOSING));
                }
            }
            //Valid, exits looop
            else{
                exit = true;
            }
        }
        
        this.txtAuthorMessage.setText(input);
    }
    
    
    //Actions
    
    private void singleProfileAction(){
        String strDatasetSize = txtDatasetSize.getText();
        String strImageWidth = txtImageWidth.getText();
        String strImageHeight = txtImageHeight.getText();
        String strAuthor = this.txtAuthorMessage.getText();
        
        int datasetSize;
        int imageWidth;
        int imageHeight;
        String saveMessage = this.txtSaveMessage.getText();
        final boolean showGraphs = this.chkShowGraph.isSelected();
        final ProfileGraph2D profiler = getProfiler();
        
        //Invalid Profiler
        if (profiler == null){
            return;
        }
        
        //Datset Size
        try{
            datasetSize = Integer.parseInt(strDatasetSize);
            
            if (datasetSize <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the dataset size.", "Error", JOptionPane.ERROR_MESSAGE);
            return;            
        }
        
        //Image Width
        try{
            imageWidth = Integer.parseInt(strImageWidth);
            
            if (imageWidth <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the image width.", "Error", JOptionPane.ERROR_MESSAGE);
            return;            
        }        
        
        //Image Height
        try{
            imageHeight = Integer.parseInt(strImageHeight);
            
            if (imageHeight <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the image height.", "Error", JOptionPane.ERROR_MESSAGE);
            return;            
        }   
        
        //Applies setting changes
        profiler.setNumDataPoints(datasetSize);
        profiler.setImageWidth(imageWidth);
        profiler.setImageHeight(imageHeight);
        profiler.getSaveSettings().setSaveMessage(saveMessage);
        profiler.getSaveSettings().setAuthorMessage(strAuthor);
        
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);
                
                ///Begin message
                publish("--------\n");
                publish(profiler.getGraphTitle() + ": Single Profile\n\n");
                
                //Runs
                publish("Running...\n");
                profiler.profile();
                publish("Running finished.\n");
                
                //Saves
                publish("Saving...\n");
                profiler.saveStatistics();
                publish("Saving finished.\n");
                
                //Displays results graph if checked
                if (showGraphs){
                    publish("\nGraphing Results...\n");
                    profiler.graphStatistics();
                    publish("Graphing Complete.\n");
                }
                
                //Finish message
                publish("\nProfiling completed.\n");
                publish("--------\n");
                
                setEnabledActions(true);
                
                return null;
            }
            
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }            
        };
        worker.execute();
    }
    private void singleProfileActionAll(){
        //Get inputs
        String strTestTime = this.txtTestTime.getText();
        String strMaxAttempts = this.txtMaxAttempts.getText();
        
        //Intended variables
        int testTime;
        int maxAttempts;
                
        //Test Time
        try{
            testTime = Integer.parseInt(strTestTime);
            
            if (testTime <= 0){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for test time.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Test Time
        try{
            maxAttempts = Integer.parseInt(strMaxAttempts);
            
            if (maxAttempts <= 0){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for max attempts.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        String strDatasetSize = txtDatasetSize.getText();
        String strImageWidth = txtImageWidth.getText();
        String strImageHeight = txtImageHeight.getText();
        
        int datasetSize;
        int imageWidth;
        int imageHeight;
        String saveMessage = this.txtSaveMessage.getText();
        final boolean showGraphs = this.chkShowGraph.isSelected();
        
        //Datset Size
        try{
            datasetSize = Integer.parseInt(strDatasetSize);
            
            if (datasetSize <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the dataset size.", "Error", JOptionPane.ERROR_MESSAGE);
            return;            
        }
        
        //Image Width
        try{
            imageWidth = Integer.parseInt(strImageWidth);
            
            if (imageWidth <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the image width.", "Error", JOptionPane.ERROR_MESSAGE);
            return;            
        }        
        
        //Image Height
        try{
            imageHeight = Integer.parseInt(strImageHeight);
            
            if (imageHeight <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the image height.", "Error", JOptionPane.ERROR_MESSAGE);
            return;            
        }   
        
        
        //Profile Creation
        final List<ProfileGraph2D> profilers = new ArrayList<>();
        
        
        for (int i = 0; i < VisualProfiler.SUPPORTED_PROFILERS.length; i++){
            
            //Instance creation                
            try {
                Class profileClass = Class.forName(PROFILE_PATH + ".Profile" + VisualProfiler.SUPPORTED_PROFILERS[i]);
                profilers.add((ProfileGraph2D) profileClass.newInstance());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "This class is not currently accessible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (InstantiationException ex) {
                return;
            } catch (IllegalAccessException ex) {
                return;
            }
            
            //Update
            profilers.get(profilers.size()-1).setTestTime(testTime);
            profilers.get(profilers.size()-1).setMaxTries(maxAttempts);   
            profilers.get(profilers.size()-1).setNumDataPoints(datasetSize);
            profilers.get(profilers.size()-1).setImageWidth(imageWidth);
            profilers.get(profilers.size()-1).setImageHeight(imageHeight);
            profilers.get(profilers.size()-1).getSaveSettings().setSaveMessage(saveMessage);            

        }

        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);
                
                for (final ProfileGraph2D profiler: profilers){
                    ///Begin message
                    publish("--------\n");
                    publish(profiler.getGraphTitle() + ": Single Profile\n\n");

                    //Runs
                    publish("Running...\n");
                    profiler.profile();
                    publish("Running finished.\n");

                    //Saves
                    publish("Saving...\n");
                    profiler.saveStatistics();
                    publish("Saving finished.\n");

                    //Displays results graph if checked
                    if (showGraphs){
                        publish("\nGraphing Results...\n");
                        profiler.graphStatistics();
                        publish("Graphing Complete.\n");
                    }

                    //Finish message
                    publish("\nProfiling completed.\n");
                    publish("--------\n");
                }
                
                setEnabledActions(true);

                return null;
            }


            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }            
        };
        worker.execute();   
    }
    private void startAction(){
        List<Resolution> resolutions = listResolutions.getSelectedValuesList();
        List<Integer> datasetSizes = listNPoints.getSelectedValuesList();
        ProfileGraph2D profiler = this.getProfiler();

        if (!resolutions.isEmpty() && !datasetSizes.isEmpty() && getProfiler() != null){
            ProfilerWorker worker = new ProfilerWorker(profiler, resolutions, datasetSizes);
            worker.execute();
        }    
        else{
            JOptionPane.showMessageDialog(null, "Profiling was cancelled due to invalid settings.", "Run Fail", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void compareTablesAction(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);                
                publish("--------\n");
                publish("Compare Tables\n");
                ProfileAnalysis.compareTables2D();   
                publish("\nComparison completed.\n");
                publish("--------\n");
                setEnabledActions(true);                
                return null;
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }
        };
        worker.execute();
    }
    private void analyzeTables1DAction(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);                
                publish("--------\n");
                publish("Comparing Single Profile Tables\n\n");
                
                List<String> output = ProfileAnalysis.analyzeTables1D();
                for (String out: output){
                    publish(out + "\n");
                }
                
                publish("\nComparison completed.\n");
                publish("--------\n");
                setEnabledActions(true);                
                return null;
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }
        };
        worker.execute();        
    }
    private void openFiles(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);                
                publish("--------\n");
                publish("Opening Files\n\n");
                
                Desktop desktop = Desktop.getDesktop();
                TreePath[] paths = tree.getSelectionPaths();

                if (paths != null && desktop != null){
                    for (TreePath path: paths){
                        if (path != null){
                            try{
                                File toOpen = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                                desktop.open(toOpen);
                                
                                publish(toOpen.getName() + "...opened successfully!\n");
                            }
                            catch(IOException e){
                                publish("Unable to open: " +
                                        ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() +
                                        "\n"
                                       );
                            }
                            catch(ClassCastException e){
                                //unable to open
                            }
                        }
                    }
                }
                
                publish("\nFile operations completed.\n");
                publish("--------\n");
                setEnabledActions(true);                
                return null;
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }
        };
        worker.execute();          
    }
    private void deleteFiles(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);                
                publish("--------\n");
                publish("Opening Files\n\n");
                
                TreePath[] paths = tree.getSelectionPaths();

                if (paths != null){
                    for (TreePath path: paths){
                        if (path != null){
                            try{
                                File toDelete = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                                
                                Files.delete(toDelete.toPath());
                                publish(toDelete.getName() + "...deleted successfully!\n");
                            }
                            catch(IOException e){
                                publish("Unable to delete: " +
                                        ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() +
                                        "\n"
                                       );
                            }
                            catch(ClassCastException e){
                                //unable to open
                            }
                        }
                    }
                }
                
                VisualProfiler.this.refreshNodes();
                
                publish("\nFile operations completed.\n");
                publish("--------\n");
                setEnabledActions(true);                
                return null;
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }
        };
        worker.execute();     
        
    }
    private void refresh(){
        this.refresh(false);
    }
    private void refresh(final boolean silent){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                if (!silent){
                    setEnabledActions(false);                
                    publish("--------\n");
                    publish("Refreshing File Browser\n");
                }
                
                VisualProfiler.this.refreshNodes();
                
                if (!silent){
                    publish("Refresh completed.\n");
                    publish("--------\n");
                    setEnabledActions(true);                
                }

                return null;                
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }
        };
        worker.execute();            
    }    
    private void saveLog(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                setEnabledActions(false);                
                
                //Where saving occurs
                saveFile();
                
                publish("--------\n");
                publish("Saving Log\n\n");
                
                //Saves beforehand to prevent this from being in log
                
                publish("\nSaving completed.\n");
                publish("--------\n");
                setEnabledActions(true);                
                return null;
            }
            
            private void saveFile(){
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = format.format(new Date());         
                
                //Creates file
                File outputFile = new File(ProfileGraph2D.LOG_FILEPATH + 
                                  date + 
                                  "-Log.txt");
                
                try {
                    outputFile.createNewFile();

                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

                    //Prints console
                    out.print(VisualProfiler.this.console.getText());

                    out.close();
                } catch (IOException ex) {
                    System.err.println("Output errors exist.");
                }                
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.print(chunk);
                }
            }
        };
        worker.execute();           
    }
    private void clearLog(){
        console.setText("");
    }

    private void setEnabledActions(boolean enabled){
        this.btnCompareTables.setEnabled(enabled);
        this.btnCompareTables1D.setEnabled(enabled);
        this.btnSingleProfile.setEnabled(enabled);
        this.btnSingleProfileAll.setEnabled(enabled);
        this.btnStart.setEnabled(enabled);
        this.btnTreeOpenFile.setEnabled(enabled);
        this.btnTreeDeleteFile.setEnabled(enabled);
        this.btnTreeRefresh.setEnabled(enabled);
        this.btnSaveLog.setEnabled(enabled);
        this.btnClearLog.setEnabled(enabled);
    }

    private void startTimer(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask(){

                    @Override
                    public void run() {         
                        publish(getTime());
                    }
                                      
                    }
                    
                    , 1000, 1000
                );
                return null;
            }

            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfiler.this.txtTime.setText(chunk);
                }
            }
        };
        worker.execute();   
    }
    
    
    //Helper
    
    //Returns null if unable to get a profiler
    public ProfileGraph2D getProfiler(){
        //Get inputs
        String strClass = listRendererTypes.getSelectedItem().toString();
        String strTestTime = this.txtTestTime.getText();
        String strMaxAttempts = this.txtMaxAttempts.getText();
        
        //Intended variables
        int testTime;
        int maxAttempts;
        ProfileGraph2D renderer;
                
        //Test Time
        try{
            testTime = Integer.parseInt(strTestTime);
            
            if (testTime <= 0){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for test time.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Test Time
        try{
            maxAttempts = Integer.parseInt(strMaxAttempts);
            
            if (maxAttempts <= 0){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for max attempts.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Instance creation                
        try {
            Class profileClass = Class.forName(PROFILE_PATH + ".Profile" + strClass);
            renderer = (ProfileGraph2D) profileClass.newInstance();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "This class is not currently accessible.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (InstantiationException ex) {
            Logger.getLogger(VisualProfiler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(VisualProfiler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        //Update
        renderer.setTestTime(testTime);
        renderer.setMaxTries(maxAttempts);
        
        //Final Format
        return renderer;
    }
    private void print(final String output){
        console.append(output);
    }
    private class ProfilerWorker extends SwingWorker<Object, String>{
        private VisualMultiLevelProfiler multiProfiler;
        
        public ProfilerWorker(ProfileGraph2D profiler, List<Resolution> resolutions, List<Integer> datasetSizes){
            setEnabledActions(false);            
            publish("--------\n");
            publish(profiler.getGraphTitle() + "\n\n");
            
            String strAuthor = VisualProfiler.this.txtAuthorMessage.getText();
            String saveMessage = VisualProfiler.this.txtSaveMessage.getText();   
            
            this.multiProfiler = new VisualMultiLevelProfiler(profiler);
            this.multiProfiler.getSaveSettings().setAuthorMessage(strAuthor);
            this.multiProfiler.getSaveSettings().setSaveMessage(saveMessage);
            this.multiProfiler.setImageSizes(resolutions);
            this.multiProfiler.setDatasetSizes(datasetSizes);
        }
        
        @Override
        protected Object doInBackground() throws Exception {
            this.multiProfiler.run();
            this.multiProfiler.saveStatistics();
            publish("\nProfiling complete." + "\n");
            publish("--------\n");
            setEnabledActions(true);
            
            return true;
        }
        
        @Override
        protected void process(List<String> chunks){
            for (String chunk: chunks){
                VisualProfiler.this.print(chunk);
            }
        }        
                
        private class VisualMultiLevelProfiler extends MultiLevelProfiler{
            public VisualMultiLevelProfiler(ProfileGraph2D profiler){
                super(profiler);
            }

            @Override
            public void processTimeWarning(int estimatedTime){
                ProfilerWorker.this.publish("The estimated run time is " + estimatedTime + " seconds." + "\n\n");
            }

            @Override
            public void processPreResult(Resolution resolution, int datasetSize){
                //Publishes
                ProfilerWorker.this.publish(resolution + ": " + datasetSize + ":" + "    ");
            }

            @Override
            public void processResult(Resolution resolution, int datasetSize, Statistics stats){
                ProfilerWorker.this.publish(stats.getAverageTime() + "ms" + "\n");                    
            }        
        };        
    };
    
    private JPanel blankPanel(Component itemToAdd){
        JPanel tmp = new JPanel();
        tmp.add(itemToAdd);
        return tmp;
    }
    
    private void addNodes(DefaultMutableTreeNode parentNode){
        File[] subfiles = ((File)parentNode.getUserObject()).listFiles();
        
        if (subfiles != null){
            for (File subfile: subfiles){
                if (subfile != null){
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(subfile);
                    parentNode.add(childNode);
                    this.addNodes(childNode);
                }
            }
        }
    }
    private void refreshNodes(){
        this.treeRoot.removeAllChildren();
        this.addNodes(treeRoot);
        
        this.treeModel.nodeStructureChanged(this.treeRoot);
        this.repaint();              
    }
    
    private String getTime(){
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        String format = "%02d";     

        return String.format(format, hour) +
               ":" +
               String.format(format, minute) +
               ":" +
               String.format(format, second);
                                
    }
    
    //Static
    
    public static void invokeVisualAid(){
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                VisualProfiler frame = new VisualProfiler();
            }
            
        });
    }  
    public static void main(String[] args){
        VisualProfiler.invokeVisualAid();
    }
}