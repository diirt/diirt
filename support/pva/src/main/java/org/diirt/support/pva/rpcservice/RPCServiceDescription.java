/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.rpcservice;

import org.diirt.service.ServiceDescription;

/**
 * The description on how to construct a pvAccess RPC rpcservice.
 *
 * @author dkumar
 */
public class RPCServiceDescription extends ServiceDescription {

    final String hostName;
    final String channelName;
    final String methodFieldName;
    final boolean useNTQuery;

    /**
     * A new rpcservice description with the given rpcservice name and
     * description.
     *
     * @param name the name of the rpcservice
     * @param description a brief description
     */
    public RPCServiceDescription(String name, String description, String hostName, String channelName,
            String methodFieldName, boolean useNTQuery) {
        super(name, description);
        this.hostName = hostName;
        this.channelName = channelName;
        this.methodFieldName = methodFieldName;
        this.useNTQuery = useNTQuery;
    }

//    ServiceDescription createServiceDescription() {
//        for (RPCServiceMethodDescription rpcServiceMethodDescription : rpcServiceMethodDescriptions.values()) {
//            rpcServiceMethodDescription.executorService(executorService);
//            serviceDescription.addServiceMethod(new RPCServiceMethod(rpcServiceMethodDescription,
//                    this.hostName, this.channelName, this.methodFieldName, this.useNTQuery));
//        }
//        return serviceDescription;
//    }

}
