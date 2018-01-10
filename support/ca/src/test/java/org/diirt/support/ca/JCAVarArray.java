/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.JCALibrary;

/**
 *
 * @author carcassi
 */
public class JCAVarArray {
    public static void main(String[] args) {
        // Test CAJ
        JCADataSource jcaDataSource = new JCADataSourceConfiguration().create();
        System.out.println("Supports variable arrays: " + jcaDataSource.isVarArraySupported());
        jcaDataSource.close();

        // Test JCA
        jcaDataSource = new JCADataSourceConfiguration().jcaContextClass(JCALibrary.JNI_THREAD_SAFE).create();
        System.out.println("Supports variable arrays: " + jcaDataSource.isVarArraySupported());
        jcaDataSource.close();
    }
}
