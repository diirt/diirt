/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
    
    //Tab: Control Panel
    
    //Tab: Multi Layer
    private JLabel              lblResolutions,
                                lblNPoints;
    
    private JButton             btnStart;
    
    private JList<Resolution>               listResolutions;
    private JList<Integer>                  listNPoints;
    private DefaultListModel<Resolution>    modelResolutions;
    private DefaultListModel<Integer>       modelNPoints;
    
    private JTextArea          console;
    
    
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
        
        //Tab: Multi Layer
        //------------
        
        lblResolutions = new JLabel("Resolutions");
        lblNPoints = new JLabel("N Points");
        btnStart = new JButton("Start");
        
        listResolutions = new JList<>();
        listNPoints = new JList<>();
        
        console = new JTextArea(20, 50);
        console.setEditable(false);
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
        btnStart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                VisualProfiler.this.startAction();
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
        
        
        //Tab: Multi Layer
        
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        left.add(lblResolutions, BorderLayout.NORTH);
        left.add(listResolutions, BorderLayout.CENTER);
        
        JPanel middle = new JPanel();
        middle.setLayout(new BorderLayout());
        middle.add(lblNPoints, BorderLayout.NORTH);
        middle.add(listNPoints, BorderLayout.CENTER);
        
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());        
        right.add(btnStart, BorderLayout.NORTH);
        right.add(console, BorderLayout.CENTER);
        
            JSplitPane inner = new JSplitPane();
            JSplitPane outer = new JSplitPane();

            inner.setLeftComponent(left);
            inner.setRightComponent(middle);

            outer.setLeftComponent(inner);
            outer.setRightComponent(right);
        
        //Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Multi Layer", outer);
        
        //Add to panel hiearchy
        mainPanel.add(settingsPane, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        super.add(mainPanel);
    }
    
    
    //Actions
    
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
            JOptionPane.showMessageDialog(null, "Error", "Enter a positive non-zero integer.", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Test Time
        try{
            maxAttempts = Integer.parseInt(strMaxAttempts);
            
            if (maxAttempts <= 0){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Error", "Enter a positive non-zero integer.", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Instance creation                
        try {
            Class profileClass = Class.forName(PROFILE_PATH + ".Profile" + strClass);
            renderer = (ProfileGraph2D) profileClass.newInstance();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error", "This class is not currently accessible.", JOptionPane.ERROR_MESSAGE);
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
