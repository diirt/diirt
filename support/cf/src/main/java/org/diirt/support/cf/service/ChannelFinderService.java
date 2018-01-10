/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.cf.service;

import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;

/**
 * @author shroffk
 *
 */
public class ChannelFinderService {

    public static ServiceMethodDescription queryMethod(){
        return new ServiceMethodDescription("find", "Find Channels"){

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
                return new QueryServiceMethod(this, serviceDescription);
            }

        }.addArgument("query", "Query String", VString.class)
                .addResult("result", "Query Result", VTable.class)
                .addResult("result_size", "Query Result size", VNumber.class);
    }

    public static Service createService(){
        return new ServiceDescription("cf", "ChannelFinder service")
                .addServiceMethod(queryMethod())
                .createService();
    }

}
