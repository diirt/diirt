/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author carcassi
 */
public class SwingBindingUtil {

    public static void onTextFieldChange(JTextField field, final Runnable task) {
        field.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                task.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                task.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                task.run();
            }
        });
    }

    public static void onCheckBoxChange(JCheckBox field, final Runnable task) {
        field.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                task.run();
            }
        });
    }

}
