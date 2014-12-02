/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.diirt.support.ca.JCADataSourceBuilder;
import org.diirt.support.ca.JCADataSource;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;

/**
 *
 * @author carcassi
 */
public class JCAVarArray {
    public static void main(String[] args) {
        // Test CAJ
        JCADataSource jcaDataSource = new JCADataSourceBuilder().build();
        System.out.println("Supports variable arrays: " + jcaDataSource.isVarArraySupported());
        jcaDataSource.close();
        
        // Test JCA
        jcaDataSource = new JCADataSourceBuilder().jcaContextClass(JCALibrary.JNI_THREAD_SAFE).build();
        System.out.println("Supports variable arrays: " + jcaDataSource.isVarArraySupported());
        jcaDataSource.close();
    }
}
