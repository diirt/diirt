/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.diirt.util.time.TimeDuration;
import org.diirt.vtype.VMultiDouble;
import org.diirt.datasource.sim.SimulationDataSource;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReaderListener;

import javax.swing.table.TableModel;

import org.diirt.datasource.PVReaderEvent;
import org.diirt.vtype.VDouble;

import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static org.diirt.util.concurrent.Executors.swingEDT;
import static java.time.Duration.*;

/**
 *
 * @author carcassi
 */
public class MockSyncArrayTableFrame extends javax.swing.JFrame {

    /** Creates new form MockPVFrame */
    public MockSyncArrayTableFrame() {
        PVManager.setDefaultNotificationExecutor(swingEDT());
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pvTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        scanRateSpinner = new javax.swing.JSpinner();
        createPVButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        nPVSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        updateRateSpinner = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(pvTable);

        jLabel6.setText("UI scan rate (Hz):");

        scanRateSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));

        createPVButton.setText("Create ");
        createPVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPVButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("N PVs:");

        nPVSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel3.setText("PV update rate (Hz):");

        updateRateSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addComponent(createPVButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scanRateSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nPVSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateRateSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(updateRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scanRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nPVSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createPVButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    PVReader<VMultiDouble> pv;

    private void createPVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPVButtonActionPerformed
        if (pv != null)
            pv.close();

        int nPvs = ((Integer) nPVSpinner.getModel().getValue()).intValue();
        double timeIntervalSec = (1.0 / ((Integer) updateRateSpinner.getModel().getValue()).intValue());
        String pvName = "ramp(-1.5, 1.5, 0.1, " + timeIntervalSec + ")";
        int scanRate = ((Integer) scanRateSpinner.getModel().getValue()).intValue();

        // Buffer depth has to be longest between the time between scan and
        // the time between sample multiplied by 5 (so you get at least 5 samples).
        double bufferDepth = Math.max(timeIntervalSec * 5.0, (1.0 / scanRate));

        pv = PVManager.read(synchronizedArrayOf(ofMillis(75), TimeDuration.ofSeconds(bufferDepth),
                     vDoubles(Collections.nCopies(nPvs, pvName))))
                .readListener(new PVReaderListener<VMultiDouble>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VMultiDouble> event) {
                        final List<VDouble> values = pv.getValue().getValues();
                        if (values != null) {
                            TableModel model = new AbstractTableModel() {

                                List<String> names = Arrays.asList("Value", "Timestamp");

                                @Override
                                public int getRowCount() {
                                    return values.size();
                                }

                                @Override
                                public int getColumnCount() {
                                    return names.size();
                                }

                                @Override
                                public String getColumnName(int column) {
                                    return names.get(column);
                                }

                                @Override
                                public Object getValueAt(int rowIndex, int columnIndex) {
                                    if (values.get(rowIndex) == null)
                                        return null;
                                    switch(columnIndex) {
                                        case 0:
                                            return values.get(rowIndex).getValue();
                                        case 1:
                                            return values.get(rowIndex).getTimestamp();
                                    }
                                    throw new IllegalStateException();
                                }
                            };
                            pvTable.setModel(model);
                        }
                    }
                })
                .maxRate(TimeDuration.ofHertz(scanRate));

    }//GEN-LAST:event_createPVButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MockSyncArrayTableFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createPVButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner nPVSpinner;
    private javax.swing.JTable pvTable;
    private javax.swing.JSpinner scanRateSpinner;
    private javax.swing.JSpinner updateRateSpinner;
    // End of variables declaration//GEN-END:variables

}
