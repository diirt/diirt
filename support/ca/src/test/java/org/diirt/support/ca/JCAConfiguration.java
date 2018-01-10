/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import gov.aps.jca.Context;

/**
 *
 * @author carcassi
 */
public class JCAConfiguration {
    public static void main(String[] args) {
        JCADataSourceConfiguration conf = new JCADataSourceConfiguration();
        Context context = conf.createContext();
        context.printInfo();
        context.dispose();

        System.out.println("---------------------------------");

        conf = new JCADataSourceConfiguration()
                .addContextProperty("auto_addr_list", "false")
                .addContextProperty("addr_list", "192.168.0.0");
        context = conf.createContext();
        context.printInfo();
        context.dispose();

    }
}
