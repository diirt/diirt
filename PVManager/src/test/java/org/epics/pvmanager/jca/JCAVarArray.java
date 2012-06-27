/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;

/**
 *
 * @author carcassi
 */
public class JCAVarArray {
    public static void main(String[] args) {
        // Test CAJ
        JCADataSource jcaDataSource = new JCADataSource();
        System.out.println("Supports variable arrays: " + jcaDataSource.isVarArraySupported());
        jcaDataSource.close();
        
        // Test JCA
        jcaDataSource = new JCADataSource(JCALibrary.JNI_THREAD_SAFE, Monitor.VALUE | Monitor.ALARM);
        System.out.println("Supports variable arrays: " + jcaDataSource.isVarArraySupported());
        jcaDataSource.close();
    }
}
