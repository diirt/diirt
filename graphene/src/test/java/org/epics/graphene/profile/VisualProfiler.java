/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


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

    
    //Pane: General Settings
    private JComboBox           listRendererTypes;
    private JLabel              lblRendererTypes;
    
    private JTextField          txtTestTime;
    private JLabel              lblTestTime;
    
    private JTextField          txtMaxAttempts;
    private JLabel              lblMaxAttempts;
    
    //Tab: Single Profile
    private JLabel              lblDatasetSize;
    private JTextField          txtDatasetSize;
    
    private JLabel              lblImageWidth;
    private JTextField          txtImageWidth;
    
    private JLabel              lblImageHeight;
    private JTextField          txtImageHeight;
    
    private JLabel              lblSaveMessage;
    private JTextField          txtSaveMessage;
    
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
    
    //Pane: Console
    private JTextArea          console;
    private JLabel             lblConsole;
    
    public VisualProfiler(){
        super("Visual Profiler");
        
        initFrame();
        initComponents();
        loadLists();
        addListeners();
        addComponents();
        
        super.pack();
    }
    
    
    //Swing Setup
    
    private void initFrame(){
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }
    private void initComponents(){
        mainPanel = new JPanel();        
        mainPanel.setLayout(new BorderLayout());
        
        //General Settings
        listRendererTypes = new JComboBox(VisualProfiler.SUPPORTED_PROFILERS);
        lblRendererTypes = new JLabel("Renderer Type: ");
        
        txtTestTime = new JTextField("20");
        lblTestTime = new JLabel("Test Time: ");
                
        txtMaxAttempts = new JTextField("1000000");
        lblMaxAttempts = new JLabel("Max Attempts: ");
        
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
        
        lblSaveMessage = new JLabel("Save Message: ");
        txtSaveMessage = new JTextField("");
        
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
        
        
        //Console
        console = new JTextArea(20, 50);
        console.setEditable(false);
        lblConsole = new JLabel("Console");
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
    }    
    private void addComponents(){
        
        //General Settings
        JPanel settingsPane = new JPanel();
        settingsPane.setLayout(new GridLayout(0, 2));
        
            settingsPane.add(this.lblRendererTypes);
            settingsPane.add(this.listRendererTypes);

            settingsPane.add(this.lblTestTime);
            settingsPane.add(this.txtTestTime);

            settingsPane.add(this.lblMaxAttempts);
            settingsPane.add(this.txtMaxAttempts);
        
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

            singleProfileTab.add(lblSaveMessage);
            singleProfileTab.add(txtSaveMessage);
            
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
                multiLayerRight.add(btnStart, BorderLayout.NORTH);
        
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
        
        //Console
        JPanel consolePanel = new JPanel();
        consolePanel.setLayout(new BorderLayout());
        consolePanel.setBorder(BorderFactory.createLineBorder(Color.black));        
        consolePanel.add(lblConsole, BorderLayout.NORTH);
        consolePanel.add(new JScrollPane(console), BorderLayout.CENTER);
            
        //Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Single Profile", singleProfileTab);
        tabs.addTab("Multi Layer", multiLayerOuter);
        tabs.addTab("Control Panel", controlPane);
        
        //Add to panel hiearchy
        mainPanel.add(settingsPane, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(consolePanel, BorderLayout.SOUTH);
        super.add(mainPanel);
    }
    
    
    //Actions
    
    private void singleProfileAction(){
        String strDatasetSize = txtDatasetSize.getText();
        String strImageWidth = txtImageWidth.getText();
        String strImageHeight = txtImageHeight.getText();
        
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
        profiler.setSaveMessage(saveMessage);
        
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
            profilers.get(profilers.size()-1).setSaveMessage(saveMessage);            

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
    private void setEnabledActions(boolean enabled){
        this.btnSingleProfile.setEnabled(enabled);
        this.btnStart.setEnabled(enabled);
        this.btnCompareTables.setEnabled(enabled);
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
            
            this.multiProfiler = new VisualMultiLevelProfiler(profiler);
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
                //Spacing formatting
                int maxSpace = 40;
                int numSpaces = resolution.toString().length() +
                             (": ").length() + 
                             Integer.toString(datasetSize).length() +
                             (": ").length();
                String spacing = "";
                
                for (int count = 0; count < maxSpace - numSpaces; count++){
                    spacing += " ";
                }
                
                //Publishes
                ProfilerWorker.this.publish(resolution + ": " + datasetSize + ": " + spacing);
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