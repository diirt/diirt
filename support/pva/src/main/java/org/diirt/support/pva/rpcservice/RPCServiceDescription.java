/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
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

    public RPCServiceDescription(String name, String description, String hostName, String channelName,
            String methodFieldName, boolean useNTQuery) {
        super(name, description);
        this.hostName = hostName;
        this.channelName = channelName;
        this.methodFieldName = methodFieldName;
        this.useNTQuery = useNTQuery;
    }
}
